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
package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Closeables;
import org.sonar.api.utils.SonarException;

import javax.annotation.Nullable;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

public class VisualStudioProjectParser {

  public SimpleVisualStudioProject parse(File file) {
    return new Parser().parse(file);
  }

  private static class Parser {

    private static final Set<String> PROJECT_ITEM_TYPES = ImmutableSet.of("Compile", "Content", "EmbeddedResource", "None", "ClCompile", "Page");

    private File file;
    private XMLStreamReader stream;
    private final ImmutableList.Builder<String> filesBuilder = ImmutableList.builder();
    private String projectTypeGuids;
    private String outputType;
    private String assemblyName;
    private String currentCondition;
    private final ImmutableList.Builder<String> propertyGroupConditionsBuilder = ImmutableList.builder();
    private final ImmutableList.Builder<String> outputPathsBuilder = ImmutableList.builder();

    public SimpleVisualStudioProject parse(File projectFile) {
      this.file = projectFile;

      InputStreamReader reader = null;
      XMLInputFactory xmlFactory = XMLInputFactory.newInstance();

      try {
        reader = new InputStreamReader(new FileInputStream(projectFile), Charsets.UTF_8);
        stream = xmlFactory.createXMLStreamReader(reader);

        boolean inItemGroup = false;
        int inItemGroupNestingLevel = 0;

        while (stream.hasNext()) {
          int next = stream.next();
          if (next == XMLStreamConstants.START_ELEMENT) {
            String tagName = stream.getLocalName();

            if (inItemGroup && inItemGroupNestingLevel == 0 && PROJECT_ITEM_TYPES.contains(tagName)) {
              handleProjectItemTag();
            } else if ("ProjectTypeGuids".equals(tagName)) {
              handleProjectTypeGuids();
            } else if ("OutputType".equals(tagName)) {
              handleOutputTypeTag();
            } else if ("AssemblyName".equals(tagName)) {
              handleAssemblyNameTag();
            } else if ("PropertyGroup".equals(tagName)) {
              handlePropertyGroupTag();
            } else if ("OutputPath".equals(tagName)) {
              handleOutputPathTag();
            }

            if ("ItemGroup".equals(tagName)) {
              inItemGroup = true;
              inItemGroupNestingLevel = 0;
            } else if (inItemGroup) {
              inItemGroupNestingLevel++;
            }
          } else if (next == XMLStreamConstants.END_ELEMENT) {
            String tagName = stream.getLocalName();

            if ("ItemGroup".equals(tagName)) {
              inItemGroup = false;
            } else if (inItemGroup) {
              inItemGroupNestingLevel--;
            }
          }
        }
      } catch (IOException e) {
        throw Throwables.propagate(e);
      } catch (XMLStreamException e) {
        throw new SonarException("Error while parsing the Visual Studio project file: " + projectFile.getAbsolutePath(), e);
      } finally {
        closeXmlStream();
        Closeables.closeQuietly(reader);
      }

      return new SimpleVisualStudioProject(projectFile,filesBuilder.build(),  outputType, assemblyName, outputPathsBuilder.build());
    }

    private void closeXmlStream() {
      if (stream != null) {
        try {
          stream.close();
        } catch (XMLStreamException e) {
          throw Throwables.propagate(e);
        }
      }
    }

    private void handleProjectItemTag() {
      String include = getRequiredAttribute("Include");
      filesBuilder.add(include);
    }

    private void handleProjectTypeGuids() throws XMLStreamException {
      projectTypeGuids = stream.getElementText();
    }

    private void handleOutputTypeTag() throws XMLStreamException {
      outputType = stream.getElementText();
    }

    private void handleAssemblyNameTag() throws XMLStreamException {
      assemblyName = stream.getElementText();
    }

    private void handlePropertyGroupTag() throws XMLStreamException {
      currentCondition = Strings.nullToEmpty(getAttribute("Condition"));
    }

    private void handleOutputPathTag() throws XMLStreamException {
      propertyGroupConditionsBuilder.add(currentCondition);
      outputPathsBuilder.add(stream.getElementText());
    }

    private String getRequiredAttribute(String name) {
      String value = getAttribute(name);
      if (value == null) {
        throw parseError("Missing attribute \"" + name + "\" in element <" + stream.getLocalName() + ">");
      }

      return value;
    }

    @Nullable
    private String getAttribute(String name) {
      for (int i = 0; i < stream.getAttributeCount(); i++) {
        if (name.equals(stream.getAttributeLocalName(i))) {
          return stream.getAttributeValue(i);
        }
      }

      return null;
    }

    private ParseErrorException parseError(String message) {
      return new ParseErrorException(message + " in " + file.getAbsolutePath() + " at line " + stream.getLocation().getLineNumber());
    }

  }

  private static class ParseErrorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ParseErrorException(String message) {
      super(message);
    }

  }

}
