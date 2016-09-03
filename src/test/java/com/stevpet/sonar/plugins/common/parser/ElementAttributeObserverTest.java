package com.stevpet.sonar.plugins.common.parser;

import java.util.function.Consumer;

import org.junit.Test;

import com.stevpet.sonar.plugins.common.parser.observer.ElementBodyRegistrar;

public class ElementAttributeObserverTest {

	@Test
	public void oneArgument() {
		optionalArguments(v -> {
			v.onAttribute("attribute", this::consumer);
			v.onAttribute("second", this::consumer);
		});
	}

	@Test
	public void eventArgument() {
		optionalArguments(v -> {
			//v.onEntry("attribute", this::event);
			v.onAttribute("second", this::consumer);
		});
	}

	private void optionalArguments(Consumer<ElementBodyRegistrar> attributeRegistration) {
		
	}
	
	private void consumer(String value ) {
		
	}	
	private void event() {
		
	}
	
}
