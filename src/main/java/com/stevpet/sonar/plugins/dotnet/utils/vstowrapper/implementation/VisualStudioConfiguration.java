package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.util.ArrayList;
import java.util.Collection;

import org.sonar.api.BatchExtension;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Qualifiers;

public class VisualStudioConfiguration implements BatchExtension {
    private final Settings settings ;
    public VisualStudioConfiguration(Settings settings) {
        this.settings = settings ;
    }
    
    public static Collection<PropertyDefinition> getProperties() {
        Collection<PropertyDefinition> properties = new ArrayList<>();
        properties.add(createProperty("rabarber.com", PropertyType.STRING)
            .name("name")
            .index(1)
            .onQualifiers(Qualifiers.PROJECT)
            .description("list of directories where artifacts are to be found after building")
            .build());
        properties.add(createProperty(VisualStudioPlugin.VISUAL_STUDIO_OUTPUT_PATHS_PROPERTY_KEY, PropertyType.STRING)
                .name("outputPaths")
                .index(0)
                .onQualifiers(Qualifiers.PROJECT)
                .description("list of directories where artifacts are to be found after building")
                .build());
        return properties;

    }


    private static Builder createProperty(String key, PropertyType propertyType) {
        return PropertyDefinition.builder(key).type(propertyType).subCategory("VisualStudio");

    }
    public String[] getOutputPaths() {
        return settings.getStringArrayBySeparator(VisualStudioPlugin.VISUAL_STUDIO_OUTPUT_PATHS_PROPERTY_KEY, ",");
    }
}
