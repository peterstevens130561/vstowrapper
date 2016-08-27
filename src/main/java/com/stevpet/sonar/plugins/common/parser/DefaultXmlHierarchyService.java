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
		return new ArrayList<String>();
	}

	public void add(String path) {
		hierarchy.add(path);
	}

}
