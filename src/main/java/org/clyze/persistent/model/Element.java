package org.clyze.persistent.model;

import java.util.Map;
import java.util.Objects;

/**
 * A persistent element of an analysis.
 */
public abstract class Element extends ItemImpl {	

	/**
	 * The root element id -- the id of the "root element" this element belong to.
	 * Either the analysis or the bundle, depending on the type of this element.	 
	 */
	private String rootElemId;

	public Element() {}		

	public String getRootElemId() {
		return rootElemId;
	}

	public void setRootElemId(String rootElemId) {
		this.rootElemId = rootElemId;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof Element)) return false;
		Element element = (Element) object;

		return Objects.equals(rootElemId, element.rootElemId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(rootElemId);
	}

	@Override
	protected void saveTo(Map<String, Object> map) {		
		//We don't serialize the id
		map.put("rootElemId", rootElemId);
	}

	@Override
	public void fromMap(Map<String, Object> map){
		this.rootElemId = (String) map.get("rootElemId");
	}
}
