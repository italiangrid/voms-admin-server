/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.util;

import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.x500.X500Principal;

import org.glite.security.voms.admin.error.VOMSException;

import eu.emi.security.authn.x509.impl.OpensslNameUtils;
import eu.emi.security.authn.x509.impl.X500NameUtils;

/**
 * Utility class to convert principal names to convenient format.
 * 
 * @see DNUtil
 * 
 */
public class DNUtil {
	
	/**
	 * Converts BouncyCastle's X500 principal to something, which is used in the
	 * Globus libraries by default (whatever it is).
	 * 
	 * @param principal
	 *            the subject's or the issuer's X500 principal
	 * @return the proper string represetnation
	 */
	public static String getOpenSSLSubject(X500Principal principal) {

		return OpensslNameUtils.convertFromRfc2253(X500NameUtils.getReadableForm(principal), false);
	}

	public static String normalizeEmailAddressInDN(String dn) {
		return dn
				.replaceAll(
						"/(E|e|((E|e|)(mail|mailAddress|mailaddress|MAIL|MAILADDRESS)))=",
						"/Email=");
	}

	public static String normalizeUIDInDN(String dn) {

		return dn
				.replaceAll("/(UserId|USERID|userId|userid|uid|Uid)=", "/UID=");
	}

	public static String normalizeDN(String dn) {
		return normalizeUIDInDN(normalizeEmailAddressInDN(dn));
	}

	public static String getEmailAddressFromDN(String dn) {

		Pattern emailPattern = Pattern.compile("Email=([^/]*)");
		Matcher m = emailPattern.matcher(dn);

		if (m.find()) {

			return m.group(1);
		}

		return null;
	}

	public static String getEmailAddressFromExtensions(X509Certificate cert) {

		try {

			Collection<List<?>> altNames = cert.getSubjectAlternativeNames();

			if (altNames == null)
				return null;

			// Iterate over alternative names
			for (List<?> entry : altNames) {

				// First item in the list is an integer specify the altName
				// 'kind'
				int entryType = (Integer) entry.get(0);

				// 1 is the code for rfc822 name, we consider only the first
				// address
				// in the list.

				if (entryType == 1)
					return (String) entry.get(1);
				else
					continue;

			}

		} catch (CertificateParsingException e) {
			throw new VOMSException(
					"Error accessing subject alternative names: "
							+ e.getMessage(), e);
		}

		return null;
	}
	
	public static boolean isRFC2253Conformant(String subjectNameIDValue) {
		
		// TODO: Migrate this method from old VOMS SAML codebase
		return true;
	}
	
	public static String getBCasX500( String principalString){
        
        return getOpenSSLSubject( new X500Principal(principalString ));
        
    }
}

// Please do not change this line.
// arch-tag: b7901446-4a9f-4e35-b334-55e4ef43d304
