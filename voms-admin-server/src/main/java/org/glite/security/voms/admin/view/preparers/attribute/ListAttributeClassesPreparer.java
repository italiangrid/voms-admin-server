/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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
 */
package org.glite.security.voms.admin.view.preparers.attribute;

import static org.apache.tiles.request.Request.REQUEST_SCOPE;

import java.util.List;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.request.Request;
import org.glite.security.voms.admin.operations.attributes.ListAttributeDescriptionsOperation;
import org.glite.security.voms.admin.persistence.model.attribute.VOMSAttributeDescription;

public class ListAttributeClassesPreparer implements ViewPreparer {

  @Override
  public void execute(Request request, AttributeContext attributeContext) {

    @SuppressWarnings("unchecked")
    List<VOMSAttributeDescription> attributeClasses =
        (List<VOMSAttributeDescription>) ListAttributeDescriptionsOperation.instance().execute();

    request.getContext(REQUEST_SCOPE).put("attributeClasses", attributeClasses);
  }

}
