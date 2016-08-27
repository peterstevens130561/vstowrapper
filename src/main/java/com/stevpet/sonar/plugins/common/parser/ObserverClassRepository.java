package com.stevpet.sonar.plugins.common.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;

public class ObserverClassRepository {
    private List<ParserObserver> parserObservers =new ArrayList<>();
    
    /**
     * Add a parser observer
     * @param observer
     */
    public void add(ParserObserver observer) {
        parserObservers.add(observer);
    }

    public void setParserData(ParserData parserData) {
        for (ParserObserver observer : parserObservers) {
            observer.setParserData(parserData);
        } 
    }

    public void checkOnErrors(File file) {
        for (ParserObserver observer : parserObservers) {
            if (observer.hasError()) {
                throw new ParserSubjectErrorException(file);
            }
        }
    }
}
