package com.stevpet.sonar.plugins.common.parser;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.DefaultXmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.XmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.observer.ObserversRepository;
import com.stevpet.sonar.plugins.common.parser.observer.ParserEventArgs;
import com.stevpet.sonar.plugins.common.parser.observer.impl.DefaultObserversRepository;
import com.stevpet.sonar.plugins.common.parser.observerdsl.DefaultTopLevelObserverRegistrar;

public class RegistrationBuilderTests {

    private String moduleName;
    private String modulePath;
    private String numBranchPoints;
	private XmlHierarchyBuilder xmlHierarchyBuilder = new DefaultXmlHierarchyBuilder();
	private List<String> hierarchy;
	private ObserversRepository observersRepository;
	@Mock private  ParserEventArgs  eventArgs;
    

	@Before
	public void before() {
    	org.mockito.MockitoAnnotations.initMocks(this);
	}
    @Test
    public void example() {

    	observersRepository = new DefaultObserversRepository();
        DefaultTopLevelObserverRegistrar registrar = new DefaultTopLevelObserverRegistrar(observersRepository);
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
        hierarchy=observersRepository.buildHierarchy();
        assertEquals("Only modules and module have children",2,hierarchy.size());
        
    }
    
    @Test
    public void testEventArgsObserver() {
    	observersRepository = new DefaultObserversRepository();
    	observersRepository.registerElementEventArgsObserver("parent", r -> r.setError());
    	observersRepository.observeElement("parent", eventArgs);
    	verify(eventArgs,times(1)).setError();
    }
}
