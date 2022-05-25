package au.org.ala.sds.generalise;

import au.org.ala.sds.api.*;
import au.org.ala.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.TermFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ClearGeneralisationTest extends TestUtils {
    @Test
    public void testToJson1() throws Exception {
        ClearGeneralisation generalisation = new ClearGeneralisation(DwcTerm.locality);
        ObjectMapper mapper = this.getObjectMapper();
        String json = mapper.writeValueAsString(generalisation);
        compareToResource("clear-generalisation-1.json", json);
    }

    @Test
    public void testFromJson1() throws Exception {
        ObjectMapper mapper = this.getObjectMapper();
        Generalisation generalisation = mapper.readValue(this.getResource("clear-generalisation-1.json"), Generalisation.class);
        assertEquals(ClearGeneralisation.class, generalisation.getClass());
        ClearGeneralisation clearGeneralisation = (ClearGeneralisation) generalisation;
        assertEquals(DwcTerm.locality, clearGeneralisation.getField().getField());
    }


    @Test
    public void testFromJson2() throws Exception {
        ObjectMapper mapper = this.getObjectMapper();
        Generalisation generalisation = mapper.readValue(this.getResource("clear-generalisation-2.json"), Generalisation.class);
        assertEquals(ClearGeneralisation.class, generalisation.getClass());
        ClearGeneralisation clearGeneralisation = (ClearGeneralisation) generalisation;
        assertEquals(DwcTerm.datasetName, clearGeneralisation.getField().getField());
    }

    @Test
    public void testFromJson3() throws Exception {
        ObjectMapper mapper = this.getObjectMapper();
        Generalisation generalisation = mapper.readValue(this.getResource("clear-generalisation-3.json"), Generalisation.class);
        assertEquals(ClearGeneralisation.class, generalisation.getClass());
        ClearGeneralisation clearGeneralisation = (ClearGeneralisation) generalisation;
        assertEquals(DwcTerm.decimalLatitude, clearGeneralisation.getField().getField());
    }

    @Test
    public void testProcess1() throws Exception {
        String localityName = DwcTerm.locality.simpleName();
        String longitudeName = DwcTerm.decimalLongitude.simpleName();
        Generalisation generalisation = new ClearGeneralisation(DwcTerm.locality);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(localityName, "Somewhere else");
        supplied.put(longitudeName, "145.786");
        SensitivityInstance instance = SensitivityInstance.builder().build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(1, original.size());
        assertEquals("Somewhere else", original.get(localityName));
        assertEquals(1, updated.size());
        assertTrue(updated.containsKey(localityName));
        assertNull(updated.get(localityName));

    }


    @Test
    public void testProcess2() throws Exception {
        String localityName = DwcTerm.locality.prefixedName();
        String longitudeName = DwcTerm.decimalLongitude.prefixedName();
        Generalisation generalisation = new ClearGeneralisation(DwcTerm.locality);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(localityName, "Somewhere else");
        supplied.put(longitudeName, "145.786");
        SensitivityInstance instance = SensitivityInstance.builder().build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(1, original.size());
        assertEquals("Somewhere else", original.get(localityName));
        assertEquals(1, updated.size());
        assertTrue(updated.containsKey(localityName));
        assertNull(updated.get(localityName));

    }

    @Test
    public void testProcess3() throws Exception {
        String localityName = DwcTerm.locality.qualifiedName();
        String longitudeName = DwcTerm.decimalLongitude.qualifiedName();
        Generalisation generalisation = new ClearGeneralisation(DwcTerm.locality);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(localityName, "Somewhere else");
        supplied.put(longitudeName, "145.786");
        SensitivityInstance instance = SensitivityInstance.builder().build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(1, original.size());
        assertEquals("Somewhere else", original.get(localityName));
        assertEquals(1, updated.size());
        assertTrue(updated.containsKey(localityName));
        assertNull(updated.get(localityName));

    }

    @Test
    public void testProcess4() throws Exception {
        String eventDateEnd = "eventDateEnd";
        Generalisation generalisation = new ClearGeneralisation(TermFactory.instance().findTerm(eventDateEnd));
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(eventDateEnd, "2021-05-24");
        SensitivityInstance instance = SensitivityInstance.builder().build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(1, original.size());
        assertEquals("2021-05-24", original.get(eventDateEnd));
        assertEquals(1, updated.size());
        assertTrue(updated.containsKey(eventDateEnd));
        assertNull(updated.get(eventDateEnd));
    }

}
