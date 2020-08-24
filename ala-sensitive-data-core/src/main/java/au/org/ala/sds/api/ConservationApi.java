package au.org.ala.sds.api;

import java.util.Set;

/**
 * Conservation status API.
 *
 * Clients implement this interface to provide a lookup for conservation information.
 */
public interface ConservationApi {
    /**
     * Get the list of fields that should be provided for sensitivity checking.
     *
     * @return The list of potentially sensitive fields
     */
    public Set<String> getSensitiveDataFields();

    /**
     * Test to see whether a taxon is potentially sensitive
     *
     * @param check The species check data
     *
     * @return True if this species is sensitive in some location
     */
    public boolean isSensitive(SpeciesCheck check);

    /**
     * Test to see whether a taxon is potentially sensitive
     *
     * @param scientificName The scientific name
     * @param taxonId The taxon identifier
     *
     * @return True if this species is sensitive in some location
     */
    public boolean isSensitive(String scientificName, String taxonId);

    /**
     * Process an occurrence record and supply modified values.
     *
     * @param query The information from the occurrence record.
     *
     * @return A report giving what is sensitive and what values need to be altered
     */
    public SensitivityReport process(SensitivityQuery query);
}
