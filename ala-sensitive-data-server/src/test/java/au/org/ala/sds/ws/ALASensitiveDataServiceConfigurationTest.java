package au.org.ala.sds.ws;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ALASensitiveDataServiceConfigurationTest {
    @Test
    public void setsSwaggerVersionFromBuildOrDefault() {
        ALASensitiveDataServiceConfiguration configuration = new ALASensitiveDataServiceConfiguration();

        assertEquals("1.0", configuration.getSwagger().getVersion());
    }
}
