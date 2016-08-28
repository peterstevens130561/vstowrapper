package com.stevpet.sonar.plugins.common.parser;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.DefaultXmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.XmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.observer.DefaultEventObservers;
import com.stevpet.sonar.plugins.common.parser.observer.DefaultValueObservers;
import com.stevpet.sonar.plugins.common.parser.observer.EventObservers;
import com.stevpet.sonar.plugins.common.parser.observer.PathSpecificationObserverRegistrationFacade;
import com.stevpet.sonar.plugins.common.parser.observer.ValueObservers;

public class RegistrationBuilderTests {

    private String moduleName;
    private String modulePath;
    private String numBranchPoints;
    private ValueObservers elementObservers = new DefaultValueObservers();
    private ValueObservers pathObservers= new DefaultValueObservers();
    private ValueObservers attributeObservers= new DefaultValueObservers();
    private EventObservers entryObservers = new DefaultEventObservers();
    private EventObservers exitObservers = new DefaultEventObservers();
	private XmlHierarchyBuilder xmlHierarchyBuilder = new DefaultXmlHierarchyBuilder();
	private List<String> hierarchy;
    

    @Test
    public void example() {
        PathSpecificationObserverRegistrationFacade registrar = new PathSpecificationObserverRegistrationFacade("",xmlHierarchyBuilder, elementObservers, pathObservers, attributeObservers, entryObservers, exitObservers);
        registrar.inPath
        ("Modules", 
            ( modules -> modules
                .inPath("Module", 
                    (module -> module
                        .onElement("ModuleName", value -> moduleName = value)
                        .onElement("ModulePath",value -> modulePath=value)
                        .inElement("Summary",
                            (summary -> summary.onAttribute("numBranchPoints",(value -> numBranchPoints=value))
                            )
                         )
                    )
                )
            )
        );
        elementObservers.observe("Modules/Module/ModuleName", "geewizz");
        elementObservers.observe("Modules/Module/ModulePath", "yogibear");
        attributeObservers.observe("Modules/Module/Summary/numBranchPoints", "321");
        assertEquals("geewizz",moduleName);
        assertEquals("yogibear",modulePath);
        assertEquals("321",numBranchPoints);
        hierarchy=xmlHierarchyBuilder.build();
        assertEquals("Only modules and module have children",2,hierarchy.size());
        
    }
}
