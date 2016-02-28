package com.stevpet.sonar.plugins.common.parser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;

public class ParserObserverMethods{
    private final ParserObserver observer;
    private final List<ParserObserverMethod> methods = new ArrayList<ParserObserverMethod>();

    public ParserObserverMethods(ParserObserver observer) {
        this.observer=observer;
        for(Method method: observer.getClass().getDeclaredMethods()) {
            methods.add(new ParserObserverMethod(method));
        }
        
    }
    public boolean hasError() {
        // TODO Auto-generated method stub
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
    

}
