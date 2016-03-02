package com.stevpet.sonar.plugins.common.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;

public class ObserverPathCache {
    private static Logger LOG = LoggerFactory.getLogger(ObserverPathCache.class);
    private Map<String,List<ParserObserver>> cache = new HashMap<>();
    private List<ParserObserver> parserObservers ;
    
    /**
     * set all observers, destructive
     * @param parserObservers
     */
    public void setParserObservers(List<ParserObserver> parserObservers) {
        this.parserObservers=parserObservers;
    }
    
    /**
     * Returns the list of parserobservers that match the path, saves the list for the next call
     * @param path
     * @return possibly empty list of parserobservers matching the path
     */
    public List<ParserObserver> getObserversMatchingPath(String path) {
        List<ParserObserver> matchingObservers = cache.get(path);
        if(matchingObservers!=null) {
            LOG.debug("Found {} {}",path,matchingObservers.size());
            return matchingObservers;
        }
        matchingObservers=new ArrayList<>();
        for(ParserObserver parserObserver: parserObservers) {
            if(parserObserver.isMatch(path)) {
                matchingObservers.add(parserObserver);
            }
        }
        LOG.debug("Added {} {}",path,matchingObservers.size());
        cache.put(path,matchingObservers);
        return matchingObservers;
    }
}
