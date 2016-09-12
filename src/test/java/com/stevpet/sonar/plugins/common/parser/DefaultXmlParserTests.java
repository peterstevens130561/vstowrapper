package com.stevpet.sonar.plugins.common.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;
import com.stevpet.sonar.plugins.common.parser.observerdsl.TopLevelObserverRegistrar;

public class DefaultXmlParserTests {

	private DefaultXmlParser parser = new DefaultXmlParser();
	private Object xmlOnError;
	private String value;
	
	@Before
	public void before() {
		
	}
	
	@Test
	public void errorCatch() {
		String basic = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Main><fun><Summary>booh</Summary></fun></Main>";
		try {
			BaseParserObserver observer = new BaseParserObserver() {

				@Override
				public void registerObservers(TopLevelObserverRegistrar registrar) {
					// TODO Auto-generated method stub
					registrar.inPath("fun").inElement("Summary",i -> i.withEventArgs(o -> {o.setError();}));
				}
				
			};
			parser.registerObserver(observer);
			parser.parseString(basic);
		} catch (ParserSubjectErrorException e) {
			return;
		}
		fail("should have gotten exception");
	}
	
	@Test
	public void checkValue() {
		String basic = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Main><fun><Summary>booh</Summary></fun></Main>";
			ParserObserver observer = new ParserObserver() {

				@Override
				public void registerObservers(TopLevelObserverRegistrar registrar) {
					registrar.inPath("fun").inElement("Summary",i -> i.withEventArgs(o -> {value=o.getValue();}));
				}
				
			};
			parser.registerObserver(observer);
			parser.parseString(basic);
			assertEquals("booh",value);
	}
}

