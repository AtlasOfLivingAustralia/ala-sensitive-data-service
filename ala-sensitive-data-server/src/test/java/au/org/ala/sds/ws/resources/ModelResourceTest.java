package au.org.ala.sds.ws.resources;

import au.org.ala.sds.api.*;
import au.org.ala.sds.validation.FactCollection;
import au.org.ala.sds.ws.core.SDSConfiguration;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.Assert.*;

public class ModelResourceTest {
    private static SDSConfiguration configuration;
    private static ModelResource resource;

    // It takes a while to build the SDS
    @BeforeClass
    public static void setUpClass() throws Exception {
        ((Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.INFO); // Stop logging insanity
        configuration = new SDSConfiguration();
        configuration.setIndex("/data/lucene/namematching-20200214"); // Ensure consistent index
        configuration.setSpeciesUrl(ModelResourceTest.class.getResource("/sensitive-species-data-1.xml").toExternalForm());
        configuration.setZonesUrl(ModelResourceTest.class.getResource("/sensitivity-zones-1.xml").toExternalForm());
        configuration.setCategoriesUrl(ModelResourceTest.class.getResource("/sensitivity-categories-1.xml").toExternalForm());
        configuration.setLayersUrl(ModelResourceTest.class.getResource("/layers-1.json").toExternalForm());
        configuration.configureSds();
        resource = new ModelResource(configuration);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        configuration = null;
        resource = null;
    }

    @Test
    public void testGetLayers1() throws Exception {
        List<String> layers = resource.getLayers();
        assertNotNull(layers);
        assertFalse(layers.isEmpty());
        assertTrue(layers.contains("cl23"));
        assertFalse(layers.contains("cl22"));
    }

    @Test
    public void testGetZones1() throws Exception {
        List<SensitivityZone> zones = resource.getZones();
        assertNotNull(zones);
        assertFalse(zones.isEmpty());
        Optional<SensitivityZone> aus = zones.stream().filter(zone -> zone.getId().equals("AUS")).findFirst();
        assertTrue(aus.isPresent());
        assertEquals("Australia", aus.get().getName());
        assertEquals("COUNTRY", aus.get().getType());
    }

    @Test
    public void testGetCategories1() throws Exception {
        List<SensitivityCategory> categories = resource.getCategories();
        assertNotNull(categories);
        assertFalse(categories.isEmpty());
        Optional<SensitivityCategory> vu = categories.stream().filter(c -> c.getId().equals("VU")).findFirst();
        assertTrue(vu.isPresent());
        assertEquals("Vulnerable", vu.get().getValue());
        assertEquals("CONSERVATION", vu.get().getType());
    }

    @Test
    public void testGetSensitive1() throws Exception {
        List<SensitiveTaxon> taxa = resource.getSensitive();
        assertNotNull(taxa);
        assertFalse(taxa.isEmpty());
        Optional<SensitiveTaxon> croc = taxa.stream().filter(t -> t.getScientificName().equals("Crocodylus johnstoni")).findFirst();
        assertTrue(croc.isPresent());
        assertEquals("Crocodylus johnstoni", croc.get().getScientificName());
        assertEquals("Crocodylidae", croc.get().getFamily());
        assertNotNull(croc.get().getInstances());
        assertEquals(1, croc.get().getInstances().size());
    }

}
