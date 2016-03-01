package com.stevpet.sonar.plugins.common.parser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementObserver.Event;

/**
 * The methods for one class
 * @author stevpet
 *
 */
public class ParserObserverMethods{
    private final ParserObserver observer;
    private ObserverMethodCache observerMethodCache = new ObserverMethodCache();

    public ParserObserverMethods(ParserObserver observer) {
        this.observer=observer;
        observerMethodCache.addObserver(observer);
    }
    public boolean hasError() {
        return observer.hasError();
    }
    public void setParserData(ParserData parserData) {
        observer.setParserData(parserData);
    }
    public boolean isMatch(String path) {
        return observer.isMatch(path);
    }
    public void observeElement(String name, String text) {
        observer.observeElement(name, text);
    }
    public void observeAttribute(String elementName, String path, String attributeValue, String attributeName) {
        observer.observeAttribute(elementName, path, attributeValue, attributeName);
    }
    
    public ParserObserver getParserObserver() {
        return observer;
    }
    
    public Method getMatchingElementMethod(String elementPath, String elementName) {
        return  observerMethodCache.getMatchingElementMethod(elementPath, elementName);
    }
    public Method getMatchingElementObserverMethod(String path, Event event) {
        return observerMethodCache.getMatchingElementObserverMethod(path,event);
    }
    public Method getMatchingAttributeMethod(String elementName, String attributeName) {
        return observerMethodCache.getMatchingAttributeMethod(elementName, attributeName);
    }

}
