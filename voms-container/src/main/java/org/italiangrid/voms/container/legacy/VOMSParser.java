package org.italiangrid.voms.container.legacy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.http.HttpParser;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.Buffers;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.io.EofException;
import org.eclipse.jetty.io.nio.DirectNIOBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VOMSParser extends HttpParser {

  public static final Logger log = LoggerFactory.getLogger(VOMSParser.class);

  public enum ParserStatus {
    START, FOUND_ZERO, FOUND_HTTP_REQUEST, FOUND_LEGACY_VOMS_REQUEST, PARSED_VOMS_REQUEST, NO_MORE_INPUT;
  }

  private static final EnumSet<ParserStatus> finalStatuses = EnumSet.of(
    ParserStatus.FOUND_HTTP_REQUEST, ParserStatus.PARSED_VOMS_REQUEST,
    ParserStatus.NO_MORE_INPUT);

  private static final byte ZERO = '0';
  private static final byte LEFT_BRACKET = '<';

  private ParserStatus _vomsParserStatus = ParserStatus.START;
  private Buffer _vomsBuffer = null;
  private final EndPoint _vomsEndpoint;
  private final EventHandler _vomsHandler;

  private final JettyHTTPGetRequestBuffers _requestBuffers;
  private final VOMSXMLRequestTranslator _xmlRequestTranslator;

  public static final Charset charset = Charset.forName("UTF-8");
  public static final CharsetDecoder decoder = charset.newDecoder();

  private static final Map<Buffer, Buffer> legacyRequestHeaders;

  static {
    legacyRequestHeaders = new HashMap<Buffer, Buffer>();

    for (LegacyHTTPHeader e : LegacyHTTPHeader.values()) {
      Buffer name = new DirectNIOBuffer(e.getHeaderName().length());
      name.put(e.getHeaderName().getBytes());
      Buffer value = new DirectNIOBuffer(e.getHeaderValue().length());
      value.put(e.getHeaderValue().getBytes());
      legacyRequestHeaders.put(name, value);
    }

  }

  public VOMSParser(Buffer buffer, EventHandler handler) {

    super(buffer, handler);
    _vomsEndpoint = null;
    _vomsHandler = handler;
    _requestBuffers = newRequestBuffers();
    _xmlRequestTranslator = newXMLRequestTranslator(_requestBuffers);

  }

  public VOMSParser(Buffers buffers, EndPoint endp, EventHandler handler) {

    super(buffers, endp, handler);
    _vomsEndpoint = endp;
    _vomsHandler = handler;
    _requestBuffers = newRequestBuffers();
    _xmlRequestTranslator = newXMLRequestTranslator(_requestBuffers);

  }

  protected JettyHTTPGetRequestBuffers newRequestBuffers() {

    return new HTTPRequestBuffers();
  }

  protected VOMSXMLRequestTranslator newXMLRequestTranslator(
    JettyHTTPGetRequestBuffers buffers) {

    return new VOMSXMLRequestTranslatorImpl();
  }

  protected void setParserStatus(ParserStatus s) {

    _vomsParserStatus = s;
  }

  protected boolean isDone() {

    return finalStatuses.contains(_vomsParserStatus);
  }

  protected int fillBuffer() throws IOException {

    int filled = -1;

    if (_vomsBuffer == null)
      _vomsBuffer = getHeaderBuffer();

    if (_vomsEndpoint != null) {

      if (_vomsBuffer.space() == 0) {
        _vomsBuffer.clear();
        throw new IllegalStateException("VOMS buffer full!");
      }

      try {

        filled = _vomsEndpoint.fill(_vomsBuffer);

      } catch (EofException e) {
        log.debug("Caught eof exception: {}", e.getMessage(), e);
        throw e;
      }
      log.debug("From endpoint filled {}", filled);
      return filled;
    }

    return -1;
  }

  protected void notifyHTTPRequestComplete() throws IOException {

    _vomsHandler.startRequest(_requestBuffers.getMethodBuffer(),
      _requestBuffers.getURIBuffer(), _requestBuffers.getVersionBuffer());

    for (Map.Entry<Buffer, Buffer> e : legacyRequestHeaders.entrySet()) {
      Buffer headerName = e.getKey();
      Buffer headerValue = e.getValue();
      _vomsHandler.parsedHeader(headerName, headerValue);
    }

    _vomsHandler.headerComplete();
  }

  protected void parseXML() {

    // Save indexes in case something goes wrong
    int getIndex = _vomsBuffer.getIndex();
    int putIndex = _vomsBuffer.putIndex();

    boolean success = _xmlRequestTranslator.translateLegacyRequest(_vomsBuffer,
      _requestBuffers);

    if (success) {
      setParserStatus(ParserStatus.PARSED_VOMS_REQUEST);
      try {
        notifyHTTPRequestComplete();
      } catch (IOException e) {
        log.error("Error completing legacy http request translation: {}",
          e.getMessage(), e);
        _requestBuffers.clearBuffers();
      }
    } else {
      // reset buffer indexes
      _vomsBuffer.setGetIndex(getIndex);
      _vomsBuffer.setPutIndex(putIndex);
    }
  }

  protected int parseVOMSRequest() throws IOException {

    log.debug("parseVOMSRequest(): endpoint: {}", _vomsEndpoint);
    int progress = 0;

    if (isDone())
      return 0;

    if (_vomsBuffer == null)
      _vomsBuffer = getHeaderBuffer();

    if (_vomsBuffer.length() == 0) {

      int filled = -1;

      try {

        filled = fillBuffer();
        log.debug("parseVOMSRequest(): _vomsBuffer: {}", _vomsBuffer);

      } catch (IOException e) {

        log.debug("Error filling buffer: {}", e.getMessage(), e);
      }

      if (filled > 0)
        progress++;

      else if (filled < 0) {
        setPersistent(false);
        log.debug("Error reading from channel, declaring parser done.");
        setParserStatus(ParserStatus.NO_MORE_INPUT);
        return -1;
      }
    }

    byte ch;

    while (!isDone() && (_vomsBuffer.length() > 0)) {

      if (_vomsParserStatus == ParserStatus.START
        || _vomsParserStatus == ParserStatus.FOUND_ZERO) {
        ch = _vomsBuffer.peek();
        log.debug("parseVOMSRequest(): first req char: {}", (char) ch);

        if (ch != ZERO && ch != LEFT_BRACKET) {
          // No VOMS nonsense around here, we're done, fall back to
          // Jetty HTTP parser
          log.debug("parseVOMSRequest(): no voms nonsense found, "
            + "it's a plain http request");
          setParserStatus(ParserStatus.FOUND_HTTP_REQUEST);
          return progress;
        }

        if (ch == ZERO) {
          _vomsBuffer.get();
          progress++;
          log.debug("parseVOMSRequest(): found zero as first request char, "
            + "swallowing it.");
          setParserStatus(ParserStatus.FOUND_ZERO);

        } else {
          log
            .debug("parseVOMSRequest(): found voms legacy request. proceeding to"
              + "parse XML");
          setParserStatus(ParserStatus.FOUND_LEGACY_VOMS_REQUEST);
          progress = progress + _vomsBuffer.length();
          parseXML();
        }

        continue;
      }

    }

    return progress;
  }

  @Override
  public boolean parseAvailable() throws IOException {

    if (isDone()) {
      log.debug("parseAvailable(): delegating up.");
      return super.parseAvailable();
    }

    boolean progress = parseVOMSRequest() > 0;
    log.debug("parseAvailable(): progress: {}", progress);

    while (!isDone() && _vomsBuffer != null && _vomsBuffer.length() > 0) {
      progress |= (parseVOMSRequest() > 0);
    }

    return progress;

  }

  @Override
  public boolean isIdle() {

    return _vomsParserStatus.equals(ParserStatus.START);
  }

  @Override
  public boolean isComplete() {

    log.debug("isComplete(): vomsParserDone: {}", isDone());
    if (isDone()) {
      boolean superComplete = super.isComplete();
      log.debug("isComplete(): httpParserDone: {}", superComplete);
      return superComplete;
    } else
      return false;
  }

  @Override
  public void reset() {

    log.debug("reset()");
    super.reset();
    setParserStatus(ParserStatus.START);
    _vomsBuffer = null;
    _requestBuffers.clearBuffers();
  }

  @Override
  public String toString() {

    return String.format("%s[status:%s];%s", getClass().getSimpleName(),
      _vomsParserStatus, super.toString());
  }
}
