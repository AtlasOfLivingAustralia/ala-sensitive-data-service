package au.org.ala.sds.ws;

import au.org.ala.sds.api.SensitivityReport;
import au.org.ala.sds.api.SpeciesCheck;
import au.org.ala.sds.generalise.Generalisation;
import au.org.ala.sds.generalise.MessageGeneralisation;
import au.org.ala.sds.ws.client.ALASDSServiceClient;
import au.org.ala.ws.ClientConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ALASensitiveDataServiceApplicationIT {
    private static ALASensitiveDataServiceApplication application;
    private ALASDSServiceClient client;

    @BeforeClass
    public static void setUpClass() throws Exception {
        application = new ALASensitiveDataServiceApplication();
        application.run("server", "./target/test-classes/config-it-1.yml");
    }

    @Before
    public void setUp() throws Exception {
        ClientConfiguration configuration = ClientConfiguration.builder().baseUrl(new URL("http://localhost:9189")).build();
        this.client = new ALASDSServiceClient(configuration);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        if (application != null) {
            application.shutdown();
        }
    }

    public static String readContent(URL source) throws IOException {
        try (InputStream is = source.openStream()) {
            Reader r = new InputStreamReader(is, "UTF-8");
            StringWriter writer = new StringWriter(1024);
            int n;
            char[] buffer = new char[1024];

            while ((n = r.read(buffer)) > 0) {
                writer.write(buffer, 0, n);
            }
            return writer.toString();
        }
    }

    public static List readList(URL source) throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(source, List.class);
    }

    public static String readResource(String resource) throws IOException {
        return readContent(ALASensitiveDataServiceApplicationIT.class.getResource(resource));
    }

    /**
     * Compare two strings, ignoring whitespace during the comparison
     *
     * @param expected The expected string
     * @param actual The actual string
     */
    public static void compareNoSpaces(String expected, String actual) {
        String expected1 = expected.replace('\n', ' ');
        expected1 = expected1.replaceAll("\\s+", " ").trim();
        String actual1 = actual.replace('\n', ' ');
        actual1 = actual.replaceAll("\\s+", " ").trim();
        try {
            Assert.assertEquals(expected1, actual1);
        } catch (ComparisonFailure fail) {
            throw new ComparisonFailure(fail.getMessage(), expected, actual);
        }
    }


    @Test
    public void testGetLayers1() throws Exception {
        URL req = new URL("http://localhost:9189/ws/layers");
        List result = readList(req);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof String);
        assertEquals("cl23", result.get(0));
    }

    @Test
    public void testGetZones1() throws Exception {
        URL req = new URL("http://localhost:9189/ws/zones");
        List result = readList(req);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Map);
        assertEquals("ACT", ((Map) result.get(0)).get("id"));
        assertEquals("Australian Capital Territory", ((Map) result.get(0)).get("layerId"));
    }

    @Test
    public void testGetCategories1() throws Exception {
        URL req = new URL("http://localhost:9189/ws/categories");
        List result = readList(req);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Map);
        assertEquals("High", ((Map) result.get(0)).get("id"));
        assertEquals("High Vulnerability", ((Map) result.get(0)).get("value"));
    }

    @Test
    public void testGetSpecies1() throws Exception {
        URL req = new URL("http://localhost:9189/ws/species");
        List result = readList(req);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Map);
        Map instance = (Map) result.get(0);
        assertEquals("Pterostylis oreophila", instance.get("scientificName"));
        assertEquals("species", instance.get("taxonRank"));
    }

    @Test
    public void testIsSensitive1() throws Exception {
        assertTrue(this.client.isSensitive("Pterostylis oreophila", null));
    }

    // In normal list but not in reduced list
    @Test
    public void testIsSensitive2() throws Exception {
        assertFalse(this.client.isSensitive("Bertmainius monachus", null));
    }

    // Not sensitive
    @Test
    public void testIsSensitive3() throws Exception {
        assertFalse(this.client.isSensitive("Acacia dealbata", null));
    }

    @Test
    public void testIsSensitive4() throws Exception {
        SpeciesCheck check = SpeciesCheck.builder().scientificName("Pterostylis oreophila").build();
        assertTrue(this.client.isSensitive(check));
    }

    @Test
    public void testGetSensitiveDataFields1() throws Exception {
       List<String> fields = this.client.getSensitiveDataFields();
       assertNotNull(fields);
       assertEquals(38, fields.size());
       assertEquals("http://rs.tdwg.org/dwc/terms/coordinateUncertaintyInMeters", fields.get(0));
    }

    @Test
    public void testGetGeneralisations1() throws Exception {
        List<Generalisation> generalisations = this.client.getGeneralisations();
        assertNotNull(generalisations);
        assertEquals(34, generalisations.size());
        assertEquals(MessageGeneralisation.class, generalisations.get(0).getClass());
    }

    @Test
    public void testGetReport1() throws Exception {
        SensitivityReport report = this.client.report("Pterostylis oreophila", null, null, "Australian Capital Territory", "Australia", null);
        assertNotNull(report);
        assertTrue(report.isSensitive());
    }


    @Test
    public void testGetReport2() throws Exception {
        SensitivityReport report = this.client.report("Pterostylis oreophila", null, null, "Victoria", "Australia", null);
        assertNotNull(report);
        assertFalse(report.isSensitive());
    }

    @Test
    public void testGetReport3() throws Exception {
        SensitivityReport report = this.client.report("Macropius rufus", null, null, "Victoria", "Australia", null);
        assertNotNull(report);
        assertFalse(report.isSensitive());
    }

}
