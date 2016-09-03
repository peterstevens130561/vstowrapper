package com.stevpet.sonar.plugins.common.parser.observer;

import com.stevpet.sonar.plugins.common.parser.ParserData;

public class DefaultParserEventArgs implements ParserEventArgs {

	private String path;
	private String value;
	private int line;
	private int column;
	private ParserData parserData;

	public DefaultParserEventArgs(String path, String value, int line, int column, ParserData parserData) {
		this.path = path;
		this.value=value;
		this.line=line;
		this.column=column;
		this.parserData=parserData;
	}
	
	public DefaultParserEventArgs() {

	}
	
	public DefaultParserEventArgs(String path, String value) {
		this.path=path;
		this.value=value;
	}

	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ParserEventArgs#getValue()
	 */
	@Override
	public String getValue() {
		return value;
	}


	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ParserEventArgs#getPath()
	 */
	@Override
	public String getPath() {
		return path;
	}
	public int getLine() {

		return line;
	}


	public int getColumn() {

		return column;
	}


	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ParserEventArgs#setError()
	 */
	@Override
	public void setError() {

	}


	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ParserEventArgs#setSkipTillNextElement()
	 */
	@Override
	public void setSkipTillNextElement() {
		
	}
	public ParserEventArgs setValue(String value) {
		this.value=value;
		return this;
	}
	public void setPath(String path) {
		this.path=path;
	}
	public ParserEventArgs clearValue() {
		value=null;
		return this;
	}
	public ParserEventArgs setLine(int line) {
		this.line-=line;
		return this;
	}
	public ParserEventArgs setColumn(int column) {
		this.column=column;
		return this;
	}

}
