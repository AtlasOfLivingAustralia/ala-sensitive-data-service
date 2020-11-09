package au.org.ala.sds.api;

import au.org.ala.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class GeneralisationRuleTest extends TestUtils {
    @Test
    public void testConstruct1() throws Exception {
        GeneralisationRule rule = new GeneralisationRule("100m");
        assertEquals("100m", rule.getGeneralisation());
        assertEquals(100, rule.getGeneralisationInMetres());
        assertEquals(0.01, rule.getPrecision(), 0.0001);
    }

    @Test
    public void testConstruct2() throws Exception {
        GeneralisationRule rule = new GeneralisationRule("2km");
        assertEquals("2km", rule.getGeneralisation());
        assertEquals(2000, rule.getGeneralisationInMetres());
        assertEquals(0.1, rule.getPrecision(), 0.0001);
    }

    @Test
    public void testConstruct3() throws Exception {
        GeneralisationRule rule = new GeneralisationRule("50km");
        assertEquals("50km", rule.getGeneralisation());
        assertEquals(50000, rule.getGeneralisationInMetres());
        assertEquals(1, rule.getPrecision(), 0.0001);
    }

    @Test
    public void testConstruct4() throws Exception {
        try {
            GeneralisationRule rule = new GeneralisationRule("50");
            fail("Expecting illegal argument exception");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testConstruct5() throws Exception {
        try {
            GeneralisationRule rule = new GeneralisationRule("70cm");
            fail("Expecting illegal argument exception");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testToJson1() throws Exception {
        GeneralisationRule rule = new GeneralisationRule("5km");
        ObjectMapper mapper = this.getObjectMapper();
        String json = mapper.writeValueAsString(rule);
        compareToResource("generalisation-rule-1.json", json);
    }

    @Test
    public void testFromJson1() throws Exception {
        ObjectMapper mapper = this.getObjectMapper();
        GeneralisationRule rule = mapper.readValue(this.getResource("generalisation-rule-1.json"), GeneralisationRule.class);
        assertEquals("5km", rule.getGeneralisation());
        assertEquals(5000, rule.getGeneralisationInMetres());
        assertEquals(0.1, rule.getPrecision(), 0.0001);
    }

    @Test
    public void testGeneraliseLatitude1() throws Exception {
        GeneralisationRule rule = new GeneralisationRule("100m");
        assertEquals(21.12, rule.generaliseLatitude(21.12345, 145.78643), 0.0001);
        assertEquals(-21.12, rule.generaliseLatitude(-21.12345, 145.78643), 0.0001);
        assertEquals(-45.68, rule.generaliseLatitude(-45.67843, 145.78643), 0.0001);
    }

    @Test
    public void testGeneraliseLatitude2() throws Exception {
        GeneralisationRule rule = new GeneralisationRule("10km");
        assertEquals(21.1, rule.generaliseLatitude(21.12345, 145.78643), 0.0001);
        assertEquals(-21.1, rule.generaliseLatitude(-21.12345, 145.78643), 0.0001);
        assertEquals(-45.7, rule.generaliseLatitude(-45.67843, 145.78643), 0.0001);
    }


    @Test
    public void testGeneraliseLatitude3() throws Exception {
        GeneralisationRule rule = new GeneralisationRule("100km");
        assertEquals(21, rule.generaliseLatitude(21.12345, 145.78643), 0.0001);
        assertEquals(-21, rule.generaliseLatitude(-21.12345, 145.78643), 0.0001);
        assertEquals(-46, rule.generaliseLatitude(-45.67843, 145.78643), 0.0001);
    }


    @Test
    public void testGeneraliseLatitude4() throws Exception {
        GeneralisationRule rule = new GeneralisationRule("500km");
        assertEquals(22, rule.generaliseLatitude(21.12345, 145.78643), 0.0001);
        assertEquals(-22, rule.generaliseLatitude(-21.12345, 145.78643), 0.0001);
        assertEquals(-45, rule.generaliseLatitude(-45.67843, 145.78643), 0.0001);
    }


    @Test
    public void testGeneraliseLongitude1() throws Exception {
        GeneralisationRule rule = new GeneralisationRule("100m");
        assertEquals(145.79, rule.generaliseLongitude(21.12345, 145.78643), 0.0001);
        assertEquals(-145.79, rule.generaliseLongitude(-21.12345, -145.78643), 0.0001);
        assertEquals(145.24, rule.generaliseLongitude(-45.67843, 145.24321), 0.0001);
        assertEquals(145.23, rule.generaliseLongitude(88.5, 145.24321), 0.0001);
    }

    @Test
    public void testGeneraliseLongitude2() throws Exception {
        GeneralisationRule rule = new GeneralisationRule("10km");
        assertEquals(145.8, rule.generaliseLongitude(21.12345, 145.78643), 0.0001);
        assertEquals(-145.8, rule.generaliseLongitude(-21.12345, -145.78643), 0.0001);
        assertEquals(145.3, rule.generaliseLongitude(-45.67843, 145.24321), 0.0001);
        assertEquals(144.4, rule.generaliseLongitude(88.5, 145.24321), 0.0001);
    }


    @Test
    public void testGeneraliseLongitude3() throws Exception {
        GeneralisationRule rule = new GeneralisationRule("100km");
        assertEquals(146, rule.generaliseLongitude(21.12345, 145.78643), 0.0001);
        assertEquals(-146, rule.generaliseLongitude(-21.12345, -145.78643), 0.0001);
        assertEquals(146, rule.generaliseLongitude(-45.67843, 145.24321), 0.0001);
        assertEquals(137, rule.generaliseLongitude(88.5, 145.24321), 0.0001);
    }


    @Test
    public void testGeneraliseLongitude4() throws Exception {
        GeneralisationRule rule = new GeneralisationRule("500km");
        assertEquals(145, rule.generaliseLongitude(21.12345, 145.78643), 0.0001);
        assertEquals(-145, rule.generaliseLongitude(-21.12345, -145.78643), 0.0001);
        assertEquals(148, rule.generaliseLongitude(-45.67843, 145.24321), 0.0001);
        assertEquals(172, rule.generaliseLongitude(88.5, 145.24321), 0.0001);
    }


    @Test
    public void testFormat1() throws Exception {
        GeneralisationRule rule = new GeneralisationRule("200m");
        assertEquals("21.56", rule.format(21.555555));
        assertEquals("-21.56", rule.format(-21.555555));
        assertEquals("143.74", rule.format(143.74366));
        assertEquals("0.00", rule.format(0));
    }

    @Test
    public void testFormat2() throws Exception {
        GeneralisationRule rule = new GeneralisationRule("10km");
        assertEquals("21.6", rule.format(21.555555));
        assertEquals("-21.6", rule.format(-21.555555));
        assertEquals("143.7", rule.format(143.74366));
        assertEquals("0.0", rule.format(0));
    }

    @Test
    public void testFormat3() throws Exception {
        GeneralisationRule rule = new GeneralisationRule("100km");
        assertEquals("22", rule.format(21.555555));
        assertEquals("-22", rule.format(-21.555555));
        assertEquals("144", rule.format(143.74366));
        assertEquals("0", rule.format(0));
    }

}
