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
     * @param attributePath
     * @param pathObserver - a void method taking the value of the attribute
     * @return
     * @deprecated
     * replaced by inElement, as it is much more concise
     */
    @Deprecated
    ObserverRegistrar onAttribute(String attributePath, ValueObserver pathObserver);
    
    /**
     * register an observer that is invoked on entry of an element
     * 
     * @param entryObserver - a void method without arguments
     * @param path - to the element
     * @return
     */
    ObserverRegistrar onEntry(EventObserver entryObserver, String path);
    
    ObserverRegistrar onExit(EventObserver exitObserver, String path);

    /**
     * 
     * @param name - the name of the element on which we want to observe attributes
     * @param registrar - registration of attribute observers
     *
     *<br>
     * {@code inElement("SomeElement",registrar -> registrar->addAttribute("vc",this::vc).addAttribute("sl",this::sl})
     */
    void inElement(String name, Consumer<AttributeRegistrar> registrar);

}
