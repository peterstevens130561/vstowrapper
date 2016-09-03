package com.stevpet.sonar.plugins.common.parser;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.DefaultXmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.XmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.observer.DefaultEventObservers;
import com.stevpet.sonar.plugins.common.parser.observer.DefaultObserversRepository;
import com.stevpet.sonar.plugins.common.parser.observer.DefaultValueObservers;
import com.stevpet.sonar.plugins.common.parser.observer.EventObservers;
import com.stevpet.sonar.plugins.common.parser.observer.ObserversRepository;
import com.stevpet.sonar.plugins.common.parser.observer.PathSpecificationObserverRegistrationFacade;
import com.stevpet.sonar.plugins.common.parser.observer.StartObserverRegistration;
import com.stevpet.sonar.plugins.common.parser.observer.ValueObservers;

public class RegistrationBuilderTests {

    private String moduleName;
    private String modulePath;
    private String numBranchPoints;
	private XmlHierarchyBuilder xmlHierarchyBuilder = new DefaultXmlHierarchyBuilder();
	private List<String> hierarchy;
	private ObserversRepository observersRepository;
    

    @Test
    public void example() {
    	observersRepository = new DefaultObserversRepository();
        StartObserverRegistration registrar = new StartObserverRegistration(observersRepository);
        registrar.inPath("Modules/Module")
                .onElement("ModuleName", value -> moduleName = value)
                .onElement("ModulePath",value -> modulePath=value);
        registrar.inPath("Modules/Module")
                .inElement("Summary",
                    (summary -> summary.onAttribute("numBranchPoints",value -> numBranchPoints=value)));

        observersRepository.observeElement("Modules/Module/ModuleName", "geewizz");
        observersRepository.observeElement("Modules/Module/ModulePath", "yogibear");
        observersRepository.observeAttribute("Modules/Module/Summary/numBranchPoints", "321");
        assertEquals("geewizz",moduleName);
        assertEquals("yogibear",modulePath);
        assertEquals("321",numBranchPoints);
        hierarchy=xmlHierarchyBuilder.build();
        assertEquals("Only modules and module have children",2,hierarchy.size());
        
    }
}
