package org.glite.security.voms.admin.jsp;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.glite.security.voms.admin.common.VOMSConfiguration;

public class VersionTag extends TagSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public VersionTag() {

        super();
    }

    @Override
    public int doStartTag() throws JspException {

        VOMSConfiguration conf = VOMSConfiguration.instance();

        String version = conf
                .getString( VOMSConfiguration.VOMS_ADMIN_SERVER_VERSION );

        try {

            if ( version == null )

                pageContext.getOut().print( "undefined version" );

            else
                pageContext.getOut().print( version );

        } catch ( IOException e ) {

            throw new JspTagException( "Error writing version information!" );
        }

        return SKIP_BODY;
    }

}
