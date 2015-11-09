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

import com.stevpet.sonar.plugins.common.parser.ParserData;



public interface ParserObserver {
    /**
     * Determine whether the current element with text is to be observed
     * @param path to current element
     * @return true if the path matches the element to observe
     */
    boolean isMatch(String path);
    /**
     * Invoked when element is found of which the localName matches the pattern
     * @param name of this element
     * @param text of this element
     */
    void observeElement(String name,String text);
    void observeAttribute(String elementName, String path,
            String attributeValue, String attributeName);
    
    
    /**
     * true if one or more errors have been found during parsing. The observer
     * is responsible for logging;
     */
    boolean hasError();
    void setParserData(ParserData parserData);
}
