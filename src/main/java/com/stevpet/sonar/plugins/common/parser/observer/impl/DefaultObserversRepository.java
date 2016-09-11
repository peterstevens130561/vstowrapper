package com.stevpet.sonar.plugins.common.parser.observer.impl;

import java.util.List;
import java.util.function.Consumer;

import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.DefaultXmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.XmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.observer.ArgumentObservers;
import com.stevpet.sonar.plugins.common.parser.observer.EventObserver;
import com.stevpet.sonar.plugins.common.parser.observer.EventObservers;
import com.stevpet.sonar.plugins.common.parser.observer.ObserversRepository;
import com.stevpet.sonar.plugins.common.parser.observer.ParserEventArgs;
import com.stevpet.sonar.plugins.common.parser.observerdsl.DefaultElementBodyRegistrar;

public class DefaultObserversRepository implements ObserversRepository{
    private ArgumentObservers<String> elementObservers = new DefaultArgumentObservers<>();
    private ArgumentObservers<ParserEventArgs> elementEventArgsObservers = new DefaultArgumentObservers<>();
    private ArgumentObservers<String> attributeObservers= new DefaultArgumentObservers<>();
    private EventObservers entryObservers = new DefaultEventObservers();
    private EventObservers exitObservers = new DefaultEventObservers();
	private XmlHierarchyBuilder hierarchyBuilder = new DefaultXmlHierarchyBuilder();
	
	@Override
	public void registerElementObserver(String path, Consumer<String> observer) {
		elementObservers.register(path, observer);
		
	}
	
	@Override
	public void registerEntryObserver(String path, EventObserver observer) {
		entryObservers.register(path, observer);
	}
	
	@Override
	public void registerExitObserver(String path, EventObserver observer) {
		exitObservers.register(path, observer);
	}
	
	@Override
	public void registerAttributeObservers(String path, Consumer<DefaultElementBodyRegistrar> attributeRegistration) {
        DefaultElementBodyRegistrar t = new DefaultElementBodyRegistrar(path,this);
        attributeRegistration.accept(t);
	}
	
	@Override
	public void observeEntry(String path) {
        entryObservers.observe(path);
	}
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ObserversFacade#observeExit(java.lang.String)
	 */
	@Override
	public void observeExit(String path) {
		exitObservers.observe(path);
	}
	public boolean hasElementMatch(String elementPath) {
		return elementObservers.hasMatch(elementPath) ;
	}
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ObserversFacade#observeElement(java.lang.String, java.lang.String)
	 */
	@Override
	public void observeElement(String elementPath, String text) {
		elementObservers.observe(elementPath,text);
	}
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ObserversFacade#observeAttribute(java.lang.String, java.lang.String)
	 */
	@Override
	public void observeAttribute(String path, String attributeValue) {
		attributeObservers.observe(path, attributeValue);
	}
	
	@Override
	public void addPath(String parent) {
        hierarchyBuilder.add(parent);
	}
	
	@Override
	public List<String> buildHierarchy() {
		return hierarchyBuilder.build();
	}

	@Override
	public void registerAttributeObserver(String path, Consumer<String> observer) {
		attributeObservers.register(path, observer);
	}

	@Override
	public void registerElementEventArgsObserver(String path, Consumer<ParserEventArgs> observer) {
		elementEventArgsObservers.register(path, observer);
	}

	@Override
	public void observeElement(String path, ParserEventArgs eventArgs) {
		elementEventArgsObservers.observe(path, eventArgs);
	}
	
}
