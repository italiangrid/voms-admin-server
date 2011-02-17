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

package org.glite.security.voms.admin.configuration;

public interface VOMSConfigurationConstants {

    	/**
    	 * Internal property used to hold vo name
    	 */
    	public static final String VO_NAME = "voms.vo.name";
    	
	public static final String LOCALHOST_DEFAULTS_TO_LOCAL_ADMIN = "voms.localhost.defaults.to.local.admin";
	
	public static final String READONLY = "voms.readonly";
	
	public static final String CAFILES = "voms.cafiles";
	public static final String CAFILES_PERIOD = "voms.cafiles.period";
	
	
	public static final String CREATE_EXPIRED_CAS = "voms.ca.create-when-expired";
	public static final String DROP_EXPIRED_CAS = "voms.ca.drop-when-expired";
	public static final String AUDITING = "voms.auditing";
	public static final String AUDITING_INSERTS = "voms.auditing.inserts";
	public static final String AUDITING_UPDATES = "voms.auditing.updates";
	public static final String AUDITING_DELETIONS = "voms.auditing.deletions";
	
	public static final String SERVICE_EMAIL_ADDRESS = "voms.notification.email-address";
	public static final String SERVICE_SMTP_SERVER = "voms.notification.smtp-server";
	public static final String SERVICE_SMTP_SERVER_PORT = "voms.notification.smtp-server.port";
	public static final String SERVICE_EMAIL_ACCOUNT_USERNAME = "voms.notification.username";
	public static final String SERVICE_EMAIL_ACCOUNT_PASSWORD = "voms.notification.password";
	public static final String SERVICE_EMAIL_USE_TLS = "voms.notification.use_tls";
	public static final String NOTIFICATION_NOTIFY_BEHAVIOUR = "voms.notification.notify";
	public static final String NOTIFICATION_RETRY_PERIOD = "voms.notification.retry_period";
	
	/**
	 * VO Membership requests expiration time (in minutes).
	 */
	public static final String VO_MEMBERSHIP_EXPIRATION_TIME = "voms.request.vo-membership-expiration-time";
	
	public static final String USER_MAX_RESULTS_PER_PAGE = "voms.pagination.user.max.results.per.page";
	public static final String ATTRIBUTES_MAX_RESULTS_PER_PAGE = "voms.pagination.attributes.max.results.per.page";
	public static final String GROUP_MAX_RESULTS_PER_PAGE = "voms.pagination.group.max.results.per.page";
	public static final String ROLE_MAX_RESULTS_PER_PAGE = "voms.pagination.role.max.results.per.page";
	public static final String REGISTRATION_SERVICE_ENABLED = "voms.request.webui.enabled";
	public static final String READ_ACCESS_FOR_AUTHENTICATED_CLIENTS = "voms.read-access-for-authenticated-clients";
	/**
	 * VERSION Properties
	 */
	
	public static final String VOMS_ADMIN_SERVER_VERSION = "voms-admin.server.version";
	public static final String VOMS_ADMIN_INTERFACE_VERSION = "voms-admin.interface.version";
	
	/**
	 * AUP Properties
	 */
	public static final String GRID_AUP_URL = "voms.aup.grid_aup.initial_url";
	public static final String VO_AUP_URL = "voms.aup.vo_aup.initial_url";
	public static final String SIGN_AUP_TASK_LIFETIME = "voms.aup.sign_aup_task_lifetime";
	/**
	 * Membership Properties
	 */
	public static final String DEFAULT_MEMBERSHIP_LIFETIME = "voms.membership.default_lifetime";
	public static final String MEMBERSHIP_CHECK_PERIOD = "voms.task.membership_check.period";
	public static final String MEMBERSHIP_EXPIRATION_WARNING_PERIOD = "voms.membership.expiration_warning_period";
	
	/**
	 * Attribute Authority Properties
	 */
	public static final String VOMS_AA_CERT_FILE = "voms.aa.certificate";
	public static final String VOMS_AA_KEY_FILE = "voms.aa.key";
	public static final String VOMS_SAML_MAX_ASSERTION_LIFETIME = "voms.saml.max_assertion_lifetime";
	public static final String VOMS_AA_COMPULSORY_GROUP_MEMBERSHIP = "voms.aa.compulsory_group_membership";
	public static final String VOMS_AA_SAML_ACTIVATE_ENDPOINT = "voms.aa.activate_saml_endpoint";
	public static final String VOMS_AA_REST_ACTIVATE_ENDPOINT = "voms.aa.activate_rest_endpoint";
	public static final String VOMS_UNAUTHENTICATED_CLIENT_PERMISSION_MASK = "voms.unauthenticated_client_permission_mask";
	
	/**
	 * External validation plugin Properties
	 */

	public static final String VOMS_EXTERNAL_VALIDATOR_LIST = "voms.external-validators";
	public static final String VOMS_EXTERNAL_VALIDATOR_PREFIX = "voms.ext";
	public static final String VOMS_EXTERNAL_VALIDATOR_CONFIG_SUFFIX = "configClass"; 
	
	/**
	 * Internal registration properties
	 */
	
	public static final String VOMS_INTERNAL_REGISTRATION_TYPE = "___voms.regitration.type";
	public static final String VOMS_INTERNAL_RO_PERSONAL_INFORMATION = "___voms.read-only-personal-information";
	
	/**
	 * Executor service properties
	 */
	public static final String THREAD_POOL_SIZE_PROPERTY = "voms.background_tasks.thread_pool_size";
	
	/**
	 * Axis Web services CSRF guard
	 */
	public static final String VOMS_CSRF_GUARD_LOG_ONLY = "voms.csrf.log_only"; 
	
}