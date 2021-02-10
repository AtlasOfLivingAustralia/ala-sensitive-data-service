package au.org.ala.sds.generalise;

import org.gbif.dwc.terms.DwcTerm;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GeneralisationTypeIdResolverTest {
    @Test
    public void testGetNames1() throws Exception {
        List<String> names = GeneralisationTypeIdResolver.getNames();
        assertEquals(5, names.size());
        assertTrue(names.contains("clear"));
        assertTrue(names.contains("latLong"));
    }

    @Test
    public void testGetIdFromValue1() throws Exception {
        ClearGeneralisation generalisation = new ClearGeneralisation(DwcTerm.coordinateUncertaintyInMeters);
        GeneralisationTypeIdResolver resolver = new GeneralisationTypeIdResolver();
        assertEquals("clear", resolver.idFromValue(generalisation));
    }


    @Test
    public void testGetIdFromValue2() throws Exception {
        MessageGeneralisation generalisation = new MessageGeneralisation(DwcTerm.dataGeneralizations, "Hello there", true, MessageGeneralisation.Trigger.ANY);
        GeneralisationTypeIdResolver resolver = new GeneralisationTypeIdResolver();
        assertEquals("message", resolver.idFromValue(generalisation));
    }

    @Test
    public void testGetIdFromValue3() throws Exception {
        LatLongGeneralisation generalisation = new LatLongGeneralisation(DwcTerm.decimalLatitude, DwcTerm.decimalLongitude);
        GeneralisationTypeIdResolver resolver = new GeneralisationTypeIdResolver();
        assertEquals("latLong", resolver.idFromValue(generalisation));
    }

    @Test
    public void testGetTypeFromId1() throws Exception {
        GeneralisationTypeIdResolver resolver = new GeneralisationTypeIdResolver();
        assertEquals(ClearGeneralisation.class, resolver.typeFromId(null, "clear").getRawClass());
    }

    @Test
    public void testGetTypeFromId2() throws Exception {
        GeneralisationTypeIdResolver resolver = new GeneralisationTypeIdResolver();
        assertEquals(RetainGeneralisation.class, resolver.typeFromId(null, "retain").getRawClass());
    }

    @Test
    public void testGetTypeFromId3() throws Exception {
        GeneralisationTypeIdResolver resolver = new GeneralisationTypeIdResolver();
        assertEquals(LatLongGeneralisation.class, resolver.typeFromId(null, "latLong").getRawClass());
    }


    @Test
    public void testGetTypeFromId4() throws Exception {
        GeneralisationTypeIdResolver resolver = new GeneralisationTypeIdResolver();
        try {
            assertEquals(ClearGeneralisation.class, resolver.typeFromId(null, "bongoBongo").getRawClass());
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
        }
    }


}
