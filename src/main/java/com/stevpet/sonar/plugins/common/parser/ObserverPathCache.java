package com.stevpet.sonar.plugins.common.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;

public class ObserverPathCache {
    private static Logger LOG = LoggerFactory.getLogger(ObserverPathCache.class);
    private Map<String,List<ParserObserverMethods>> cache = new HashMap<>();
    private List<ParserObserverMethods> parserObservers =new ArrayList<>();
    
    /**
     * set all observers, destructive
     * @param parserObservers
     */
    public void setParserObservers(List<ParserObserverMethods> parserObservers) {
        this.parserObservers=parserObservers;
    }
    
    /**
     * Returns the list of parserobservers that match the path, saves the list for the next call
     * @param path
     * @return possibly empty list of parserobservers matching the path
     */
    public List<ParserObserverMethods> getObserversMatchingPath(String path) {
        List<ParserObserverMethods> matchingObservers = cache.get(path);
        if(matchingObservers!=null) {
            return matchingObservers;
        }
        matchingObservers=new ArrayList<>();
        for(ParserObserverMethods parserObserver: parserObservers) {
            if(parserObserver.isMatch(path)) {
                matchingObservers.add(parserObserver);
            }
        }
        cache.put(path,matchingObservers);
        return matchingObservers;
    }

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
