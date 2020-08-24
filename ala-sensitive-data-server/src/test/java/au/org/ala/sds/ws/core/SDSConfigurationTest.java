package au.org.ala.sds.ws.core;

import au.org.ala.sds.util.Configuration;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SDSConfigurationTest {
    @Test
    public void testConfig1() throws Exception {
        SDSConfiguration configuration = new SDSConfiguration();
        configuration.configureSds();
        Configuration config = Configuration.getInstance();
        assertEquals("https://sds.ala.org.au/sensitive-species-data.xml", config.getSpeciesUrl());
        assertEquals("https://sds.ala.org.au/sensitivity-categories.xml", config.getCategoryUrl());
        assertEquals("https://sds.ala.org.au/sensitivity-zones.xml", config.getZoneUrl());
        assertNotNull(config.getGeospatialLayers());
        assertFalse(config.getGeospatialLayers().isEmpty());
        assertTrue(config.getGeospatialLayers().contains("cl22"));
    }

    @Test
    public void testConfig2() throws Exception {
        SDSConfiguration configuration = new SDSConfiguration();
        String speciesUrl = this.getClass().getResource("/sensitive-species-data-1.xml").toExternalForm();
        configuration.setSpeciesUrl(speciesUrl);
        configuration.configureSds();
        Configuration config = Configuration.getInstance();
        assertEquals(speciesUrl, config.getSpeciesUrl());
    }

    @Test
    public void testConfig3() throws Exception {
        SDSConfiguration configuration = new SDSConfiguration();
        String zonesUrl = this.getClass().getResource("/sensitivity-zones-1.xml").toExternalForm();
        configuration.setZonesUrl(zonesUrl);
        configuration.configureSds();
        Configuration config = Configuration.getInstance();
        assertEquals(zonesUrl, config.getZoneUrl());
    }

    @Test
    public void testConfig4() throws Exception {
        SDSConfiguration configuration = new SDSConfiguration();
        String categoriesUrl = this.getClass().getResource("/sensitivity-categories-1.xml").toExternalForm();
        configuration.setCategoriesUrl(categoriesUrl);
        configuration.configureSds();
        Configuration config = Configuration.getInstance();
        assertEquals(categoriesUrl, config.getCategoryUrl());
    }

    @Test
    public void testConfig5() throws Exception {
        SDSConfiguration configuration = new SDSConfiguration();
        String layersUrl = this.getClass().getResource("/layers-1.json").toExternalForm();
        configuration.setLayersUrl(layersUrl);
        configuration.configureSds();
        Configuration config = Configuration.getInstance();
        assertFalse(config.getGeospatialLayers().contains("cl22"));
        assertTrue(config.getGeospatialLayers().contains("cl23"));
    }

    @Test
    public void testConfig6() throws Exception {
        SDSConfiguration configuration = new SDSConfiguration();
        configuration.setLayers(Arrays.asList("cl23", "cl947"));
        configuration.configureSds();
        Configuration config = Configuration.getInstance();
        assertFalse(config.getGeospatialLayers().contains("cl22"));
        assertTrue(config.getGeospatialLayers().contains("cl23"));
        assertTrue(config.getGeospatialLayers().contains("cl947"));
    }

}
