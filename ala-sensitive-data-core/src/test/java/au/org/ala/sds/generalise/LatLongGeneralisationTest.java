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

public class LatLongGeneralisationTest extends TestUtils {
    @Test
    public void testToJson1() throws Exception {
        LatLongGeneralisation generalisation = new LatLongGeneralisation(DwcTerm.decimalLatitude, DwcTerm.decimalLongitude);
        ObjectMapper mapper = this.getObjectMapper();
        String json = mapper.writeValueAsString(generalisation);
        compareToResource("latlong-generalisation-1.json", json);
    }

    @Test
    public void testFromJson1() throws Exception {
        ObjectMapper mapper = this.getObjectMapper();
        Generalisation generalisation = mapper.readValue(this.getResource("latlong-generalisation-1.json"), Generalisation.class);
        assertEquals(LatLongGeneralisation.class, generalisation.getClass());
        LatLongGeneralisation clearGeneralisation = (LatLongGeneralisation) generalisation;
        assertEquals(DwcTerm.decimalLatitude, clearGeneralisation.getLatitudeField().getField());
        assertEquals(DwcTerm.decimalLongitude, clearGeneralisation.getLongitudeField().getField());
    }


    @Test
    public void testProcess1() throws Exception {
        String localityName = DwcTerm.locality.prefixedName();
        String latitudeName = DwcTerm.decimalLatitude.prefixedName();
        String longitudeName = DwcTerm.decimalLongitude.prefixedName();
        Generalisation generalisation = new LatLongGeneralisation(DwcTerm.decimalLatitude, DwcTerm.decimalLongitude);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(localityName, "Somewhere else");
        supplied.put(latitudeName, "-37.2234");
        supplied.put(longitudeName, "145.786");
        GeneralisationRule rule = new GeneralisationRule("1km");
        SensitivityInstance instance = SensitivityInstance.builder().generalisation(rule).build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(2, original.size());
        assertEquals("-37.2234", original.get(latitudeName));
        assertEquals("145.786", original.get(longitudeName));
        assertEquals(2, updated.size());
        assertEquals("-37.23", updated.get(latitudeName));
        assertEquals("145.79", updated.get(longitudeName));
    }


    @Test
    public void testProcess2() throws Exception {
        String localityName = DwcTerm.locality.prefixedName();
        String latitudeName = DwcTerm.decimalLatitude.prefixedName();
        String longitudeName = DwcTerm.decimalLongitude.prefixedName();
        Generalisation generalisation = new LatLongGeneralisation(DwcTerm.decimalLatitude, DwcTerm.decimalLongitude);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(localityName, "Somewhere else");
        supplied.put(latitudeName, "-37.2234");
        supplied.put(longitudeName, "145.786");
        GeneralisationRule rule = new GeneralisationRule("5km");
        SensitivityInstance instance = SensitivityInstance.builder().generalisation(rule).build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(2, original.size());
        assertEquals("-37.2234", original.get(latitudeName));
        assertEquals("145.786", original.get(longitudeName));
        assertEquals(2, updated.size());
        assertEquals("-37.2", updated.get(latitudeName));
        assertEquals("145.8", updated.get(longitudeName));
    }


    @Test
    public void testProcess3() throws Exception {
        String localityName = DwcTerm.locality.prefixedName();
        String latitudeName = DwcTerm.decimalLatitude.prefixedName();
        String longitudeName = DwcTerm.decimalLongitude.prefixedName();
        Generalisation generalisation = new LatLongGeneralisation(DwcTerm.decimalLatitude, DwcTerm.decimalLongitude);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(localityName, "Somewhere else");
        supplied.put(latitudeName, "-37.2234");
        supplied.put(longitudeName, "145.786");
        GeneralisationRule rule = new GeneralisationRule("200km");
        SensitivityInstance instance = SensitivityInstance.builder().generalisation(rule).build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(2, original.size());
        assertEquals("-37.2234", original.get(latitudeName));
        assertEquals("145.786", original.get(longitudeName));
        assertEquals(2, updated.size());
        assertEquals("-38", updated.get(latitudeName)); // Within 200km means within 2degrees
        assertEquals("147", updated.get(longitudeName));
    }

    @Test
    public void testProcess5() throws Exception {
        String localityName = DwcTerm.locality.prefixedName();
        String latitudeName = DwcTerm.decimalLatitude.prefixedName();
        String longitudeName = DwcTerm.decimalLongitude.prefixedName();
        Generalisation generalisation = new LatLongGeneralisation(DwcTerm.decimalLatitude, DwcTerm.decimalLongitude);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(localityName, "Somewhere else");
        supplied.put(latitudeName, "-37.2234");
        supplied.put(longitudeName, "145.786");
        GeneralisationRule rule = new GeneralisationRule("100m");
        SensitivityInstance instance = SensitivityInstance.builder().generalisation(rule).build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(2, original.size());
        assertEquals("-37.2234", original.get(latitudeName));
        assertEquals("145.786", original.get(longitudeName));
        assertEquals(2, updated.size());
        assertEquals("-37.22", updated.get(latitudeName));
        assertEquals("145.79", updated.get(longitudeName));
    }

    @Test
    public void testProcess6() throws Exception {
        String localityName = DwcTerm.locality.prefixedName();
        String latitudeName = DwcTerm.decimalLatitude.prefixedName();
        String longitudeName = DwcTerm.decimalLongitude.prefixedName();
        Generalisation generalisation = new LatLongGeneralisation(DwcTerm.decimalLatitude, DwcTerm.decimalLongitude);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(localityName, "Somewhere else");
        supplied.put(latitudeName, "70.552");
        supplied.put(longitudeName, "145.786");
        GeneralisationRule rule = new GeneralisationRule("1000m");
        SensitivityInstance instance = SensitivityInstance.builder().generalisation(rule).build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(2, original.size());
        assertEquals("70.552", original.get(latitudeName));
        assertEquals("145.786", original.get(longitudeName));
        assertEquals(2, updated.size());
        assertEquals("70.56", updated.get(latitudeName));
        assertEquals("145.78", updated.get(longitudeName));
    }

    @Test
    public void testProcess7() throws Exception {
        String localityName = DwcTerm.locality.prefixedName();
        String latitudeName = DwcTerm.decimalLatitude.prefixedName();
        String longitudeName = DwcTerm.decimalLongitude.prefixedName();
        Generalisation generalisation = new LatLongGeneralisation(DwcTerm.decimalLatitude, DwcTerm.decimalLongitude);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(localityName, "Somewhere else");
        supplied.put(latitudeName, "89");
        supplied.put(longitudeName, "146.786");
        GeneralisationRule rule = new GeneralisationRule("1000m");
        SensitivityInstance instance = SensitivityInstance.builder().generalisation(rule).build();
        generalisation.process(supplied, original, updated, instance);
        assertEquals(2, original.size());
        assertEquals("89", original.get(latitudeName));
        assertEquals("146.786", original.get(longitudeName));
        assertEquals(2, updated.size());
        assertEquals("89.00", updated.get(latitudeName));
        assertEquals("146.94", updated.get(longitudeName));
    }

}
