package au.org.ala.sds.ws;

import org.junit.Test;

import static org.junit.Assert.*;

public class ALASensitiveDataServiceConfigurationTest {

    @Test
    public void testSwaggerConfiguration() throws Exception {
        ALASensitiveDataServiceConfiguration config = new ALASensitiveDataServiceConfiguration();
        assertNotNull(config.getSwagger());
        assertEquals("ALA Sensitive Data API", config.getSwagger().getTitle());
        assertNotNull(config.getSwagger().getSchemes());
        assertEquals(0, config.getSwagger().getSchemes().length);
        assertTrue(config.getSwagger().getDescription().contains("ALA sensitive data API"));
        assertEquals("Terms of Use", config.getSwagger().getLicense());
        assertEquals("https://www.ala.org.au/terms-of-use", config.getSwagger().getLicenseUrl());

        io.swagger.jaxrs.config.BeanConfig beanConfig = config.getSwagger().build("/api");
        assertNull(beanConfig.getSwagger().getSchemes());

        String json = io.swagger.util.Json.mapper().writeValueAsString(beanConfig.getSwagger());
        assertFalse("Swagger JSON should omit schemes to allow runtime protocol detection", json.contains("\"schemes\""));
    }
}
