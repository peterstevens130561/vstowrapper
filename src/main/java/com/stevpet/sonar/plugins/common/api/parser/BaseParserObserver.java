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
package com.stevpet.sonar.plugins.common.api.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stevpet.sonar.plugins.common.parser.ParserData;


public abstract class BaseParserObserver implements ParserObserver {

    private Pattern pattern;
    private boolean hasError =false;
    private ParserData parserData;
    protected void setError() {
        hasError=true;
    }
    

    /**
     * parser state data to be manipulated by the observer, to be inserted by the parser.
     * @param parserData - valid parserData object
     */
    public void setParserData(ParserData parserData) {
        this.parserData = parserData;
    }
    
    public void setSkipTillNextElement(String elementName) {
        parserData.setSkipThisLevel();
    }
    public boolean hasError() {
        return hasError;
    }
    public final boolean isMatch(String path) {
        if(pattern==null) {
            throw new IllegalArgumentException("No patterns defined (did you use setPattern in the creation of your observer?) ");
        }
        Matcher matcher = pattern.matcher(path);
        return matcher.matches();      
    }

    /**
     * Pattern to use when matching
     * @param regex to use. observeElement and observAttribute will be invoked for paths matching the regex
     */
    protected void setPattern(String regex) {
        pattern = Pattern.compile(regex);
    }
    

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserObserver#observeElement(java.lang.String, java.lang.String)
     */
    public void observeElement(String name, String text) {
    }


    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserObserver#observeAttribute(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void observeAttribute(String elementName, String path,
            String attributeValue, String attributeName) {
        // to be overridden when needed
    }
}
