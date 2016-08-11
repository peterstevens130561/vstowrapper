package com.stevpet.sonar.plugins.common.parser;

import java.util.function.Consumer;

/**
 * allows to register all observers through a fluent interface 
 * @author stevpet
 *
 */
public interface ObserverRegistrar {

    /**
     * register an observer that matches the specified element
     * 
     * @param elementObserver a void method taking the value of the element
     * @param element - the name of the element
     * @return
     */
    ObserverRegistrar onElement(ValueObserver elementObserver,String element );

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
     * @param pathObserver - a void method taking the value of the attribute
     * @param attributePath
     * @return
     */
    ObserverRegistrar onAttribute(ValueObserver pathObserver, String attributePath);
    
    /**
     * register an observer that is invoked on entry of an element
     * 
     * @param entryObserver - a void method without arguments
     * @param path - to the element
     * @return
     */
    ObserverRegistrar onEntry(EventObserver entryObserver, String path);
    
    ObserverRegistrar onExit(EventObserver exitObserver, String path);

}
