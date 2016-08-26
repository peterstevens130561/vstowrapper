package com.stevpet.sonar.plugins.common.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;

public class RegisteredParserObservers {
    private Map<String,List<ParserObserverMethods>> cache = new HashMap<>();
    private List<ParserObserverMethods> parserObservers =new ArrayList<>();
    
    /**
     * Add a parser observer
     * @param observer
     */
    public void add(ParserObserver observer) {
        ParserObserverMethods parserObserverMethods = new ParserObserverMethods(observer);
        parserObservers.add(parserObserverMethods);
    }

    public void setParserData(ParserData parserData) {
        for (ParserObserverMethods observer : parserObservers) {
            observer.setParserData(parserData);
        } 
    }

    public void checkOnErrors(File file) {
        for (ParserObserverMethods observer : parserObservers) {
            if (observer.hasError()) {
                throw new ParserSubjectErrorException(file);
            }
        }
    }
}
