package com.stevpet.sonar.plugins.common.parser.hierarchybuilder;

import java.util.List;
/**
 * builds the hierarchy of an xml document
 * @author stevpet
 *
 */
public interface XmlHierarchyBuilder {

	/**
	 * returns the hierarchy, which is a list of elements, in no specific order
	 * i.e. if elements a/b/c and a/b/d then the list may be a,b,c,d or a,b,d,c
	 * @return
	 */
	List<String> build();

	/**
	 * add a path to the hierarchy, i.e. a/b. The last element in the list must have children.
	 * @param path
	 */
	void add(String path);

}