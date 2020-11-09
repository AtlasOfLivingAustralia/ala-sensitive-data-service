package au.org.ala.sds.generalise;

import au.org.ala.sds.api.SensitivityInstance;
import au.org.ala.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gbif.dwc.terms.DwcTerm;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MessageGeneralisationTest extends TestUtils {
    @Test
    public void testToJson1() throws Exception {
        MessageGeneralisation generalisation = new MessageGeneralisation(DwcTerm.dataGeneralizations, "Well, hello", true);
        ObjectMapper mapper = this.getObjectMapper();
        String json = mapper.writeValueAsString(generalisation);
        compareToResource("message-generalisation-1.json", json);
    }

    @Test
    public void testFromJson1() throws Exception {
        ObjectMapper mapper = this.getObjectMapper();
        Generalisation generalisation = mapper.readValue(this.getResource("message-generalisation-1.json"), Generalisation.class);
        assertEquals(MessageGeneralisation.class, generalisation.getClass());
        MessageGeneralisation messageGeneralisation = (MessageGeneralisation) generalisation;
        assertEquals(DwcTerm.dataGeneralizations, messageGeneralisation.getField().getField());
        assertEquals("Well, hello", messageGeneralisation.getMessage());
        assertTrue(messageGeneralisation.isAppend());
    }


    @Test
    public void testFromJson2() throws Exception {
        ObjectMapper mapper = this.getObjectMapper();
        Generalisation generalisation = mapper.readValue(this.getResource("message-generalisation-2.json"), Generalisation.class);
        assertEquals(MessageGeneralisation.class, generalisation.getClass());
        MessageGeneralisation messageGeneralisation = (MessageGeneralisation) generalisation;
        assertEquals(DwcTerm.dataGeneralizations, messageGeneralisation.getField().getField());
    }

    @Test
    public void testFromJson3() throws Exception {
        ObjectMapper mapper = this.getObjectMapper();
        Generalisation generalisation = mapper.readValue(this.getResource("message-generalisation-3.json"), Generalisation.class);
        assertEquals(MessageGeneralisation.class, generalisation.getClass());
        MessageGeneralisation messageGeneralisation = (MessageGeneralisation) generalisation;
        assertEquals(DwcTerm.dataGeneralizations, messageGeneralisation.getField().getField());
    }

    @Test
    public void testProcess1() throws Exception {
        String dataGeneralizationsName = DwcTerm.dataGeneralizations.simpleName();
        Generalisation generalisation = new MessageGeneralisation(DwcTerm.dataGeneralizations, "I''m a teapot", true);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        SensitivityInstance instance = SensitivityInstance.builder().build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(1, original.size());
        assertEquals(null, original.get(dataGeneralizationsName));
        assertEquals(1, updated.size());
        assertTrue(updated.containsKey(dataGeneralizationsName));
        assertEquals("I'm a teapot", updated.get(dataGeneralizationsName));

    }


    @Test
    public void testProcess2() throws Exception {
        String dataGeneralizationsName = DwcTerm.dataGeneralizations.prefixedName();
        String longitudeName = DwcTerm.decimalLongitude.prefixedName();
        Generalisation generalisation = new MessageGeneralisation(DwcTerm.dataGeneralizations, "William byrne", false);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(dataGeneralizationsName, "Somewhere else");
        supplied.put(longitudeName, "145.786");
        SensitivityInstance instance = SensitivityInstance.builder().build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(1, original.size());
        assertEquals("Somewhere else", original.get(dataGeneralizationsName));
        assertEquals(1, updated.size());
        assertTrue(updated.containsKey(dataGeneralizationsName));
        assertEquals("William byrne", updated.get(dataGeneralizationsName));

    }

    @Test
    public void testProcess3() throws Exception {
        String dataGeneralizationsName = DwcTerm.dataGeneralizations.qualifiedName();
        String longitudeName = DwcTerm.decimalLongitude.qualifiedName();
        Generalisation generalisation = new MessageGeneralisation(DwcTerm.dataGeneralizations, "I am a message", true);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(dataGeneralizationsName, "Somewhere else");
        supplied.put(longitudeName, "145.786");
        SensitivityInstance instance = SensitivityInstance.builder().build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(1, original.size());
        assertEquals("Somewhere else", original.get(dataGeneralizationsName));
        assertEquals(1, updated.size());
        assertTrue(updated.containsKey(dataGeneralizationsName));
        assertEquals("Somewhere else I am a message", updated.get(dataGeneralizationsName));

    }

}
