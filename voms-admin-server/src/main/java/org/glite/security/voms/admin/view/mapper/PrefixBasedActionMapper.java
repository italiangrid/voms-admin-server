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

package org.glite.security.voms.admin.view.mapper;

import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.dispatcher.mapper.ActionMapper;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.dispatcher.mapper.DefaultActionMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <!-- START SNIPPET: description -->
 * 
 * A prefix based action mapper that is capable of delegating to other
 * {@link ActionMapper}s based on the request's prefix
 *
 * It is configured through struts.xml
 *
 * For example, with the following entries in struts.properties
 *
 * <pre>
 * &lt;constant name="struts.mapper.class" value="org.apache.struts2.dispatcher.mapper.PrefixBasedActionMapper"/&gt;
 * &lt;constant name="struts.mapper.prefixMapping" value="/communities:pseudoRestful,/communityTags:pseudoRestful,/events:pseudoRestful,/mediaList:pseudoRestful,/users:pseudoRestful,/community:struts,/communityTag:struts,/event:struts,/media:struts,/user:struts,:struts"/&gt;
 * </pre>
 * <p/>
 * When
 * {@link PrefixBasedActionMapper#getMapping(HttpServletRequest, ConfigurationManager)}
 * or {@link PrefixBasedActionMapper#getUriFromActionMapping(ActionMapping)} is
 * invoked, {@link PrefixBasedActionMapper} will check each possible prefix (url
 * prefix terminating just before a /) to find the most specific ActionMapper
 * that returns a mapping when asked to map the request. If none are found, null
 * is returned for both
 * {@link PrefixBasedActionMapper#getMapping(HttpServletRequest, ConfigurationManager)}
 * and {@link PrefixBasedActionMapper#getUriFromActionMapping(ActionMapping)}
 * methods.
 * <p/>
 *
 * <!-- END SNIPPET: description -->
 *
 * @see ActionMapper
 * @see ActionMapping
 *
 */
public class PrefixBasedActionMapper extends DefaultActionMapper implements
  ActionMapper {

  protected transient final Log log = LogFactory.getLog(getClass());
  protected Container container;
  protected Map<String, ActionMapper> actionMappers = new HashMap<String, ActionMapper>();

  @Inject
  public void setContainer(Container container) {

    this.container = container;
  }

  @Inject(StrutsConstants.PREFIX_BASED_MAPPER_CONFIGURATION)
  public void setPrefixBasedActionMappers(String list) {

    if (list != null) {
      String[] mappers = list.split(",");
      for (String mapper : mappers) {
        String[] thisMapper = mapper.split(":");
        if ((thisMapper != null) && (thisMapper.length == 2)) {
          String mapperPrefix = thisMapper[0].trim();
          String mapperName = thisMapper[1].trim();
          Object obj = container.getInstance(ActionMapper.class, mapperName);
          if (obj != null) {
            actionMappers.put(mapperPrefix, (ActionMapper) obj);
          } else if (log.isDebugEnabled()) {
            log
              .debug("invalid PrefixBasedActionMapper config entry: " + mapper);
          }
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  public ActionMapping getMapping(HttpServletRequest request,
    ConfigurationManager configManager) {

    String uri = getUri(request);
    for (int lastIndex = uri.lastIndexOf('/'); lastIndex > (-1); lastIndex = uri
      .lastIndexOf('/', lastIndex - 1)) {
      ActionMapper actionMapper = actionMappers
        .get(uri.substring(0, lastIndex));
      if (actionMapper != null) {
        ActionMapping actionMapping = actionMapper.getMapping(request,
          configManager);
        if (log.isDebugEnabled()) {
          log.debug("Using ActionMapper " + actionMapper);
        }
        if (actionMapping != null) {
          if (log.isDebugEnabled()) {
            if (actionMapping.getParams() != null) {
              log.debug("ActionMapper found mapping. Parameters: "
                + actionMapping.getParams());
              for (Map.Entry<String, Object> mappingParameterEntry : ((Map<String, Object>) (actionMapping
                .getParams())).entrySet()) {
                Object paramValue = mappingParameterEntry.getValue();
                if (paramValue == null) {
                  log.debug(mappingParameterEntry.getKey() + " : null!");
                } else if (paramValue instanceof String[]) {
                  log.debug(mappingParameterEntry.getKey() + " : (String[]) "
                    + Arrays.toString((String[]) paramValue));
                } else if (paramValue instanceof String) {
                  log.debug(mappingParameterEntry.getKey() + " : (String) "
                    + (String) paramValue);
                } else {
                  log.debug(mappingParameterEntry.getKey() + " : (Object) "
                    + (paramValue.toString()));
                }
              }
            }
          }
          return actionMapping;
        } else if (log.isDebugEnabled()) {
          log.debug("ActionMapper " + actionMapper
            + " failed to return an ActionMapping");
        }
      }
    }
    if (log.isDebugEnabled()) {
      log.debug("no ActionMapper found");
    }
    return null;
  }

  public String getUriFromActionMapping(ActionMapping mapping) {

    String namespace = mapping.getNamespace();

    // Handle null namespace gracefully
    if (namespace == null)
      namespace = "";

    for (int lastIndex = namespace.length(); lastIndex > (-1); lastIndex = namespace
      .lastIndexOf('/', lastIndex - 1)) {
      ActionMapper actionMapper = actionMappers.get(namespace.substring(0,
        lastIndex));
      if (actionMapper != null) {
        String uri = actionMapper.getUriFromActionMapping(mapping);
        if (log.isDebugEnabled()) {
          log.debug("Using ActionMapper " + actionMapper);
        }
        if (uri != null) {
          return uri;
        } else if (log.isDebugEnabled()) {
          log.debug("ActionMapper " + actionMapper
            + " failed to return an ActionMapping (null)");
        }
      }
    }
    if (log.isDebugEnabled()) {
      log.debug("ActionMapper failed to return a uri");
    }
    return null;
  }
}
