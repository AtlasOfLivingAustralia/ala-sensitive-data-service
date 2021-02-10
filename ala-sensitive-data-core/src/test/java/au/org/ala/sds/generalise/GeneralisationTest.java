package au.org.ala.sds.generalise;

import au.org.ala.sds.api.*;
import org.gbif.dwc.terms.DwcTerm;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GeneralisationTest {

    @Test
    public void testQueryProcess1() throws Exception {
        String localityName = DwcTerm.locality.qualifiedName();
        String longitudeName = DwcTerm.decimalLongitude.qualifiedName();
        Generalisation generalisation = new ClearGeneralisation(DwcTerm.locality);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(localityName, "Somewhere else");
        supplied.put(longitudeName, "145.786");
        ProcessQuery query = ProcessQuery.builder()
            .properties(supplied)
            .scientificName("Acacia filifolia")
            .build();
        SensitivityInstance instance = SensitivityInstance.builder().build();
        SensitiveTaxon taxon = SensitiveTaxon.builder()
            .taxonRank("species")
            .scientificName("Acacia filifolia")
            .instances(Arrays.asList(instance))
            .build();
        ValidationReport validation = ValidationReport.builder()
            .taxon(taxon)
            .build();
        SensitivityReport report = SensitivityReport.builder()
            .sensitive(true)
            .loadable(true)
            .valid(true)
            .original(original)
            .updated(updated)
            .report(validation)
            .build();
        generalisation.process(query, report);
        assertEquals(1, original.size());
        assertEquals("Somewhere else", original.get(localityName));
        assertEquals(1, updated.size());
        assertTrue(updated.containsKey(localityName));
        assertNull(updated.get(localityName));
    }


    @Test
    public void testQueryProcess2() throws Exception {
        String localityName = DwcTerm.locality.qualifiedName();
        String longitudeName = DwcTerm.decimalLongitude.qualifiedName();
        Generalisation generalisation = new ClearGeneralisation(DwcTerm.locality);
        Map<String, String> supplied = new HashMap<>();
        Map<String, Object> original = new HashMap<>();
        Map<String, Object> updated = new HashMap<>();
        supplied.put(localityName, "Somewhere else");
        supplied.put(longitudeName, "145.786");
        ProcessQuery query = ProcessQuery.builder()
            .properties(supplied)
            .scientificName("Acacia filifolia")
            .build();
        SensitivityInstance instance = SensitivityInstance.builder().build();
        SensitiveTaxon taxon = SensitiveTaxon.builder()
            .taxonRank("species")
            .scientificName("Acacia filifolia")
            .instances(Arrays.asList(instance))
            .build();
        ValidationReport validation = ValidationReport.builder()
            .taxon(taxon)
            .build();
        SensitivityReport report = SensitivityReport.builder()
            .sensitive(false)
            .loadable(true)
            .valid(true)
            .original(original)
            .updated(updated)
            .report(validation)
            .build();
        generalisation.process(query, report);
        assertEquals(0, original.size());
        assertEquals(0, updated.size());
    }

}
