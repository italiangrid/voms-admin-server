/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/
package org.glite.security.voms.admin.jsp;

import java.net.MalformedURLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.taglib.TagUtils;
import org.glite.security.voms.admin.dao.SearchResults;


public class SearchNavBarTag extends TagSupport {

    private static final Log log = LogFactory.getLog( SearchNavBarTag.class );

    String id;

    String searchURL;

    String context;

    String permission;

    String styleClass;

    String disabledLinkStyleClass;

    String linkStyleClass;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SearchNavBarTag() {

        super();

    }

    protected String buildURL( String text, int firstResult )
            throws MalformedURLException {

        HashMap params = new HashMap();

        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();

        String url = req.getContextPath() + searchURL;

        params.put( "firstResults", new Integer( firstResult ) );
        params.put( "text", text );

        return TagUtils.getInstance().computeURL( pageContext, null, url, null,
                null, null, params, null, false );
    }

    protected void writeLink( TagUtils t, SearchResults res, int firstResult,
            String content ) throws JspException {

        String link;
        String url;

        try {
            url = buildURL( res.getSearchString(), firstResult );

        } catch ( MalformedURLException e ) {

            throw new JspTagException( "Error building searchURL: "
                    + e.getMessage(), e );

        }

        if ( org.glite.security.voms.admin.jsp.TagUtils.isAuthorized( pageContext, context, permission ) ) {

            link = "<a href=\"" + url + "\" class=\"" + linkStyleClass + "\">"
                    + content + "</a>";

        } else
            link = "<div class=\"" + disabledLinkStyleClass + "\">" + content
                    + "</div>";

        t.write( pageContext, link );

    }

    protected void writeFirst( TagUtils t, SearchResults res )
            throws JspException {

        writeLink( t, res, 0, "first" );

    }

    protected void writeLast( TagUtils t, SearchResults res )
            throws JspException {

        writeLink( t, res, res.getCount() - res.getResultsPerPage(), "last" );

    }

    protected void writePrevious( TagUtils t, SearchResults res )
            throws JspException {

        int previousIndex = res.getFirstResult() - res.getResultsPerPage();
        if (previousIndex < 0)
            previousIndex = 0;
        
        writeLink( t, res, previousIndex,
                "&lt;" );

    }

    protected void writeNext( TagUtils t, SearchResults res )
            throws JspException {
        
        writeLink( t, res, res.getFirstResult() + res.getResultsPerPage(),
                "&gt;" );

    }

    protected void writeResultCount( TagUtils t, SearchResults res )
            throws JspException {

        String resCount = ( res.getFirstResult() + 1 ) + "-"
                + ( res.getFirstResult() + res.getResults().size() ) + " of "
                + res.getCount();

        t.write( pageContext, resCount );
    }

    public int doStartTag() throws JspException {

        SearchResults results = (SearchResults) pageContext.findAttribute( id );

        TagUtils tUtils = TagUtils.getInstance();
        if ( results == null )
            throw new JspTagException(
                    "SearchResults not found in pageContext. Key: " + id );

        tUtils.write( pageContext, "<div class=\"" + styleClass + "\">\n" );

        if ( results.getFirstResult() > 0 ) {

            // Not the second page
            if ( ( results.getFirstResult() - results.getResultsPerPage() ) > 0 )
                writeFirst( tUtils, results );

            writePrevious( tUtils, results );

        }

        writeResultCount( tUtils, results );

        if ( results.getRemainingCount() > 0 ) {

            writeNext( tUtils, results );

            // Not the last - 1 page.
            if ( results.getRemainingCount() > results.getResultsPerPage()) {

                writeLast( tUtils, results );
            }
        }

        tUtils.write( pageContext, "</div>" );
        return SKIP_BODY;
    }

    public String getContext() {

        return context;
    }

    public void setContext( String context ) {

        this.context = context;
    }

    public String getDisabledLinkStyleClass() {

        return disabledLinkStyleClass;
    }

    public void setDisabledLinkStyleClass( String disabledLinkStyleClass ) {

        this.disabledLinkStyleClass = disabledLinkStyleClass;
    }

    public String getId() {

        return id;
    }

    public void setId( String id ) {

        this.id = id;
    }

    public String getLinkStyleClass() {

        return linkStyleClass;
    }

    public void setLinkStyleClass( String linkStyleClass ) {

        this.linkStyleClass = linkStyleClass;
    }

    public String getPermission() {

        return permission;
    }

    public void setPermission( String permission ) {

        this.permission = permission;
    }

    public String getSearchURL() {

        return searchURL;
    }

    public void setSearchURL( String searchURL ) {

        this.searchURL = searchURL;
    }

    public String getStyleClass() {

        return styleClass;
    }

    public void setStyleClass( String styleClass ) {

        this.styleClass = styleClass;
    }

}
