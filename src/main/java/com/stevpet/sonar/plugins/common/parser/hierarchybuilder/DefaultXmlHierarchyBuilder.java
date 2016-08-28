package com.stevpet.sonar.plugins.common.parser.hierarchybuilder;

import java.util.ArrayList;
import java.util.List;

public class DefaultXmlHierarchyBuilder implements XmlHierarchyBuilder {

	private List<String> hierarchy = new ArrayList<>();

	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.XmlHierarchyService#build()
	 */
	@Override
	public List<String> build() {
		return hierarchy;
	}

	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.XmlHierarchyService#add(java.lang.String)
	 */
	@Override
	public void add(String path) {
		for(String element : path.split("/")) {
			if(!hierarchy.contains(element)) {
				hierarchy.add(element);
			}
		}
	}


}
