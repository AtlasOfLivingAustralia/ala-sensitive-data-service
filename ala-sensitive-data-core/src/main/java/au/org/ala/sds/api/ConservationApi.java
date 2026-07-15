package au.org.ala.sds.api;

import au.org.ala.sds.generalise.Generalisation;

import java.util.List;

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
    public List<String> getSensitiveDataFields();

    /**
     * Get the list of generalisations used for processing sensitive data.
     *
     * @return The list of generalisations
     */
    public List<Generalisation> getGeneralisations();

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
     * Provide a sensitivity report based on a taxon and zone.
     * <p>
     * Ths method only produces a sensitivity report on the supplied taxon/zones.
     * To generalise an occurrence record, the report must be applied to an occurrence
     * record using the generalisation rules.
     * </p>
     *
     * @param query The information from the occurrence record.
     *
     * @return A report giving whether the combination of taxon/area is sensitive
     *
     * @see #process(ProcessQuery)
     * @see #getGeneralisations()
     */
    public SensitivityReport report(SensitivityQuery query);

    /**
     * Provide a sensitivity report based on taxon and zone.
     *
     * @param scientificName The taxon scientific name
     * @param taxonId The taxon identifier
     * @param dataResourceUid The source data resource for the occurrecne record
     * @param stateProvince The state/province identifier
     * @param country The country identifier
     * @param zones The zones that this record is in
     *
     * @return A report giving the sensitivity status for that taxon/area
     */
    public SensitivityReport report(String scientificName, String taxonId, String dataResourceUid, String stateProvince, String country, List<String> zones);

    /**
     * Process an occurrence record and supply modified values.
     *
     * @param query The information from the occurrence record.
     *
     * @return A report giving what is sensitive and what values need to be altered
     *
     * @see #report(SensitivityQuery)
     */
    public SensitivityReport process(ProcessQuery query);
}
