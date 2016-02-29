package com.stevpet.sonar.plugins.common.parser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.PathMatcher;

/**
 * The annotations for one method
 * @author stevpet
 *
 */
public class ParserObserverMethod {
    List<AnnotatedMethodObserver> observerMethods = new ArrayList<>();
    private Method selectedObserver;
    public ParserObserverMethod(Method method) {
        observerMethods.add(AttributeMatcherMethodObserver.create(method));
        observerMethods.add(PathMatcherMethodObserver.create(method));
        observerMethods.add(ElementMatcherObserver.create(method));
    }

    public boolean shouldObserve(String key, String value) {
        for(AnnotatedMethodObserver methodObserver : observerMethods) {
            if(methodObserver.shouldObserve(key, value)) {
                selectedObserver=methodObserver.getMethod();
                return true;
            }
        }
        return false;
    }
    
    public Method getObserver() {
        Preconditions.checkState(selectedObserver !=null, "no observer to get");
        return selectedObserver;
    }
}
