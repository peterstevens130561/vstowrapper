package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Settings;

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

    private Settings settings;
    
    public CppProjectParser(Settings settings) {
        this.settings=settings;
    }
	@Override
	public SimpleVisualStudioProject parse(File file) {
	    parseFile(file);
	    project=new SimpleVisualStudioProject();
	    project.setProjectFile(file).setOutputType("library");
		String assemblyName = createAssemblyName();
		project.setAssemblyName(assemblyName);
		
		project.setLanguage("cpp");
		return project;
	}

    private String createAssemblyName() {
        String assemblyName;
		if(StringUtils.isEmpty(targetName)) {
		    assemblyName=projectName;
		} else {
		    String evaluatedName = targetName.replaceFirst("\\$\\(ProjectName\\)", projectName);
		    assemblyName=evaluatedName;
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
                      boolean useName=true;
                      int attributes=stream.getAttributeCount();
                      if(attributes==1) {
                          String name=stream.getAttributeName(0).getLocalPart();
                          String value=stream.getAttributeValue(0);
                          if("Condition".equals(name)) {
                              useName=evalCondition(value);
                          }
                      }
                      if(useName) {
                          targetName = stream.getElementText();
                      }
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
              throw new IllegalStateException("Error while parsing the Visual Studio project file: " + projectFile.getAbsolutePath(), e);
            } finally {
              closeXmlStream();
              Closeables.closeQuietly(reader);
            }
    }
    private boolean evalCondition(String value) {
        value=replaceProperty(value,"Configuration", VisualStudioPlugin.BUILD_CONFIGURATION_KEY,VisualStudioPlugin.BUILD_CONFIGURATIONS_DEFVALUE);
        value=replaceProperty(value,"Platform",VisualStudioPlugin.BUILD_PLATFORM_KEY,VisualStudioPlugin.BUILD_PLATFORM_DEFVALUE);
        String parts[]=value.split("==");
        if(parts.length!=2) { 
            throw new VsToWrapperException("unsupported condition" + value);
        }
        return parts[0].equalsIgnoreCase(parts[1]);
    }

    private String replaceProperty(String original,String variable,String key,String defaultValue) {
        String property=settings.getString(key);
        if(StringUtils.isEmpty(property)) {
            property=defaultValue;
        }
        String result=original.replaceAll("\\$\\(" + variable + "\\)", property);
        return result;
        
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
