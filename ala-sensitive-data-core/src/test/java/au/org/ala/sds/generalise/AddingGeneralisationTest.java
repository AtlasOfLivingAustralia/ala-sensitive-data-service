package au.org.ala.sds.generalise;

import au.org.ala.sds.api.GeneralisationRule;
import au.org.ala.sds.api.SensitivityInstance;
import au.org.ala.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gbif.dwc.terms.DwcTerm;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AddingGeneralisationTest extends TestUtils {
    @Test
    public void testToJson1() throws Exception {
        AddingGeneralisation generalisation = new AddingGeneralisation(DwcTerm.coordinateUncertaintyInMeters, true, true, 10);
        ObjectMapper mapper = this.getObjectMapper();
        String json = mapper.writeValueAsString(generalisation);
        compareToResource("adding-generalisation-1.json", json);
    }

    @Test
    public void testFromJson1() throws Exception {
        ObjectMapper mapper = this.getObjectMapper();
        Generalisation generalisation = mapper.readValue(this.getResource("adding-generalisation-1.json"), Generalisation.class);
        assertEquals(AddingGeneralisation.class, generalisation.getClass());
        AddingGeneralisation addingGeneralisation = (AddingGeneralisation) generalisation;
        assertEquals(DwcTerm.coordinateUncertaintyInMeters, addingGeneralisation.getField().getField());
        assertEquals(true, addingGeneralisation.isRetainUnparsable());
        assertEquals(true, addingGeneralisation.isUseSensitivity());
        assertEquals(10, addingGeneralisation.getAdd());
    }

    @Test
    public void testProcess1() throws Exception {
        String coordinateUncertaintyInMetersName = DwcTerm.coordinateUncertaintyInMeters.simpleName();
        String longitudeName = DwcTerm.decimalLongitude.simpleName();
        Generalisation generalisation = new AddingGeneralisation(DwcTerm.coordinateUncertaintyInMeters, true, true, 40);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(coordinateUncertaintyInMetersName, "100");
        supplied.put(longitudeName, "145.786");
        GeneralisationRule rule = new GeneralisationRule("400m");
        SensitivityInstance instance = SensitivityInstance.builder().generalisation(rule).build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(1, original.size());
        assertEquals("100", original.get(coordinateUncertaintyInMetersName));
        assertEquals(1, updated.size());
        assertEquals("540", updated.get(coordinateUncertaintyInMetersName));
    }

    @Test
    public void testProcess2() throws Exception {
        String coordinateUncertaintyInMetersName = DwcTerm.coordinateUncertaintyInMeters.simpleName();
        String longitudeName = DwcTerm.decimalLongitude.simpleName();
        Generalisation generalisation = new AddingGeneralisation(DwcTerm.coordinateUncertaintyInMeters, true, true, 40);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(coordinateUncertaintyInMetersName, "30-50km");
        supplied.put(longitudeName, "145.786");
        GeneralisationRule rule = new GeneralisationRule("400m");
        SensitivityInstance instance = SensitivityInstance.builder().generalisation(rule).build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(0, original.size());
        assertEquals(0, updated.size());
    }


    @Test
    public void testProcess3() throws Exception {
        String coordinateUncertaintyInMetersName = DwcTerm.coordinateUncertaintyInMeters.simpleName();
        String longitudeName = DwcTerm.decimalLongitude.simpleName();
        Generalisation generalisation = new AddingGeneralisation(DwcTerm.coordinateUncertaintyInMeters, false, true, 40);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(coordinateUncertaintyInMetersName, "100");
        supplied.put(longitudeName, "145.786");
        GeneralisationRule rule = new GeneralisationRule("400m");
        SensitivityInstance instance = SensitivityInstance.builder().generalisation(rule).build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(1, original.size());
        assertEquals("100", original.get(coordinateUncertaintyInMetersName));
        assertEquals(1, updated.size());
        assertEquals("540", updated.get(coordinateUncertaintyInMetersName));
    }

    @Test
    public void testProcess4() throws Exception {
        String coordinateUncertaintyInMetersName = DwcTerm.coordinateUncertaintyInMeters.simpleName();
        String longitudeName = DwcTerm.decimalLongitude.simpleName();
        Generalisation generalisation = new AddingGeneralisation(DwcTerm.coordinateUncertaintyInMeters, false, true, 40);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(coordinateUncertaintyInMetersName, "30-50km");
        supplied.put(longitudeName, "145.786");
        GeneralisationRule rule = new GeneralisationRule("400m");
        SensitivityInstance instance = SensitivityInstance.builder().generalisation(rule).build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(1, original.size());
        assertEquals("30-50km", original.get(coordinateUncertaintyInMetersName));
        assertEquals(1, updated.size());
        assertEquals("440", updated.get(coordinateUncertaintyInMetersName));
    }

    @Test
    public void testProcess5() throws Exception {
        String coordinateUncertaintyInMetersName = DwcTerm.coordinateUncertaintyInMeters.simpleName();
        String longitudeName = DwcTerm.decimalLongitude.simpleName();
        Generalisation generalisation = new AddingGeneralisation(DwcTerm.coordinateUncertaintyInMeters, false, false, 40);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(coordinateUncertaintyInMetersName, "100");
        supplied.put(longitudeName, "145.786");
        GeneralisationRule rule = new GeneralisationRule("400m");
        SensitivityInstance instance = SensitivityInstance.builder().generalisation(rule).build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(1, original.size());
        assertEquals("100", original.get(coordinateUncertaintyInMetersName));
        assertEquals(1, updated.size());
        assertEquals("140", updated.get(coordinateUncertaintyInMetersName));
    }

}
