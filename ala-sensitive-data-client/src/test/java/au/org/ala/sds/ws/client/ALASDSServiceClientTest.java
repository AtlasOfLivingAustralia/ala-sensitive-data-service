package au.org.ala.sds.ws.client;

import au.org.ala.sds.api.*;
import au.org.ala.util.TestUtils;
import au.org.ala.ws.ClientConfiguration;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ALASDSServiceClientTest extends TestUtils {
    private MockWebServer server;
    private ClientConfiguration configuration;
    private ALASDSServiceClient client;

    @Before
    public void setUp() throws Exception {
        this.server = new MockWebServer();
        server.start();

        this.configuration = ClientConfiguration.builder().baseUrl(server.url("").url()).build();
        this.client = new ALASDSServiceClient(configuration);
    }

    @After
    public void tearDown() throws Exception {
        this.server.shutdown();
        this.client.close();
    }


    /** Sensitive data fields list */
    @Test
    public void testGetSensitiveFields1() throws Exception {
        String response = this.getResource("response-gsdf-1.json");

        server.enqueue(new MockResponse().setBody(response));
        List<String> fields = client.getSensitiveDataFields();

        assertNotNull(fields);
        assertTrue(fields.contains("decimalLatitude"));
        assertTrue(fields.contains("eventDate"));
        assertEquals(1, server.getRequestCount());
        RecordedRequest req = server.takeRequest();
        assertEquals("GET", req.getMethod());
        assertEquals("/api/sensitiveDataFields", req.getPath());
    }

    /** Sensitive check */
    @Test
    public void testIsSensitiveCheck1() throws Exception {
        String request = this.getResource("request-is-1.json");
        String response = this.getResource("response-is-1.json");

        server.enqueue(new MockResponse().setBody(response));
        SpeciesCheck check = SpeciesCheck.builder().scientificName("Acacia dealbata").build();
        Boolean sensitive = client.isSensitive(check);

        assertNotNull(sensitive);
        assertFalse(sensitive);
        assertEquals(1, server.getRequestCount());
        RecordedRequest req = server.takeRequest();
        assertEquals("POST", req.getMethod());
        assertEquals("/api/isSensitive", req.getPath());
        assertEquals(request, req.getBody().readUtf8());
    }


    /** Sensitive check */
    @Test
    public void testIsSensitiveCheck2() throws Exception {
        String request = this.getResource("request-is-2.json");
        String response = this.getResource("response-is-2.json");

        server.enqueue(new MockResponse().setBody(response));
        SpeciesCheck check = SpeciesCheck.builder()
            .scientificName("Varanus semiremex")
            .taxonId("urn:lsid:biodiversity.org.au:afd.taxon:81244b7e-7145-42fe-b28c-3d672310e739")
            .build();
        Boolean sensitive = client.isSensitive(check);

        assertNotNull(sensitive);
        assertTrue(sensitive);
        assertEquals(1, server.getRequestCount());
        RecordedRequest req = server.takeRequest();
        assertEquals("POST", req.getMethod());
        assertEquals("/api/isSensitive", req.getPath());
        assertEquals(request, req.getBody().readUtf8());
    }


    /** Sensitive check */
    @Test
    public void testIsSensitiveParams1() throws Exception {
        String response = this.getResource("response-is-2.json");

        server.enqueue(new MockResponse().setBody(response));
        Boolean sensitive = client.isSensitive(
            "Varanus semiremex",
            "urn:lsid:biodiversity.org.au:afd.taxon:81244b7e-7145-42fe-b28c-3d672310e739"
        );
        assertNotNull(sensitive);
        assertTrue(sensitive);
        assertEquals(1, server.getRequestCount());
        RecordedRequest req = server.takeRequest();
        assertEquals("GET", req.getMethod());
        assertEquals("/api/isSensitive?scientificName=Varanus%20semiremex&taxonId=urn%3Alsid%3Abiodiversity.org.au%3Aafd.taxon%3A81244b7e-7145-42fe-b28c-3d672310e739", req.getPath());
    }

    @Test
    public void testReport1() throws Exception {
        String request = this.getResource("request-r-1.json");
        String response = this.getResource("response-r-1.json");

        server.enqueue(new MockResponse().setBody(response));
        SensitivityQuery query = SensitivityQuery.builder()
            .scientificName("Varanus semiremex")
            .taxonId("urn:lsid:biodiversity.org.au:afd.taxon:81244b7e-7145-42fe-b28c-3d672310e739")
            .stateProvince("Queensland")
            .country("Australia")
            .build();
        SensitivityReport report = client.report(query);
        assertNotNull(report);
        assertTrue(report.isValid());
        assertTrue(report.isSensitive());
        assertTrue(report.isLoadable());
        assertFalse(report.isAccessControl());
        List<SensitivityInstance> instances = report.getInstances();
        assertNotNull(instances);
        assertEquals(1, instances.size());
        assertEquals("Queensland", instances.get(0).getZone().getName());
        ValidationReport vr = report.getReport();
        assertNotNull(vr);
        SensitiveTaxon taxon = vr.getTaxon();
        assertNotNull(taxon);
        assertEquals("Varanus semiremex", taxon.getScientificName());
        assertEquals("urn:lsid:biodiversity.org.au:afd.taxon:81244b7e-7145-42fe-b28c-3d672310e739", taxon.getTaxonId());
        assertEquals("rusty monitor", taxon.getCommonName());
        assertNotNull(taxon.getInstances());
        assertEquals(1, taxon.getInstances().size());
        SensitivityInstance instance = taxon.getInstances().get(0);
        assertEquals("Qld DEHP", instance.getAuthority());
        assertNotNull(instance.getCategory());
        assertEquals("Sensitive", instance.getCategory().getId());
        assertEquals("dr493", instance.getDataResourceId());
        assertNotNull(instance.getZone());
        assertEquals("QLD", instance.getZone().getId());
        assertEquals(1, server.getRequestCount());
        RecordedRequest req = server.takeRequest();
        assertEquals("POST", req.getMethod());
        assertEquals("/api/report", req.getPath());
        assertEquals(request, req.getBody().readUtf8());
    }

    @Test
    public void testReport2() throws Exception {
        String response = this.getResource("response-r-1.json");
        Map<String, String> properties = new HashMap<>();

        server.enqueue(new MockResponse().setBody(response));
        SensitivityReport report = client.report(
            "Varanus semiremex",
            "urn:lsid:biodiversity.org.au:afd.taxon:81244b7e-7145-42fe-b28c-3d672310e739",
            "dr6567",
            "Queensland",
            "Australia",
            Arrays.asList("FFEZ")
        );
        assertNotNull(report);
        assertTrue(report.isValid());
        assertTrue(report.isSensitive());
        assertTrue(report.isLoadable());
        assertFalse(report.isAccessControl());
        List<SensitivityInstance> instances = report.getInstances();
        assertNotNull(instances);
        assertEquals(1, instances.size());
        assertEquals("QLD", instances.get(0).getZone().getId());
        ValidationReport vr = report.getReport();
        assertNotNull(vr);
        SensitiveTaxon taxon = vr.getTaxon();
        assertNotNull(taxon);
        assertEquals("Varanus semiremex", taxon.getScientificName());
        assertEquals("urn:lsid:biodiversity.org.au:afd.taxon:81244b7e-7145-42fe-b28c-3d672310e739", taxon.getTaxonId());
        assertEquals("rusty monitor", taxon.getCommonName());
        assertNotNull(taxon.getInstances());
        assertEquals(1, taxon.getInstances().size());
        SensitivityInstance instance = taxon.getInstances().get(0);
        assertEquals("Qld DEHP", instance.getAuthority());
        assertNotNull(instance.getCategory());
        assertEquals("Sensitive", instance.getCategory().getId());
        assertEquals("dr493", instance.getDataResourceId());
        assertNotNull(instance.getZone());
        assertEquals("QLD", instance.getZone().getId());
        assertEquals(1, server.getRequestCount());
        RecordedRequest req = server.takeRequest();
        assertEquals("GET", req.getMethod());
        assertEquals("/api/report", req.getRequestUrl().encodedPath());
        assertEquals("FFEZ", req.getRequestUrl().queryParameter("zone"));
        assertEquals("Queensland", req.getRequestUrl().queryParameter("stateProvince"));
        assertEquals("Australia", req.getRequestUrl().queryParameter("country"));
    }


    @Test
    public void testProcess1() throws Exception {
        String request = this.getResource("request-p-1.json");
        String response = this.getResource("response-p-1.json");
        Map<String, String> properties = new HashMap<>();

        properties.put("decimalLatitude", "-27.327739");
        properties.put("decimalLongitude", "152.527914");
        properties.put("eventDate", "2020-01-02");
        server.enqueue(new MockResponse().setBody(response));
        ProcessQuery query = ProcessQuery.builder()
            .scientificName("Varanus semiremex")
            .taxonId("urn:lsid:biodiversity.org.au:afd.taxon:81244b7e-7145-42fe-b28c-3d672310e739")
            .properties(properties)
            .build();
        SensitivityReport report = client.process(query);

        assertNotNull(report);
        assertTrue(report.isValid());
        assertTrue(report.isSensitive());
        assertTrue(report.isLoadable());
        assertFalse(report.isAccessControl());
        List<SensitivityInstance> instances = report.getInstances();
        assertNotNull(instances);
        assertEquals(1, instances.size());
        assertEquals("QLD", instances.get(0).getZone().getId());
        ValidationReport vr = report.getReport();
        assertNotNull(vr);
        SensitiveTaxon taxon = vr.getTaxon();
        assertNotNull(taxon);
        assertEquals("Varanus semiremex", taxon.getScientificName());
        assertEquals("urn:lsid:biodiversity.org.au:afd.taxon:81244b7e-7145-42fe-b28c-3d672310e739", taxon.getTaxonId());
        assertEquals("rusty monitor", taxon.getCommonName());
        assertNotNull(taxon.getInstances());
        assertEquals(1, taxon.getInstances().size());
        SensitivityInstance instance = taxon.getInstances().get(0);
        assertEquals("Qld DEHP", instance.getAuthority());
        assertNotNull(instance.getCategory());
        assertEquals("Sensitive", instance.getCategory().getId());
        assertEquals("dr493", instance.getDataResourceId());
        assertNotNull(instance.getZone());
        assertEquals("QLD", instance.getZone().getId());
        Map<String, Object> result = report.getUpdated();
        assertNotNull(result);
        assertEquals("152.5", result.get("decimalLongitude"));
        assertEquals("-27.3", result.get("decimalLatitude"));
        assertEquals("10000", result.get("generalisationInMetres"));
        assertEquals(1, server.getRequestCount());
        RecordedRequest req = server.takeRequest();
        assertEquals("POST", req.getMethod());
        assertEquals("/api/process", req.getPath());
        assertEquals(request, req.getBody().readUtf8());
    }

}
