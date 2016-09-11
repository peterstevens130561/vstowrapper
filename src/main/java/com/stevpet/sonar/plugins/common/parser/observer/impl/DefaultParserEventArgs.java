package com.stevpet.sonar.plugins.common.parser.observer.impl;

import com.stevpet.sonar.plugins.common.parser.ParserData;
import com.stevpet.sonar.plugins.common.parser.observer.ParserEventArgs;

public class DefaultParserEventArgs implements ParserEventArgs {

	private String value;
	private boolean error;


	public DefaultParserEventArgs() {

	}
	

	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ParserEventArgs#getValue()
	 */
	@Override
	public String getValue() {
		return value;
	}

	public ParserEventArgs setValue(String value) {
		this.value=value;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ParserEventArgs#setError()
	 */
	@Override
	public void setError() {
		error=true;
	}



	public boolean isError() {
		return error;
	}

}
