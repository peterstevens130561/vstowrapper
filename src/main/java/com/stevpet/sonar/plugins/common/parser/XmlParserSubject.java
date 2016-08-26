/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.common.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;
import com.stevpet.sonar.plugins.common.api.parser.ParserSubject;

/**
 * Each parser should implement this class, and the getHierarchy method
 * 
 * 
 */
public abstract class XmlParserSubject implements ParserSubject {

    private static final String FACTORY_CONFIGURATION_ERROR = "FactoryConfigurationError";
    private static final String COULD_NOT_CREATE_CURSOR = "Could not create cursor ";
    private static final Logger LOG = LoggerFactory
            .getLogger(XmlParserSubject.class);

    private List<String> parentElements = new ArrayList<String>();
    private int line;
    private int column;
    private final ParserData parserData;
    private RegisteredParserObservers registeredObserverClasses = new RegisteredParserObservers();
   
    private ValueObservers pathElementObservers = new DefaultValueObservers();
    private ValueObservers pathPathObservers= new DefaultValueObservers();
    private ValueObservers pathAttributeObservers= new DefaultValueObservers();
    private EventObservers pathEntryObservers = new DefaultEventObservers();
    private EventObservers pathExitObservers = new DefaultEventObservers();
    private PathSpecificationObserverRegistrationFacade observerRegistrationFacade = new PathSpecificationObserverRegistrationFacade(
                    "", pathElementObservers, pathPathObservers, pathAttributeObservers, pathEntryObservers, pathExitObservers);
    public XmlParserSubject() {
        this(new ParserData());
    }

    /**
     * mainly intended for unit testing, normally you should not be concerned about
     * the parserData
     * @param parserData
     */
    public XmlParserSubject(ParserData parserData) {
        this.parserData = parserData;
        String[] names = getHierarchy();
        for (String name : names) {
            parentElements.add(name);
        }
        //observerRegistrationFacade.setNewFacade(newFacade); //TODO: remove at end
    }
    /**
     * an array of all elements that have children.
     * @return
     */
    public abstract String[] getHierarchy();

    @SuppressWarnings("ucd")
    public void parseString(String string) {
        SMInputCursor cursor;
        try {
            cursor = getCursorFromString(string);
            parse(cursor);
        } catch (FactoryConfigurationError e) {
            LOG.error(FACTORY_CONFIGURATION_ERROR, e);
            throw new IllegalStateException(e);
        } catch (XMLStreamException e) {
            String msg = "XMLStreamException in string";
            LOG.error(msg, e);
            throw new IllegalStateException(msg, e);
        }
    }

    /**
     * Gets the cursor for the given file
     * 
     * @param file
     * @return
     * @throws FactoryConfigurationError
     * @throws XMLStreamException
     */
    private SMInputCursor getCursorFromString(String string) {
        SMInputCursor result = null;
        try {
            SMInputFactory inf = new SMInputFactory(
                    XMLInputFactory.newInstance());
            InputStream inputStream = new ByteArrayInputStream(
                    string.getBytes());
            SMHierarchicCursor cursor = inf.rootElementCursor(inputStream);
            result = cursor.advance();
        } catch (XMLStreamException e) {
            String msg = COULD_NOT_CREATE_CURSOR + e.getMessage();
            LOG.error(msg);
            throw new IllegalStateException(msg, e);
        }
        return result;
    }

    public void parseFile(File file) {
        SMInputCursor cursor = null;
        try {
            cursor = getCursor(file);
            parse(cursor);
        } catch (FactoryConfigurationError e) {
            LOG.error(FACTORY_CONFIGURATION_ERROR, e);
            throw new IllegalStateException(e);
        } catch (XMLStreamException e) {
            String msg = "XMLStreamException in " + file.getAbsolutePath()
                    + " column/line " + column + "/" + line;
            LOG.error(msg, e);
            throw new IllegalStateException(msg, e);
        } finally {
            closeStream(cursor);
        }
        registeredObserverClasses.checkOnErrors(file);
    }

    private void parse(SMInputCursor rootCursor) throws XMLStreamException {
        registeredObserverClasses.setParserData(parserData);

        SMInputCursor childCursor = rootCursor.childElementCursor();
        parseChild("", childCursor);
    }

    public void closeStream(SMInputCursor cursor) {
        try {
            if (cursor == null) {
                return;
            }
            XMLStreamReader2 reader = cursor.getStreamReader();
            if (reader == null) {
                return;
            }
            reader.closeCompletely();
        } catch (XMLStreamException e) {
            throw new IllegalStateException("exception during closing stream", e);
        }
    }

    
    public void registerObserver(ParserObserver observer) {
        registeredObserverClasses.add(observer);
        observer.registerObservers(observerRegistrationFacade);
    }

    private boolean parseChild(String path, SMInputCursor childCursor)
            throws XMLStreamException {
        boolean parsedChild = false;
        parserData.levelDown();
        while ((childCursor.getNext()) != null) {
            if (!parserData.parseLevelAndBelow()) {
                processStartElement(path, childCursor);
                parsedChild = true;
            }
        }
        parserData.levelUp();
        return parsedChild;
    }


    private void processStartElement(String path, SMInputCursor childCursor)
            throws XMLStreamException {
        String name = childCursor.getLocalName();
        if ("schema".equals(name)) {
            return;
        }
        String elementPath = createElementPath(path, name);

        onEntry(elementPath);
        processAttributes(elementPath, name, childCursor);
        processElement(elementPath, name, childCursor);
        onExit(elementPath);
    }

    /**
     * executed on entry of an element, can override to change behavior. Default
     * behavior is to execute the entry methods
     * 
     * @param path
     */
    protected void onEntry(String path) {
        pathEntryObservers.observe(path);
    }
    
    /**
     * executed on exit of an element, can override to change behavior. Default
     * behavior is to execute the exit methods
     * 
     * @param path
     */
    protected void onExit(String path) {
        pathExitObservers.observe(path);
    }

    
    private void processElement(String elementPath, String name,
            SMInputCursor childCursor) throws XMLStreamException {

        if (parentElements.contains(name)) {
            parseChild(elementPath, childCursor.childElementCursor());
        } else {
            updateLocation(childCursor);
            String text = getTrimmedElementStringValue(childCursor);
            pathElementObservers.observe(elementPath,text);
        }
    }

    private void processAttributes(String path, String name,
            SMInputCursor elementCursor) throws XMLStreamException {
        int attributeCount = elementCursor.getAttrCount();
        for (int index = 0; index < attributeCount; index++) {
            String attributeValue = elementCursor.getAttrValue(index);
            String attributeName = elementCursor.getAttrLocalName(index);
            updateLocation(elementCursor);
            pathAttributeObservers.observe(path + "/" + attributeName, attributeValue);
        }
    }

    private void updateLocation(SMInputCursor elementCursor) {
        Location location;
        try {
            location = elementCursor.getCursorLocation();

        } catch (XMLStreamException e) {
            throw new XmlParserSubjectException(
                    "Exception thrown on getting location", e);
        }
        line = location.getLineNumber();
        column = location.getColumnNumber();
    }

    private String getTrimmedElementStringValue(SMInputCursor childCursor)
            throws XMLStreamException {
        String text = childCursor.getElemStringValue();
        if (StringUtils.isNotEmpty(text)) {
            text = text.trim();
        }
        return text;
    }

    private String createElementPath(String path, String name) {
        String elementPath;
        if ("".equals(path)) {
            elementPath = name;
        } else {
            elementPath = path + "/" + name;
        }
        return elementPath;
    }

    /**
     * Gets the cursor for the given file
     * 
     * @param file
     * @return
     * @throws FactoryConfigurationError
     * @throws XMLStreamException
     */
    private SMInputCursor getCursor(File file) {
        SMInputCursor result = null;
        try {
            SMInputFactory inf = new SMInputFactory(
                    XMLInputFactory.newInstance());
            SMHierarchicCursor cursor = inf.rootElementCursor(file);
            result = cursor.advance();
        } catch (XMLStreamException e) {
            String msg = COULD_NOT_CREATE_CURSOR + e.getMessage();
            LOG.error(msg);
            throw new IllegalStateException(msg, e);
        }
        return result;
    }

}
