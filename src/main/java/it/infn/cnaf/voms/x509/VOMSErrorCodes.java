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

package it.infn.cnaf.voms.x509;

public interface VOMSErrorCodes {
	
	public static final String VOMS_ERROR_NO_SUCH_USER = "NoSuchUser";
	public static final String VOMS_ERROR_SUSPENDED_USER = "SuspendedUser";
	public static final String VOMS_ERROR_SUSPENDED_CERTIFICATE = "SuspendedCertificate";
	public static final String VOMS_ERROR_BAD_REQUEST = "BadRequest";
	public static final String VOMS_ERROR_INTERNAL_ERROR = "InternalError";
	
}
