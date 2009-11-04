package it.infn.cnaf.voms.x509;

import it.infn.cnaf.voms.aa.VOMSAttributes;
import it.infn.cnaf.voms.aa.VOMSFQAN;
import it.infn.cnaf.voms.aa.VOMSGenericAttribute;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.x509.AttributeCertificateHolder;
import org.bouncycastle.x509.AttributeCertificateIssuer;
import org.bouncycastle.x509.X509Attribute;
import org.bouncycastle.x509.X509V2AttributeCertificate;
import org.bouncycastle.x509.X509V2AttributeCertificateGenerator;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.common.VOMSException;
import org.glite.security.voms.admin.common.VOMSFatalException;
/**
 * 
 * @author Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *
 */
public class X509ACGenerator {

    public static final Log logger = LogFactory.getLog( X509ACGenerator.class );

    public static final String VOMS_OID = "1.3.6.1.4.1.8005.100.100";
    
    public static final String FQAN_OID = VOMS_OID + ".4";
    public static final String ISSUER_CERT_OID = VOMS_OID + ".10";
    public static final String TAGS_OID = VOMS_OID + ".11";
    
    public static final String TARGETS_OID = "2.5.29.55";
    public static final String NO_REV_AVAIL_OID = "2.5.29.56";
    public static final String AUTHORITY_KEY_ID_EXTENSION_OID = "2.5.29.35";
    

    public static final String SHA1_ENCRYPTION_SCHEME = "SHA1WithRSAEncryption";
    public static final String MD5_ENCRYPTION_SCHEME = "md5WithRSAEncryption";
    public static final String DEFAULT_ENCRYPTION_SCHEME = SHA1_ENCRYPTION_SCHEME;
    
    private static X509ACGenerator instance = null;

    private X509Certificate signerCert;

    private PrivateKey signerKey;

    private X509V2AttributeCertificateGenerator acGenerator;

    long lifetime = 1000 * 60 * 60 * 6;

    public X509ACGenerator( X509Certificate cert, PrivateKey key ) {

        assert cert != null : "Signer certificate is null!";
        assert key != null : "Signer private key is null!";

        signerCert = cert;
        signerKey = key;

        try {
            CertificateFactory.getInstance( "X.509", "BC" );

        } catch ( CertificateException e ) {

            logger.error( "Certificate factory initialization failed: "
                    + e.getMessage() );
            throw new VOMSFatalException(
                    "Certificate factory initialization failed: "
                            + e.getMessage(), e );

        } catch ( NoSuchProviderException e ) {
            logger.error( "Certificate factory initialization failed: "
                    + e.getMessage() );
            throw new VOMSFatalException(
                    "Certificate factory initialization failed: "
                            + e.getMessage(), e );
        }

        acGenerator = new X509V2AttributeCertificateGenerator();
        
        lifetime = VOMSConfiguration.instance().getLong( "voms.aa.default-ac-lifetime", 1000 * 60 * 60 * 12 );

    }

    public static X509ACGenerator instance( X509Certificate signerCertificate,
            PrivateKey signerKey ) {

        if ( instance == null )
            instance = new X509ACGenerator( signerCertificate, signerKey );

        return instance;
    }

    public static X509ACGenerator instance() {

        if ( instance == null )
            throw new IllegalStateException(
                    "Please initialize this object once using the instance(X509Certificate, PrivateKey) method!" );

        return instance;
    }

    public X509V2AttributeCertificate generateVOMSAttributeCertificate(
            X509Certificate userCertificate, VOMSAttributes userAttributes ) {

        assert userCertificate != null : "Cannot create an AC for a null user cert!";
        assert userAttributes != null : "Cannot create a VOMS AC without user attributes!";

        acGenerator.reset();

        prepareAC( userCertificate );

        encodeVOMSFQANs( userAttributes );
        encodeGAs( userAttributes );
        encodeNoRevAvail();
        encodeAuthorityKeyIdentifier();
        encodeTargets();      
        encodeIssuerCerts();
        

        X509V2AttributeCertificate ac;

        try {
            ac = (X509V2AttributeCertificate) acGenerator.generate( signerKey,
                    "BC" );

        } catch ( Exception e ) {

            logger.error( "Error generating AC: " + e.getMessage() );
            throw new VOMSException( "Error generating AC: " + e.getMessage(),
                    e );
        }

        return ac;
    }

    private void encodeAuthorityKeyIdentifier() {

        // Get AuthrorityKeyIdentifier extension from the certificate if present
        byte[] authKeyId = signerCert.getExtensionValue( AUTHORITY_KEY_ID_EXTENSION_OID );
        
        if (authKeyId != null){
            AuthorityKeyIdentifier aki = new AuthorityKeyIdentifier(authKeyId);
                        
            try {
                acGenerator.addExtension( AUTHORITY_KEY_ID_EXTENSION_OID, false, aki );
            
            } catch ( IOException e ) {
                
                logger.error("Error encoding authority key identifier inside AC: "+e.getMessage());
                throw new VOMSException("Error encoding authority key identifier inside AC: "+e.getMessage(),e);
                
            }
        }
        
    }

    private void encodeNoRevAvail() {

        try {
            
            acGenerator.addExtension( NO_REV_AVAIL_OID, false, new DERNull() );
            
        } catch ( IOException e ) {
            
            logger.error("Error encoding id-ce-NoRevAvail in AttributeCertificate: "+e.getMessage());
            throw new VOMSException("Error encoding id-ce-NoRevAvail in AttributeCertificate: "+e.getMessage(),e);
        }
        

    }

    private void encodeTargets() {

        // TODO implement me! Nobody is using this feature anyway...

    }

    private BigInteger computeSerialNumber() {

        ByteBuffer buf = ByteBuffer.allocate( 16 );

        UUID r = UUID.randomUUID();

        long lsb = r.getLeastSignificantBits();
        long msb = r.getMostSignificantBits();

        buf.putLong( r.getMostSignificantBits() );
        buf.putLong( r.getLeastSignificantBits() );

        buf.flip();

        BigInteger bi = new BigInteger( buf.array() );
        return bi.abs();

    }

    private void prepareAC( X509Certificate userCert ) {

        AttributeCertificateHolder holder = new AttributeCertificateHolder(
                userCert.getSubjectX500Principal(), userCert.getSerialNumber() );

        acGenerator.setHolder( holder );

        try {
            acGenerator.setIssuer( new AttributeCertificateIssuer( signerCert
                    .getSubjectX500Principal() ) );

        } catch ( IOException e ) {
            throw new VOMSException( "Error setting AC Issuer: "
                    + e.getMessage(), e );
        }

        acGenerator.setSerialNumber( computeSerialNumber() );

        long maximumACLifetime = VOMSConfiguration.instance().getLong( "voms.aa.max-ac-lifetime", 1000 * 60 * 60 * 24 );
        long defaultACLifetime = VOMSConfiguration.instance().getLong( "voms.aa.default-ac-lifetime", 1000 * 60 * 60 * 12 );
        
        long thisLifetime; 
        
        if (lifetime > maximumACLifetime)
            thisLifetime = defaultACLifetime;
        else
            thisLifetime = lifetime;
        
        Date now = new Date();
        Date end = new Date( now.getTime() + thisLifetime );
        
        // Reset lifetime to the default value for future calls
        thisLifetime = defaultACLifetime;

        acGenerator.setNotBefore( now );
        acGenerator.setNotAfter( end );

        acGenerator.setSignatureAlgorithm( DEFAULT_ENCRYPTION_SCHEME );

    }

    private String getVOUri() {

        try {
            String voName = VOMSConfiguration.instance().getVOName();
            String hostName = InetAddress.getLocalHost().getHostName();

            return voName + "://" + hostName + ":8443";

        } catch ( UnknownHostException e ) {
            throw new VOMSException(
                    "Cannot get information about local host name! "
                            + e.getMessage(), e );
        }
    }

    private void encodeVOMSFQANs( VOMSAttributes attrs ) {

        assert ( attrs.getFqans() != null && attrs.getFqans().size() != 0 ) : "Cannot create an AC without FQANS!";

        ASN1EncodableVector acStructureVector = new ASN1EncodableVector();
        ASN1EncodableVector fqansVector = new ASN1EncodableVector();

        // VOMS bug? RFC states that policyAuthority is a tagged GeneralNames object,
        // while ACs issued by VOMS have a tagged GeneralName object here...
        acStructureVector.add( new DERTaggedObject(0,buildPolicyAuthorityInfo()));
        
        
        for ( VOMSFQAN fqan : attrs.getFqans() ) {
            DEROctetString os = getDEROctetString( fqan.getFQAN() );
            fqansVector.add( os );
        }

        acStructureVector.add( new DERSequence( fqansVector ) );

        X509Attribute vomsFqanAttribute = new X509Attribute( FQAN_OID,
                new DERSequence(acStructureVector) );

        acGenerator.addAttribute( vomsFqanAttribute );

    }

    private DEROctetString getDEROctetString( String s ) {

        return new DEROctetString( s.getBytes() );
    }

    private DERSequence buildTagSequence( VOMSGenericAttribute ga ) {

        assert ga != null : "Cannot build a tag sequence for a null generic attribute!";

        ASN1EncodableVector tagSequence = new ASN1EncodableVector();

        tagSequence.add( getDEROctetString( ga.getName() ) );
        tagSequence.add( getDEROctetString( ga.getValue() ) );
        tagSequence.add( getDEROctetString( ga.getContext() ) );

        return new DERSequence( tagSequence );

    }

    private void encodeGAs( VOMSAttributes attrs ) {

        if ( attrs.getGenericAttributes() == null
                || attrs.getGenericAttributes().isEmpty() )
            return;

        ASN1EncodableVector tagContainer = new ASN1EncodableVector();
        ASN1EncodableVector tagSequences = new ASN1EncodableVector();

        for ( VOMSGenericAttribute ga : attrs.getGenericAttributes() )
            tagSequences.add( buildTagSequence( ga ) );

        
        // Why VOMS encodes GeneralNames differently in FQANS and GAs? (voms bug?)
        tagContainer.add( new GeneralNames(buildPolicyAuthorityInfo()) );
        
        tagContainer.add( new DERSequence( tagSequences ) );

        
        try {
            // Why VOMS wraps GAs three times more than what's described in the AC 
            // description format? (voms bug?)
            acGenerator.addExtension( TAGS_OID, false, new DERSequence( new DERSequence( new DERSequence(
                    tagContainer ) ) ) );

        } catch ( IOException e ) {

            throw new VOMSException(
                    "Erro generating AC generic attributes extension:"
                            + e.getMessage(), e );

        }
    }

    private void encodeIssuerCerts() {

        // FIXME: considers only the signer certificate (it should encode the
        // whole
        // chain up to (and excluding) the CA cert for the signer certificate).
        try {
            
            ASN1EncodableVector issuerCertsContainer = new ASN1EncodableVector();
            
            issuerCertsContainer.add( new DERSequence(getCertAsDEREncodable( signerCert )));
            
            acGenerator.addExtension( ISSUER_CERT_OID, false, new DERSequence(issuerCertsContainer) );

        } catch ( IOException e ) {
            logger
                    .error( "Error adding issuer cert chain extension to the ac: "
                            + e.getMessage() );
            throw new VOMSException(
                    "Error adding issuer cert chain extension to the ac: "
                            + e.getMessage(), e );
        }
    }

    private GeneralName buildPolicyAuthorityInfo() {

        return new GeneralName(
                GeneralName.uniformResourceIdentifier, getVOUri() ) ;
        
    }
    
    private DEREncodable getCertAsDEREncodable(X509Certificate cert){
        
        try {
            byte[] certBytes = cert.getEncoded();
            
            ByteArrayInputStream bais = new ByteArrayInputStream(certBytes);
            ASN1InputStream is = new ASN1InputStream(bais);
            DERObject derCert = is.readObject();
            return derCert;
        
        } catch ( CertificateEncodingException e ) {
            throw new VOMSException("Error encoding X509 certificate: "+ e.getMessage(),e);
        } catch ( IOException e ) {
            throw new VOMSException("Error encoding X509 certificate: "+ e.getMessage(),e);
        }
        
    }
    
    
    public long getLifetime() {

        return lifetime;
    }

    
    public void setLifetime( long lifetime ) {

        this.lifetime = lifetime;
    }
}
