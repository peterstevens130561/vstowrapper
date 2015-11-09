package com.stevpet.sonar.plugins.common.parser;

import javax.xml.stream.XMLStreamException;

import org.sonar.api.utils.SonarException;

public class XmlParserSubjectException extends SonarException {

	public XmlParserSubjectException(String string, XMLStreamException e) {
		super(string,e);
	}

	public XmlParserSubjectException(String string) {
		super(string);
	}

}
