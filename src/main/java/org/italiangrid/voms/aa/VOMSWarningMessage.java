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

package org.italiangrid.voms.aa;


public class VOMSWarningMessage {

	private final VOMSWarning warning;
	private final String vo;
	private final String message;
	
	private VOMSWarningMessage(VOMSWarning warning, String vo) {
		this.warning = warning;
		this.vo = vo;
		this.message = null;
	}

	private VOMSWarningMessage(VOMSWarning warning, String vo, String message){
		this.warning = warning;
		this.vo = vo;
		this.message = message;
	}
	
	/**
	 * @return the vo
	 */
	public String getVo() {
		return vo;
	}

	
	/**
	 * @return the message
	 */
	public String getMessage() {
		if (message == null)
			return warning.getDefaultMessage();
		return message;
	}
	
	/**
	 * @return the warning
	 */
	public VOMSWarning getWarning() {
		return warning;
	}
	
	public static VOMSWarningMessage orderingNotSatisfied(String vo){
		return new VOMSWarningMessage(VOMSWarning.OrderNotSatisfied,vo);
	}
	
	public static VOMSWarningMessage shortenedAttributeValidity(String vo){
		return new VOMSWarningMessage(VOMSWarning.ShortenedAttributeValidity,vo);
	}
	
	public static VOMSWarningMessage attributeSubset(String vo){
		return new VOMSWarningMessage(VOMSWarning.AttributeSubset,vo);
	}
	
}
