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

import java.net.URL;
import java.net.URLConnection;

public class URLContentFetcher {

  private URLContentFetcher() {

  }

  public static final String fetchTextFromURL(String url) {

    if (url == null)
      return null;

    try {

      URL daURL = new URL(url);
      URLConnection conn = daURL.openConnection();

      conn.connect();

      String contentType = conn.getContentType();

      if (!contentType.startsWith("text")) {

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

    } catch (Throwable e) {

      return null;
    }

  }

}
