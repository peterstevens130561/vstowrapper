package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.SonarException;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Closeables;

public class CppProjectParser implements VisualStudioProjectParser {
    private static final Set<String> PROJECT_ITEM_TYPES = ImmutableSet.of("Compile", "Content", "EmbeddedResource", "None", "ClCompile", "Page");

	SimpleVisualStudioProject project;
    private String projectName;

    private String targetName;

    private XMLStreamReader stream;
	@Override
	public SimpleVisualStudioProject parse(File file) {
	    parseFile(file);
		File projectFile = file;
		List<String> files= new ArrayList<String>();
		String outputType = "assembly"; 
		String assemblyName = createAssemblyName();
		List<String> outputPaths = new ArrayList<String>();
		project = new SimpleVisualStudioProject(projectFile, files, outputType, assemblyName, outputPaths);
		project.setLanguage("cpp");
		return project;
	}

    private String createAssemblyName() {
        String assemblyName;
		if(StringUtils.isEmpty(targetName)) {
		    assemblyName=projectName + ".dll";
		} else {
		    String evaluatedName = targetName.replaceFirst("\\$\\(ProjectName\\)", projectName);
		    assemblyName=evaluatedName + ".dll";
		}
        return assemblyName;
    }
	
    @Override
    public void setName(String projectName) {
        this.projectName = projectName;
    }
    
    
    private void parseFile(File projectFile) {
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
                  } else if ("TargetName".equals(tagName)) {
                      targetName = stream.getElementText();
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
        // TODO Auto-generated method stub
        
    }

}
