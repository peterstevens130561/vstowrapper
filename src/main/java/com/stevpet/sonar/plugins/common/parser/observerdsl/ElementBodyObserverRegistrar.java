package com.stevpet.sonar.plugins.common.parser.observerdsl;

import java.util.function.Consumer;

import com.stevpet.sonar.plugins.common.parser.observer.EventObserver;
import com.stevpet.sonar.plugins.common.parser.observer.ParserEventArgs;

/**
 * register observers on the element through fluent interface
 * <p>
 * {@code
 * inElement("Class", v-v.onEntry(this:entry).onAttribute("uid",this::uid).onValue(this.classValue).onExit(this::exitClass);}
 *
 */
public interface ElementBodyObserverRegistrar {

	/**
	 * Register an observer which will read the value of an attribute
	 * @param attribute
	 * @param observer
	 * @return this
	 */
	ElementBodyObserverRegistrar onAttribute(String attribute, Consumer<String> observer);

	/**
	 * register an observer which will be triggered on entry of the element
	 * @param observer
	 * @return this
	 */
	ElementBodyObserverRegistrar onEntry(EventObserver observer);


	/**
	 * register an observer which will be triggered on exit of the element
	 * @param observer
	 * @return
	 */
	ElementBodyObserverRegistrar onExit(EventObserver observer);

	/**
	 * register an observer which will read the value of the element
	 * @param observer
	 * @return
	 */
	ElementBodyObserverRegistrar onValue(Consumer<String> observer);

	/**
	 * register an observer which will get the eventArgs as argument
	 * @param observer
	 */
	ElementBodyObserverRegistrar withEventArgs(Consumer<ParserEventArgs> observer);

}