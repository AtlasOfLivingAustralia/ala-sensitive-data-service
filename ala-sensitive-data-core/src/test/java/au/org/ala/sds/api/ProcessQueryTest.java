package au.org.ala.sds.api;

import au.org.ala.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ProcessQueryTest extends TestUtils {
    @Test
    public void testToJson1() throws Exception {
        Map<String, String> properties = new HashMap<>();
        properties.put("decimalLatitude", "-27.1529712");
        properties.put("decimalLongitude", "152.158576");
        ProcessQuery query = ProcessQuery.builder()
            .scientificName("Varanus semiremex")
            .taxonId("urn:lsid:biodiversity.org.au:afd.taxon:81244b7e-7145-42fe-b28c-3d672310e739")
            .dataResourceUid("dr839")
            .stateProvince("Queensland")
            .country("Australia")
            .properties(properties)
            .build();
        ObjectMapper mapper = this.getObjectMapper();
        String json = mapper.writeValueAsString(query);
        compareToResource("process-query-1.json", json);
    }

    @Test
    public void testFromJson1() throws Exception {
        ObjectMapper mapper = this.getObjectMapper();
        ProcessQuery query = mapper.readValue(this.getResource("process-query-1.json"), ProcessQuery.class);
        assertEquals("Varanus semiremex", query.getScientificName());
        assertEquals("urn:lsid:biodiversity.org.au:afd.taxon:81244b7e-7145-42fe-b28c-3d672310e739", query.getTaxonId());
        assertEquals("dr839", query.getDataResourceUid());
        assertEquals("Queensland", query.getStateProvince());
        assertNull(query.getZones());
        assertNotNull(query.getProperties());
        assertEquals(2, query.getProperties().size());
        assertEquals("-27.1529712", query.getProperties().get("decimalLatitude"));
     }
}
