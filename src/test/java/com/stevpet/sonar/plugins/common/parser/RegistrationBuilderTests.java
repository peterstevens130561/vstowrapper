package com.stevpet.sonar.plugins.common.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RegistrationBuilderTests {

    private String moduleName;
    private String modulePath;
    private String numBranchPoints;
    private ValueObservers elementObservers = new DefaultValueObservers();
    private ValueObservers pathObservers= new DefaultValueObservers();
    private ValueObservers attributeObservers= new DefaultValueObservers();
    private EventObservers entryObservers = new DefaultEventObservers();
    private EventObservers exitObservers = new DefaultEventObservers();
    

    @Test
    public void example() {
        DefaultObserverRegistrationFacade registrar = new DefaultObserverRegistrationFacade("",elementObservers, pathObservers, attributeObservers, entryObservers, exitObservers);
        registrar.setNewFacade(new PathSpecificationObserverRegistrationFacade("", elementObservers, pathObservers, attributeObservers, entryObservers, exitObservers) );
        registrar.inPath
        ("Modules", 
            ( modules -> modules
                .inPath("Module", 
                    (module -> module
                        .onElement("ModuleName", value -> moduleName = value)
                        .onElement("ModulePath",value -> modulePath=value)
                        .inPath("Summary",
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
        
    }
}
