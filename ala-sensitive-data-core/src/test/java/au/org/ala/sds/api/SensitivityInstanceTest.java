package au.org.ala.sds.api;

import au.org.ala.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SensitivityInstanceTest extends TestUtils {
    @Test
    public void testToJson1() throws Exception {
        SensitivityInstance instance = SensitivityInstance.builder()
            .type(SensitivityInstance.SensitivityType.CONSERVATION)
            .zone(SensitivityZone.builder().name("Queensland").type("State").id("QLD").build())
            .authority("ALA")
            .generalisation(new GeneralisationRule("10km"))
            .dataResourceId("dr1023")
            .category(SensitivityCategory.builder().id("VU").value("Vulnerable").build())
            .build();
        ObjectMapper mapper = this.getObjectMapper();
        String json = mapper.writeValueAsString(instance);
        compareToResource("sensitivity-instance-1.json", json);
    }

    @Test
    public void testFromJson1() throws Exception {
        ObjectMapper mapper = this.getObjectMapper();
        SensitivityInstance instance = mapper.readValue(this.getResource("sensitivity-instance-1.json"), SensitivityInstance.class);
        assertEquals(SensitivityInstance.SensitivityType.CONSERVATION, instance.getType());
        assertNotNull(instance.getCategory());
        assertEquals("VU", instance.getCategory().getId());
        assertNotNull(instance.getZone());
        assertEquals("QLD", instance.getZone().getId());
        assertEquals("ALA", instance.getAuthority());
        assertNotNull(instance.getGeneralisation());
        assertEquals(10000, instance.getGeneralisation().getGeneralisationInMetres());
        assertFalse(instance.getGeneralisation().isWithhold());
    }

    @Test
    public void testToJson2() throws Exception {
        SensitivityInstance instance = SensitivityInstance.builder()
            .type(SensitivityInstance.SensitivityType.CONSERVATION)
            .zone(SensitivityZone.builder().name("New South Wales").type("State").id("NSW").build())
            .authority("ALA")
            .generalisation(new GeneralisationRule("WITHHOLD"))
            .dataResourceId("dr1029")
            .category(SensitivityCategory.builder().id("1").value("Highest Risk").build())
            .build();
        ObjectMapper mapper = this.getObjectMapper();
        String json = mapper.writeValueAsString(instance);
        compareToResource("sensitivity-instance-2.json", json);
    }

    @Test
    public void testFromJson2() throws Exception {
        ObjectMapper mapper = this.getObjectMapper();
        SensitivityInstance instance = mapper.readValue(this.getResource("sensitivity-instance-2.json"), SensitivityInstance.class);
        assertEquals(SensitivityInstance.SensitivityType.CONSERVATION, instance.getType());
        assertNotNull(instance.getCategory());
        assertEquals("1", instance.getCategory().getId());
        assertNotNull(instance.getZone());
        assertEquals("NSW", instance.getZone().getId());
        assertEquals("ALA", instance.getAuthority());
        assertNotNull(instance.getGeneralisation());
        assertEquals(1000000, instance.getGeneralisation().getGeneralisationInMetres());
        assertTrue(instance.getGeneralisation().isWithhold());
    }

}
