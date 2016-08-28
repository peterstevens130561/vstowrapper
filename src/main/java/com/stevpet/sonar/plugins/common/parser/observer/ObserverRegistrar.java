package com.stevpet.sonar.plugins.common.parser.observer;

import java.util.function.Consumer;

/**
 * allows to register all observers through a fluent interface 
 * following convention is used
 * 
 *- method starting with
 *<p>
 *<li>
 *on: use to register an observer. 
 *<li>
 *  	First argument is a string to select a name or attribute.
 *  	Second argument is a lambda to register the observer
 *  - in: selects a path or element
 * @author stevpet
 *
 */
public interface ObserverRegistrar {

    /**
     * register an observer that matches the specified element
     * @param element - the name of the element
     * @param elementObserver a void method taking the value of the element
     * 
     * @return
     */
    ObserverRegistrar onElement(String element,ValueObserver elementObserver );

    /**
     * register an observer that matches a full path, matches on an element
     * 
     * @param pathObserver - a void method taking the value of the element
     * @param path
     * @return
     */
    ObserverRegistrar onPath(ValueObserver pathObserver, String path);
    
    /**
     * register an observer that matches the full path to an attribute
     * @param attributePath
     * @param pathObserver - a void method taking the value of the attribute
     * @return
     */
    ObserverRegistrar onAttribute(String attributePath, ValueObserver pathObserver);
    
    /**
     * register an observer that is invoked on entry of an element
     * @param element - to the element
     * @param entryObserver - a void method without arguments
     * 
     * @return
     */
    ObserverRegistrar onEntry(String element, EventObserver entryObserver);
    
    /**
     * register an observer that is invoked on exit of an element
     * @param element - to the element
     * @param entryObserver - a void method without arguments
     * @return
     */
    ObserverRegistrar onExit(String element, EventObserver exitObserver);

    /**
     * register an observer on the current paths entry
     */
    ObserverRegistrar onEntry(EventObserver entryObserver) ;
    
    /**
     * register an observer on the current paths exit
     */
    ObserverRegistrar onExit(EventObserver exitObserver) ;
    
    /**
     * 
     * @param name - the name of the element on which we want to observe attributes
     * @param registrar - registration of attribute observers
     *
     *<br>
     * {@code inElement("SomeElement",registrar -> registrar->addAttribute("vc",this::vc).addAttribute("sl",this::sl})
     */
    ObserverRegistrar inElement(String name, Consumer<AttributeRegistrar> registrar);

    /**
     * select the path for the observers
     * @param path - starting path
     * @param registrar observers
     * @return
     */
    ObserverRegistrar inPath(String path, Consumer<ObserverRegistrar> registrar);

    void setName(String name);

    /**
     * Select the specified path, followed by the observers. Does not allow nesting, but provides often for a cleaner
     * look
     * @param path 
     * @return
     */
	ObserverRegistrar inPath(String path);

	AttributeRegistrar inElement(String string);

}
