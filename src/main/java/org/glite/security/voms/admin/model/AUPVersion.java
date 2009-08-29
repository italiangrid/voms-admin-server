package org.glite.security.voms.admin.model;

import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AUPVersion implements Serializable, Comparable<AUPVersion> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(AUPVersion.class);

	Long id;

	AUP aup = null;

	String version;

	String url;

	String text;

	Date creationTime;

	Date lastForcedReacceptanceTime;

	Boolean active;

	public AUPVersion() {

		setActive(false);
	}

	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (!(other instanceof AUPVersion))
			return false;

		if (other == null)
			return false;

		AUPVersion that = (AUPVersion) other;

		if (getAup().equals(that.getAup()))
			return getVersion().equals(that.getVersion());
		else
			return false;

	}

	@Override
	public int hashCode() {

		if (this.getId() != null)
			return getId().hashCode();

		return super.hashCode();
	}

	// Getters and Setters
	/**
	 * @return the id
	 */
	public Long getId() {

		return id;
	}

	/**
	 * @return the aup
	 */
	public AUP getAup() {

		return aup;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {

		return version;
	}

	/**
	 * @return the creationTime
	 */
	public Date getCreationTime() {

		return creationTime;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {

		this.id = id;
	}

	/**
	 * @param aup
	 *            the aup to set
	 */
	public void setAup(AUP aup) {

		this.aup = aup;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {

		this.version = version;
	}

	/**
	 * @param creationTime
	 *            the creationTime to set
	 */
	public void setCreationTime(Date creationTime) {

		this.creationTime = creationTime;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {

		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {

		this.url = url;
	}

	@Override
	public String toString() {

		return String.format("[version:%s, url:%s, creationTime:%s]", version,
				url, creationTime.toString());

	}

	public int compareTo(AUPVersion o) {

		return getCreationTime().compareTo(o.getCreationTime());
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getURLContent() {

		if (getUrl() == null)
			return null;

		try {

			URL daURL = new URL(getUrl());
			URLConnection conn = daURL.openConnection();

			conn.connect();

			String contentType = conn.getContentType();

			if (!contentType.startsWith("text")) {
				log.error("Unsupported content type for AUP: " + contentType);
				return null;

			} else {

				// FIXME: leverage CONTENT length,
				StringBuilder text = new StringBuilder();

				int c;
				// FIXME: implement more efficient AUP fetching
				while ((c = conn.getInputStream().read()) != -1) {
					text.append((char) c);
				}

				return text.toString();

			}

		} catch (Exception e) {
			log.error("Error in opening AUP version url: " + e.getMessage());
			if (log.isDebugEnabled())
				log.error(
						"Error in opening AUP version url: " + e.getMessage(),
						e);
			return null;
		}

	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getLastForcedReacceptanceTime() {
		return lastForcedReacceptanceTime;
	}

	public void setLastForcedReacceptanceTime(Date lastForcedReacceptanceTime) {
		this.lastForcedReacceptanceTime = lastForcedReacceptanceTime;
	}

	public Date getLastUpdateTime() {

		if (getLastForcedReacceptanceTime() != null)
			return getLastForcedReacceptanceTime();
		else
			return getCreationTime();
	}

}
