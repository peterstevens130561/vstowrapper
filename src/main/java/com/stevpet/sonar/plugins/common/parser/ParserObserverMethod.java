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
    private Method selectedObserver;
    private AnnotatedMethodObserver attributeObserver;
    private AnnotatedMethodObserver pathObserver;
    private AnnotatedMethodObserver elementObserver;
    public ParserObserverMethod(Method method) {
        attributeObserver=AttributeMatcherMethodObserver.create(method);
        pathObserver=PathMatcherMethodObserver.create(method);
        elementObserver=ElementMatcherObserver.create(method);
    }

    
    public Method getObserver() {
        Preconditions.checkState(selectedObserver !=null, "no observer to get");
        return selectedObserver;
    }

    public boolean shouldObserve(String path, String elementName) {
        return shouldObservePath(path) || shouldObserveElement(elementName);
    }
    
    public boolean shouldObservePath(String path) {
        boolean result=pathObserver !=null && path.equals(pathObserver.shouldObserve(path,null));
        selectedObserver=result?pathObserver.getMethod():null;
        return result;
    }


    public boolean shouldObserveElement(String elementName) {
        boolean result=elementObserver!=null && elementName.equals(elementObserver.shouldObserve(elementName, null));
        selectedObserver=result?elementObserver.getMethod():null;
        return result;
    }



}
