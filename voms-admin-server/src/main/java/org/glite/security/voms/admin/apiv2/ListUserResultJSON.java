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
package org.glite.security.voms.admin.apiv2;

import java.util.List;

public class ListUserResultJSON {
  
  long startIndex;
  long pageSize;
  long count;
  
  List<VOMSUserJSON> result;

  public long getStartIndex() {
    return startIndex;
  }

  public void setStartIndex(long startIndex) {
    this.startIndex = startIndex;
  }

  public long getPageSize() {
    return pageSize;
  }

  public void setPageSize(long pageSize) {
    this.pageSize = pageSize;
  }
 
  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public List<VOMSUserJSON> getResult() {
    return result;
  }

  public void setResult(List<VOMSUserJSON> result) {
    this.result = result;
  }
}
