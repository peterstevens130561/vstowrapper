package com.stevpet.sonar.plugins.common.parser;

import java.util.function.Consumer;

public interface ObserverRegistry {

    ObserverRegistry onElement(Consumer<String> elementObserver,String string );

    ObserverRegistry onPath(ValueObserver pathObserver, String string);
    ObserverRegistry onAttribute(Consumer<String> pathObserver, String string);
    
    ObserverRegistry onEntry(Consumer<Void> pathObserver, String string);
    ObserverRegistry onExit(Consumer<Void> pathObserver, String string);

    void invokePathObserver(String path, String value);
}
