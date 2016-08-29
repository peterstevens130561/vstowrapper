package com.stevpet.sonar.plugins.common.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.observer.ObserverRegistrar;

public class ParserHierarchyTests {

	private XmlParser parser;
	private File File;
	private String basic = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Main><Summary></Summary><Child><GrandChild><GrandChildPoint>BLA</GrandChildPoint></GrandChild><Point>POINT1</Point><Point>POINT2</Point></Child></Main>";
	private int grandChildEntered;
	private int grandChildExited;
	private int childExited;
	private int childEntered;
	private String point;

	@Before
	public void before()
 {
		}
	
	/**
	 * parse a file with some elements and hierarchy, should just work
	 */
	@Test
	public void parseBasic() {
		parser = new DefaultXmlParser() {		
		};
		parser.registerObserver(new UninterestedObserver());
		parser.parseString(basic);
		assertEquals(1,grandChildEntered);
		assertEquals(1,grandChildExited);
		assertEquals(1,childEntered);
		assertEquals(1,childExited);
		assertNull(point);
	}
	
	@Test
	public void parseWithTop() {
		parser = new DefaultXmlParser() {		
		};
		parser.registerObserver(new Observer());
		parser.parseString(basic);
		assertEquals(1,grandChildEntered);
		assertEquals(1,grandChildExited);
		assertEquals(1,childEntered);
		assertEquals(1,childExited);
		assertEquals("BLA",point);
	}
	
	public void grandChildEntered() {
		++grandChildEntered;
	}
	
	
	class UninterestedObserver extends BaseParserObserver {
		@Override
		public void registerObservers(ObserverRegistrar registrar) {
				registrar.inPath("Child/GrandChild")
				.onExit(()-> {grandChildExited+=1;})
				.onEntry(()->{grandChildEntered++;});
				registrar.inPath("Child")
				.onExit(() -> {childExited++;})
				.onEntry(() -> {childEntered++;});
			}		
	}
	class Observer extends BaseParserObserver {



		@Override
		public void registerObservers(ObserverRegistrar registrar) {
				registrar.inPath("Child/GrandChild")
				.onExit(()-> {grandChildExited+=1;})
				.onEntry(()->{grandChildEntered++;});
				registrar.inPath("Child")
				.onExit(() -> {childExited++;})
				.onEntry(() -> {childEntered++;});
				registrar.inPath("Child/GrandChild").onElement("GrandChildPoint",value -> point = value  );
			}
	}
	

 }
