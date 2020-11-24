package au.org.ala.sds.generalise;

import au.org.ala.sds.api.GeneralisationRule;
import au.org.ala.sds.api.SensitivityCategory;
import au.org.ala.sds.api.SensitivityInstance;
import au.org.ala.sds.api.SensitivityZone;
import au.org.ala.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.gbif.dwc.terms.DwcTerm;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MessageGeneralisationTest extends TestUtils {
    @Test
    public void testToJson1() throws Exception {
        MessageGeneralisation generalisation = new MessageGeneralisation(DwcTerm.dataGeneralizations, "Well, hello", true, MessageGeneralisation.Trigger.GENERALISE);
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
        assertEquals(MessageGeneralisation.Trigger.GENERALISE, messageGeneralisation.getTrigger());
    }


    @Test
    public void testFromJson2() throws Exception {
        ObjectMapper mapper = this.getObjectMapper();
        Generalisation generalisation = mapper.readValue(this.getResource("message-generalisation-2.json"), Generalisation.class);
        assertEquals(MessageGeneralisation.class, generalisation.getClass());
        MessageGeneralisation messageGeneralisation = (MessageGeneralisation) generalisation;
        assertEquals(DwcTerm.dataGeneralizations, messageGeneralisation.getField().getField());
        assertEquals(MessageGeneralisation.Trigger.ANY, messageGeneralisation.getTrigger());
    }

    @Test
    public void testFromJson3() throws Exception {
        ObjectMapper mapper = this.getObjectMapper();
        Generalisation generalisation = mapper.readValue(this.getResource("message-generalisation-3.json"), Generalisation.class);
        assertEquals(MessageGeneralisation.class, generalisation.getClass());
        MessageGeneralisation messageGeneralisation = (MessageGeneralisation) generalisation;
        assertEquals(DwcTerm.dataGeneralizations, messageGeneralisation.getField().getField());
        assertEquals(MessageGeneralisation.Trigger.ANY, messageGeneralisation.getTrigger());
    }

    @Test
    public void testFromYaml1() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Generalisation generalisation = mapper.readValue(this.getResource("message-generalisation-1.yml"), Generalisation.class);
        assertEquals(MessageGeneralisation.class, generalisation.getClass());
        MessageGeneralisation messageGeneralisation = (MessageGeneralisation) generalisation;
        assertEquals(DwcTerm.dataGeneralizations, messageGeneralisation.getField().getField());
        assertEquals("Well, hello", messageGeneralisation.getMessage());
        assertTrue(messageGeneralisation.isAppend());
        assertEquals(MessageGeneralisation.Trigger.GENERALISE, messageGeneralisation.getTrigger());
    }

    @Test
    public void testProcess1() throws Exception {
        String dataGeneralizationsName = DwcTerm.dataGeneralizations.simpleName();
        Generalisation generalisation = new MessageGeneralisation(DwcTerm.dataGeneralizations, "I''m a teapot", true, MessageGeneralisation.Trigger.ANY);
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
        Generalisation generalisation = new MessageGeneralisation(DwcTerm.dataGeneralizations, "William byrne", false, MessageGeneralisation.Trigger.ANY);
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
        Generalisation generalisation = new MessageGeneralisation(DwcTerm.dataGeneralizations, "I am a message", true, MessageGeneralisation.Trigger.ANY);
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


    @Test
    public void testProcess4() throws Exception {
        String dataGeneralizationsName = DwcTerm.dataGeneralizations.simpleName();
        Generalisation generalisation = new MessageGeneralisation(DwcTerm.dataGeneralizations, "Record in {2} is {1}. Generalised to {3} by {0}", true, MessageGeneralisation.Trigger.ANY);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        SensitivityInstance instance = SensitivityInstance.builder()
            .category(SensitivityCategory.builder().id("1").value("Category 1").type("Conservation").build())
            .zone(SensitivityZone.builder().id("NSW").name("New South Wales").type("State").build())
            .generalisation(new GeneralisationRule("10km"))
            .authority("NSW OEH")
            .type(SensitivityInstance.SensitivityType.CONSERVATION)
            .build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(1, updated.size());
        assertTrue(updated.containsKey(dataGeneralizationsName));
        assertEquals("Record in New South Wales is Category 1. Generalised to 10km by NSW OEH", updated.get(dataGeneralizationsName));

    }

    @Test
    public void testProcess5() throws Exception {
        String dataGeneralizationsName = DwcTerm.dataGeneralizations.simpleName();
        Generalisation generalisation = new MessageGeneralisation(DwcTerm.dataGeneralizations, "Record in {2} is {1}. Generalised to {3} by {0}", true, MessageGeneralisation.Trigger.WITHHOLD);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        SensitivityInstance instance = SensitivityInstance.builder()
            .category(SensitivityCategory.builder().id("1").value("Category 1").type("Conservation").build())
            .zone(SensitivityZone.builder().id("NSW").name("New South Wales").type("State").build())
            .generalisation(new GeneralisationRule("10km"))
            .authority("NSW OEH")
            .type(SensitivityInstance.SensitivityType.CONSERVATION)
            .build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(0, updated.size());
    }


    @Test
    public void testProcess6() throws Exception {
        String dataGeneralizationsName = DwcTerm.dataGeneralizations.simpleName();
        Generalisation generalisation = new MessageGeneralisation(DwcTerm.dataGeneralizations, "Information withheld by {0}. Record in {2} is {1}", true, MessageGeneralisation.Trigger.WITHHOLD);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        SensitivityInstance instance = SensitivityInstance.builder()
            .category(SensitivityCategory.builder().id("1").value("Category 1").type("Conservation").build())
            .zone(SensitivityZone.builder().id("NSW").name("New South Wales").type("State").build())
            .generalisation(new GeneralisationRule("WITHHOLD"))
            .authority("NSW OEH")
            .type(SensitivityInstance.SensitivityType.CONSERVATION)
            .build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(1, updated.size());
        assertTrue(updated.containsKey(dataGeneralizationsName));
        assertEquals("Information withheld by NSW OEH. Record in New South Wales is Category 1", updated.get(dataGeneralizationsName));
    }

}
