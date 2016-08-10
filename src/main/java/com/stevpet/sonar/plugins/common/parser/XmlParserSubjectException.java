package com.stevpet.sonar.plugins.common.parser;

import javax.xml.stream.XMLStreamException;


public class XmlParserSubjectException extends IllegalStateException {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    public XmlParserSubjectException(String string, XMLStreamException e) {
		super(string,e);
	}

	public XmlParserSubjectException(String string) {
		super(string);
	}

}
