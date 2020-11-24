package au.org.ala.sds.api;

import au.org.ala.sds.generalise.ClearGeneralisation;
import au.org.ala.sds.generalise.Generalisation;
import au.org.ala.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gbif.dwc.terms.DwcTerm;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SensitivityQueryTest extends TestUtils {
    @Test
    public void testToJson1() throws Exception {
        SensitivityQuery query = SensitivityQuery.builder()
            .scientificName("Varanus semiremex")
            .taxonId("urn:lsid:biodiversity.org.au:afd.taxon:81244b7e-7145-42fe-b28c-3d672310e739")
            .dataResourceUid("dr839")
            .stateProvince("Queensland")
            .country("Australia")
            .zones(Arrays.asList("QEX"))
            .build();
        ObjectMapper mapper = this.getObjectMapper();
        String json = mapper.writeValueAsString(query);
        compareToResource("sensitivity-query-1.json", json);
    }

    @Test
    public void testFromJson1() throws Exception {
        ObjectMapper mapper = this.getObjectMapper();
        SensitivityQuery query = mapper.readValue(this.getResource("sensitivity-query-1.json"), SensitivityQuery.class);
        assertEquals("Varanus semiremex", query.getScientificName());
        assertEquals("urn:lsid:biodiversity.org.au:afd.taxon:81244b7e-7145-42fe-b28c-3d672310e739", query.getTaxonId());
        assertEquals("dr839", query.getDataResourceUid());
        assertEquals("Queensland", query.getStateProvince());
        assertNotNull(query.getZones());
        assertEquals(1, query.getZones().size());
        assertEquals("QEX", query.getZones().get(0));
    }
}
