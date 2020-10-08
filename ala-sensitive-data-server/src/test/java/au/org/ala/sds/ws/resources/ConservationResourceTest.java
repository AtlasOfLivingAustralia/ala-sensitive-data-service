package au.org.ala.sds.ws.resources;

import au.org.ala.sds.api.*;
import au.org.ala.sds.validation.FactCollection;
import au.org.ala.sds.ws.core.SDSConfiguration;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.*;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.Assert.*;

public class ConservationResourceTest {
    private static SDSConfiguration configuration;
    private static ConservationResource resource;

    // It takes a while to build the SDS
    @BeforeClass
    public static void setUpClass() throws Exception {
        ((Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.INFO); // Stop logging insanity
        configuration = new SDSConfiguration();
        configuration = new SDSConfiguration();
        configuration.setIndex("/data/lucene/namematching-20200214"); // Ensure consistent index
        configuration.setSpeciesUrl(ModelResourceTest.class.getResource("/sensitive-species-data-1.xml").toExternalForm());
        configuration.setZonesUrl(ModelResourceTest.class.getResource("/sensitivity-zones-1.xml").toExternalForm());
        configuration.setCategoriesUrl(ModelResourceTest.class.getResource("/sensitivity-categories-1.xml").toExternalForm());
        configuration.setLayersUrl(ModelResourceTest.class.getResource("/layers-1.json").toExternalForm());
        configuration.configureSds();
        resource = new ConservationResource(configuration);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        configuration = null;
        resource = null;
    }

    @Test
    public void testGetSensitiveDataFields1() throws Exception {
        Set<String> fields = resource.getSensitiveDataFields();
        assertTrue(fields.contains("scientificName"));
        assertTrue(fields.contains("decimalLatitude"));
        assertTrue(fields.contains("eventDate"));
    }

    @Test
    public void testIsSensitiveCheck1() throws Exception {
        SpeciesCheck check;
        check = SpeciesCheck.builder().scientificName("Acacia dealbata").build();
        assertFalse(resource.isSensitive(check));
        check = SpeciesCheck.builder().scientificName("Caesia talingka").build();
        assertTrue(resource.isSensitive(check));
        check = SpeciesCheck.builder().scientificName("Caesia sp. Koolanooka Hills (R. Meissner & Y. Caruso 78)").build();
        assertTrue(resource.isSensitive(check));
    }

    @Test
    public void testIsSensitiveCheck2() throws Exception {
        SpeciesCheck check;
        check = SpeciesCheck.builder().scientificName("Acacia dealbata").taxonId("https://id.biodiversity.org.au/taxon/apni/51286863").build();
        assertFalse(resource.isSensitive(check));
        check = SpeciesCheck.builder().scientificName("Thryptomene stenophylla").taxonId("http://id.biodiversity.org.au/node/apni/2914477").build();
        assertTrue(resource.isSensitive(check));
        check = SpeciesCheck.builder().scientificName("Caesia sp. Koolanooka Hills (R. Meissner & Y. Caruso 78)").taxonId("ALA_Caesia_sp_Koolanooka_Hills_R_Meissner_Y_Caruso_78").build();
        assertTrue(resource.isSensitive(check));
        check = SpeciesCheck.builder().scientificName("Thryptomene stenophylla").taxonId("Some random junk").build();
        assertTrue(resource.isSensitive(check));
    }

    @Test
    public void testIsSensitive1() throws Exception {
        assertFalse(resource.isSensitive("Acacia dealbata", "https://id.biodiversity.org.au/taxon/apni/51286863"));
        assertTrue(resource.isSensitive("Thryptomene stenophylla", "http://id.biodiversity.org.au/node/apni/2914477"));
    }

    @Test
    public void testProcess1() throws Exception {
        Map<String, String> properties = new HashMap<>();
        properties.put(FactCollection.DECIMAL_LATITUDE_KEY, "-33.757122");
        properties.put(FactCollection.DECIMAL_LONGITUDE_KEY, "121.9266423");
        properties.put(FactCollection.EVENT_DATE_KEY, "2020-08-14");
        SensitivityQuery query = SensitivityQuery.builder().scientificName("Eucalyptus sparsa").properties(properties).build();
        SensitivityReport report = resource.process(query);
        assertNotNull(report);
        assertTrue(report.isValid());
        assertTrue(report.isSensitive());
        assertTrue(report.isLoadable());
        assertFalse(report.isAccessControl());
        ValidationReport vr = report.getReport();
        assertNotNull(vr);
        SensitiveTaxon taxon = vr.getTaxon();
        assertNotNull(taxon);
        assertEquals("Eucalyptus sparsa", taxon.getScientificName());
        assertEquals("http://id.biodiversity.org.au/node/apni/2914499".split("//")[1], taxon.getTaxonId().split("//")[1]);
        assertNotNull(taxon.getInstances());
        assertEquals(1, taxon.getInstances().size());
        SensitivityInstance instance = taxon.getInstances().get(0);
        assertEquals("WA DEC", instance.getAuthority());
        assertNotNull(instance.getCategory());
        assertEquals("Sensitive", instance.getCategory().getId());
        assertEquals("dr467", instance.getDataResourceId());
        assertNotNull(instance.getZone());
        assertEquals("WA", instance.getZone().getId());
        Map<String, Object> result = report.getResult();
        assertNotNull(result);
        assertEquals("121.9", result.get("decimalLongitude"));
        assertEquals("-33.8", result.get("decimalLatitude"));
        assertEquals("10000", result.get("generalisationInMetres"));
    }

    @Test
    public void testProcess2() throws Exception {
        Map<String, String> properties = new HashMap<>();
        properties.put(FactCollection.DECIMAL_LATITUDE_KEY, "-27.327739");
        properties.put(FactCollection.DECIMAL_LONGITUDE_KEY, "152.527914");
        properties.put(FactCollection.EVENT_DATE_KEY, "1990-04-12");
        SensitivityQuery query = SensitivityQuery.builder().scientificName("Litoria lorica").taxonId("urn:lsid:biodiversity.org.au:afd.taxon:8002722e-d0f8-4de2-af61-5cba07d44cd2").properties(properties).build();
        SensitivityReport report = resource.process(query);
        assertNotNull(report);
        assertTrue(report.isValid());
        assertTrue(report.isSensitive());
        assertTrue(report.isLoadable());
        assertFalse(report.isAccessControl());
        ValidationReport vr = report.getReport();
        assertNotNull(vr);
        SensitiveTaxon taxon = vr.getTaxon();
        assertNotNull(taxon);
        assertEquals("Litoria lorica", taxon.getScientificName());
        assertEquals("urn:lsid:biodiversity.org.au:afd.taxon:8002722e-d0f8-4de2-af61-5cba07d44cd2", taxon.getTaxonId());
        assertEquals("little waterfall frog", taxon.getCommonName());
        assertNotNull(taxon.getInstances());
        assertEquals(1, taxon.getInstances().size());
        SensitivityInstance instance = taxon.getInstances().get(0);
        assertEquals("Qld DEHP", instance.getAuthority());
        assertNotNull(instance.getCategory());
        assertEquals("Sensitive", instance.getCategory().getId());
        assertEquals("dr493", instance.getDataResourceId());
        assertNotNull(instance.getZone());
        assertEquals("QLD", instance.getZone().getId());
        Map<String, Object> result = report.getResult();
        assertNotNull(result);
        assertEquals("152.5", result.get("decimalLongitude"));
        assertEquals("-27.3", result.get("decimalLatitude"));
        assertEquals("10000", result.get("generalisationInMetres"));
    }

    @Test
    public void testProcess3() throws Exception {
        Map<String, String> properties = new HashMap<>();
        properties.put(FactCollection.DECIMAL_LATITUDE_KEY, "-26.453510");
        properties.put(FactCollection.DECIMAL_LONGITUDE_KEY, "114.653393");
        properties.put(FactCollection.EVENT_DATE_KEY, "1990-04-12");
        SensitivityQuery query = SensitivityQuery.builder().scientificName("Litoria lorica").taxonId("urn:lsid:biodiversity.org.au:afd.taxon:8002722e-d0f8-4de2-af61-5cba07d44cd2").properties(properties).build();
        SensitivityReport report = resource.process(query);
        assertNotNull(report);
        assertTrue(report.isValid());
        assertFalse(report.isSensitive());
        assertTrue(report.isLoadable());
        assertFalse(report.isAccessControl());
        ValidationReport vr = report.getReport();
        assertNotNull(vr);
        SensitiveTaxon taxon = vr.getTaxon();
        assertNotNull(taxon);
        assertEquals("Litoria lorica", taxon.getScientificName());
        assertEquals("urn:lsid:biodiversity.org.au:afd.taxon:8002722e-d0f8-4de2-af61-5cba07d44cd2", taxon.getTaxonId());
        assertEquals("little waterfall frog", taxon.getCommonName());
        assertNotNull(taxon.getInstances());
        assertEquals(1, taxon.getInstances().size());
        SensitivityInstance instance = taxon.getInstances().get(0);
        assertEquals("Qld DEHP", instance.getAuthority());
        assertNotNull(instance.getCategory());
        assertEquals("Sensitive", instance.getCategory().getId());
        assertEquals("dr493", instance.getDataResourceId());
        assertNotNull(instance.getZone());
        assertEquals("QLD", instance.getZone().getId());
        Map<String, Object> result = report.getResult();
        assertNotNull(result);
        assertNull(result.get("decimalLongitude"));
        assertNull(result.get("decimalLatitude"));
        assertNull(result.get("generalisationInMetres"));
    }

}
