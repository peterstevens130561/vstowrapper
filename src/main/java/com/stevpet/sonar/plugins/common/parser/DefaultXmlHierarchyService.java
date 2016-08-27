package com.stevpet.sonar.plugins.common.parser;

import java.util.ArrayList;
import java.util.List;

public class DefaultXmlHierarchyService {

	private List<String> hierarchy = new ArrayList<>();

	/**
	 * builds the elements in the hierarchy
	 * @return
	 */
	public List<String> build() {
		return hierarchy;
	}

	public void add(String path) {
		for(String element : path.split("/")) {
			if(!hierarchy.contains(element)) {
				hierarchy.add(element);
			}
		}
	}

}
