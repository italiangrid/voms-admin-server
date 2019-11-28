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
package org.glite.security.voms.admin.integration.cern.dto;

import java.util.List;

public class ListResponseDTO<T> {

  private final long totalResults;
  private final int itemsPerPage;
  private final int startIndex;

  private final List<T> resources;

  private ListResponseDTO(Builder<T> builder) {
    this.totalResults = builder.totalResults;
    this.startIndex = builder.startIndex;
    this.itemsPerPage = builder.itemsPerPage;
    this.resources = builder.resources;
  }

  public long getTotalResults() {
    return totalResults;
  }

  public int getItemsPerPage() {
    return itemsPerPage;
  }

  public int getStartIndex() {
    return startIndex;
  }

  public List<T> getResources() {
    return resources;
  }

  public static class Builder<T> {
    private long totalResults;
    private int itemsPerPage;
    private int startIndex;
    private List<T> resources;

    public Builder<T> totalResults(long totalResults) {
      this.totalResults = totalResults;
      return this;
    }

    public Builder<T> itemsPerPage(int itemsPerPage) {
      this.itemsPerPage = itemsPerPage;
      return this;
    }

    public Builder<T> startIndex(int startIndex) {
      this.startIndex = startIndex;
      return this;
    }

    public Builder<T> resources(List<T> resources) {
      this.resources = resources;
      return this;
    }

    public ListResponseDTO<T> build() {
      return new ListResponseDTO<>(this);
    }
  }

  public static <T> Builder<T> builder() {
    return new Builder<>();
  }
}
