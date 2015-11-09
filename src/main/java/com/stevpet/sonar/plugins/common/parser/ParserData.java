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

public class ParserData {
    private int skipLevel = 0;
    private int level=0;

    /**
     * go next level down in the xml tree
     */
    void levelDown() {
        ++level;
    }
    
    /**
     * go level up in the xml tree
     */
    void levelUp() {
        --level;
    }
    
    public void setSkipThisLevel() {
        this.skipLevel=level;
    }
    

    boolean parseLevelAndBelow()  {
        boolean skip=skipLevel >0 && level >= skipLevel;
        if(!skip) {
            skipLevel=0;
        }
        return skip;
    }

    Object getLevel() {
        return level;
    }


}
