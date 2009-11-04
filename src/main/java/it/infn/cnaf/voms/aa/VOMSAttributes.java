package it.infn.cnaf.voms.aa;

import java.util.List;

/**
 * 
 * @author Andrea Ceccanti
 *
 */
public interface VOMSAttributes {

	public abstract List<VOMSFQAN> getFqans();

	public abstract List<VOMSGenericAttribute> getGenericAttributes();

	public abstract VOMSUser getUser();

}