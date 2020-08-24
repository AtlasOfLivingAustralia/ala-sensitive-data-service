package au.org.ala.sds.ws.resources;

import au.org.ala.names.search.ALANameSearcher;
import au.org.ala.sds.SensitiveDataService;
import au.org.ala.sds.SensitiveSpeciesFinder;
import au.org.ala.sds.SensitiveSpeciesFinderFactory;
import au.org.ala.sds.api.ConservationApi;
import au.org.ala.sds.api.SensitivityQuery;
import au.org.ala.sds.api.SensitivityReport;
import au.org.ala.sds.api.SpeciesCheck;
import au.org.ala.sds.util.Configuration;
import au.org.ala.sds.validation.FactCollection;
import au.org.ala.sds.validation.ValidationOutcome;
import au.org.ala.sds.ws.core.SDSConfiguration;
import au.org.ala.sds.ws.health.Checkable;
import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.cache2k.Cache;
import org.cache2k.integration.CacheLoader;
import org.gbif.dwc.terms.DwcTerm;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Closeable;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * TODO add diagnostics to payload - similar to GBIF
 */
@Api(
    value = "Conservation status management"
)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api")
@Slf4j
@Singleton
public class ConservationResource implements ConservationApi, Closeable, Checkable {
    /** Name searcher */
    private final ALANameSearcher searcher;
    /** Find sensitive species */
    private final SensitiveSpeciesFinder finder;
    /** The data checker */
    private final SensitiveDataService sds;
    /** The API translator */
    private ApiTranslator translator;

    //Cache2k instance
    private final Cache<SpeciesCheck, Boolean> isSensitiveCache;

    public ConservationResource(SDSConfiguration configuration){
        try {
            log.info("Initialising ConservationResource.....");
            this.searcher = new ALANameSearcher(configuration.getIndex());
            this.finder = SensitiveSpeciesFinderFactory.getSensitiveSpeciesFinder(configuration.getSpeciesUrl(), this.searcher, true);
            this.sds = new SensitiveDataService();
            this.translator = new ApiTranslator();
            this.isSensitiveCache = configuration.getCache().builder(SpeciesCheck.class, Boolean.class)
                    .loader(new CacheLoader<SpeciesCheck, Boolean>() {
                        @Override
                        public Boolean load(SpeciesCheck check) throws Exception {
                            return doIsSensitive(check);
                        }
                    }) //auto populating function
                    .build();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            throw new RuntimeException("Unable to initialise searcher: " + e.getMessage(), e);
        }
    }

    /**
     * Make sure that the system is still operating.
     *
     * @return True if things can still be found, etc.
     */
    @Override
    public boolean check() {
        try {
            this.finder.findSensitiveSpecies("Animalia");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @ApiOperation(
        value = "Get a list of the fields that the SDS service uses and can manipulate",
        notes = "The list of occurrence, event, location and taxonomy fields that the SDS can alter or fuzz, depending on sensitivity rules."
    )
    @GET
    @Path("/sensitiveDataFields")
    public Set<String> getSensitiveDataFields() {
        Set<String> fields = new TreeSet<String>();
        Configuration config = Configuration.getInstance();
        fields.addAll(Arrays.asList(config.getFlagRules().split(",")));
        fields.addAll(Arrays.asList(FactCollection.FACT_NAMES));
        fields.add(DwcTerm.locationRemarks.simpleName());
        fields.add(DwcTerm.verbatimLatitude.simpleName());
        fields.add(DwcTerm.verbatimLongitude.simpleName());
        fields.add(DwcTerm.locality.simpleName());
        fields.add(DwcTerm.verbatimLocality.simpleName());
        fields.add(DwcTerm.verbatimCoordinates.simpleName());
        fields.add(DwcTerm.footprintWKT.simpleName());
        fields.add(DwcTerm.eventID.simpleName());
        fields.add(DwcTerm.eventDate.simpleName());
        fields.add(DwcTerm.day.simpleName());
        fields.add(DwcTerm.infraspecificEpithet.simpleName());
        return fields;
    }

    @ApiOperation(
            value = "Check to see if a species is potentially sensitive",
            notes = "Search based on a species name or taxon and see whether it is in the list of potentially sensitive species. " +
                    "Sensitive species declarations are based on geography; this method simply indicates whether a species might be classified as sensitive."
    )
    @POST
    @Timed
    @Path("/isSensitive")
    public boolean isSensitive(SpeciesCheck check) {
        try {
            return this.isSensitiveCache.get(check);
        } catch (Exception e){
            log.warn("Problem cheking species : " + e.getMessage() + " with specices: " + check);
            throw e;
        }
    }

    @ApiOperation(
        value = "Check to see if a species is potentially sensitive",
        notes = "Search based on a species name or taxon and see whether it is in the list of potentially sensitive species. " +
            "Sensitive species declarations are based on geography; this method simply indicates whether a species might be classified as sensitive."
    )
    @GET
    @Timed
    @Path("/isSensitive")
    public boolean isSensitive(
        @ApiParam(value = "The scientific name of the taxon", required = true, example = "Acacia dealbata") @QueryParam("scientificName") String scientificName,
        @ApiParam(value = "The taxonomc identifier for the taxon") @QueryParam("taxonId") String taxonId
    ) {
        SpeciesCheck check = SpeciesCheck.builder().scientificName(scientificName).taxonId(taxonId).build();
        return this.isSensitive(check);
    }

    /**
     * Search for a sensitive data instance in the underlying SDS library.
     *
     * @param check What to check
     *
     * @return True for possibly sensitive, false otherwise
     */
    protected boolean doIsSensitive(SpeciesCheck check) {
        return this.sds.isTaxonSensitive(this.finder, check.getScientificName(), check.getTaxonId());
    }

    @ApiOperation(
        value = "Process occurrence properties for a sensitive species",
        notes = "Applies the sensitivity processing rules for a sensitive species to properties from an occurrence record."
    )
    @POST
    @Path("/process")
    public SensitivityReport process(SensitivityQuery query) {
        ValidationOutcome outcome = this.sds.testMapDetails(
            this.finder,
            query.getProperties(),
            query.getScientificName(),
            query.getTaxonId()
        );
        SensitivityReport report = this.translator.buildSensitivityReport(outcome);
        return report;
    }

    /**
     * Close the resource.
     */
    @Override
    public void close()  {
    }
}
