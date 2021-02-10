package au.org.ala.sds.generalise;

import au.org.ala.sds.api.SensitivityInstance;
import au.org.ala.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gbif.dwc.terms.DwcTerm;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RetainGeneralisationTest extends TestUtils {
    @Test
    public void testToJson1() throws Exception {
        RetainGeneralisation generalisation = new RetainGeneralisation(DwcTerm.stateProvince);
        ObjectMapper mapper = this.getObjectMapper();
        String json = mapper.writeValueAsString(generalisation);
        compareToResource("retain-generalisation-1.json", json);
    }

    @Test
    public void testFromJson1() throws Exception {
        ObjectMapper mapper = this.getObjectMapper();
        Generalisation generalisation = mapper.readValue(this.getResource("retain-generalisation-1.json"), Generalisation.class);
        assertEquals(RetainGeneralisation.class, generalisation.getClass());
        RetainGeneralisation retainGeneralisation = (RetainGeneralisation) generalisation;
        assertEquals(DwcTerm.stateProvince, retainGeneralisation.getField().getField());
    }

    @Test
    public void testProcess1() throws Exception {
        String stateProvinceName = DwcTerm.stateProvince.simpleName();
        String longitudeName = DwcTerm.decimalLongitude.simpleName();
        Generalisation generalisation = new RetainGeneralisation(DwcTerm.stateProvince);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(stateProvinceName, "New South Wales");
        supplied.put(longitudeName, "145.786");
        SensitivityInstance instance = SensitivityInstance.builder().build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(0, original.size());
        assertEquals(0, updated.size());
    }
}
