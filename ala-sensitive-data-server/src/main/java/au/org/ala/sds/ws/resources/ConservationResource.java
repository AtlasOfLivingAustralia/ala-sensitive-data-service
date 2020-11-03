package au.org.ala.sds.ws.resources;

import au.org.ala.names.search.ALANameSearcher;
import au.org.ala.sds.SensitiveDataService;
import au.org.ala.sds.SensitiveSpeciesFinder;
import au.org.ala.sds.SensitiveSpeciesFinderFactory;
import au.org.ala.sds.api.ConservationApi;
import au.org.ala.sds.api.SensitivityQuery;
import au.org.ala.sds.api.SensitivityReport;
import au.org.ala.sds.api.SpeciesCheck;
import au.org.ala.sds.generalise.FieldAccessor;
import au.org.ala.sds.generalise.Generalisation;
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
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.TermFactory;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Closeable;
import java.util.*;
import java.util.stream.Collectors;

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
    public static final Set<Term> PROPERTIES_TO_REMOVE = new HashSet<Term>(Arrays.asList(
        TermFactory.instance().findTerm("northing"),
        TermFactory.instance().findTerm("easting"),
        DwcTerm.locationRemarks,
        DwcTerm.verbatimLatitude,
        DwcTerm.verbatimLongitude,
        DwcTerm.locality,
        DwcTerm.verbatimLocality,
        DwcTerm.verbatimCoordinates,
        TermFactory.instance().findTerm("gridReference"),
        DwcTerm.footprintWKT,
        DwcTerm.coordinateUncertaintyInMeters,
        TermFactory.instance().findTerm("bbox"),

        DwcTerm.day,
        DwcTerm.month,
        DwcTerm.eventID,
        DwcTerm.eventDate,
        DwcTerm.eventTime,
        TermFactory.instance().findTerm("eventDateEnd"),
        DwcTerm.verbatimEventDate
    ));

    public static final Set<Term> PROPERTIES_TO_REQUEST = new HashSet<Term>(Arrays.asList(
        DwcTerm.informationWithheld,
        DwcTerm.dataGeneralizations,

        DwcTerm.decimalLatitude,
        DwcTerm.decimalLongitude,
        DwcTerm.verbatimLatitude,
        DwcTerm.verbatimLongitude,
        DwcTerm.verbatimCoordinates,
        DwcTerm.locality,
        DwcTerm.verbatimLocality,
        DwcTerm.municipality,
        DwcTerm.stateProvince,
        DwcTerm.country,
        DwcTerm.locationRemarks,
        TermFactory.instance().findTerm("gridReference"),
        DwcTerm.footprintWKT,
        DwcTerm.coordinateUncertaintyInMeters,
        TermFactory.instance().findTerm("northing"),
        TermFactory.instance().findTerm("easting"),
        TermFactory.instance().findTerm("bbox"),

        DwcTerm.eventID,
        DwcTerm.eventDate,
        DwcTerm.eventTime,
        TermFactory.instance().findTerm("eventDateEnd"),
        DwcTerm.verbatimEventDate,
        DwcTerm.day,
        DwcTerm.month,
        DwcTerm.year,

        DwcTerm.scientificName,
        DwcTerm.family,
        DwcTerm.genus,
        DwcTerm.specificEpithet,
        DwcTerm.infraspecificEpithet,
        TermFactory.instance().findTerm("intraspecificEpithet") // Misspelling
    ));
    
    /** Name searcher */
    private final ALANameSearcher searcher;
    /** Find sensitive species */
    private final SensitiveSpeciesFinder finder;
    /** The data checker */
    private final SensitiveDataService sds;
    /** The API translator */
    private ApiTranslator translator;
    /** The list of generalisations to apply */
    private List<Generalisation> generalisations;
    /** The list of terms to request */
    private Set<Term> requestTerms;

    //Cache2k instance
    private final Cache<SpeciesCheck, Boolean> isSensitiveCache;

    public ConservationResource(SDSConfiguration configuration){
        try {
            log.info("Initialising ConservationResource.....");
            this.searcher = new ALANameSearcher(configuration.getIndex());
            this.finder = SensitiveSpeciesFinderFactory.getSensitiveSpeciesFinder(configuration.getSpeciesUrl(), this.searcher, true);
            this.sds = new SensitiveDataService();
            this.translator = new ApiTranslator(false);
            this.generalisations = configuration.getGeneralisations();
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
        this.requestTerms = this.buildRequestTerms();
     }

    /**
     * Build the list of terms that might be needed.
     *
     * @return A set of terms that get used somewhere
     */
    protected Set<Term> buildRequestTerms() {
         TermFactory factory = TermFactory.instance();
         Set<Term> fields = new HashSet<>();
         Configuration config = Configuration.getInstance();
         for (String flag: config.getFlagRules().split(",")) {
             fields.add(factory.findTerm(flag));
         }
         for (Generalisation generalisation: this.generalisations)
             fields.addAll(generalisation.getFields().stream().map(FieldAccessor::getField).collect(Collectors.toSet()));
         for (String fact: FactCollection.FACT_NAMES) {
             fields.add(factory.findTerm(fact));
         }
         return fields;
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
        value = "Get a list of the fields that the SDS service uses and can manipulate, as URIs",
        notes = "The list of occurrence, event, location and taxonomy fields that the SDS can alter or fuzz, depending on sensitivity rules."
    )
    @GET
    @Path("/sensitiveDataFields")
    public List<String> getSensitiveDataFields() {
        return this.requestTerms.stream().map(Term::qualifiedName).sorted().collect(Collectors.toList());
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
        Map<String, String> properties = new HashMap<>(query.getProperties());
        // Ensure key facts are present in the way expected
        for (String fact: FactCollection.FACT_NAMES) {
            if (properties.containsKey(fact))
                continue;
            Term term = TermFactory.instance().findTerm(fact);
            if (properties.containsKey(term.qualifiedName())) {
                properties.put(fact, properties.get(term.qualifiedName()));
                continue;
            }
            if (properties.containsKey(term.prefixedName())) {
                properties.put(fact, properties.get(term.prefixedName()));
            }
        }
        ValidationOutcome outcome = this.sds.testMapDetails(
            this.finder,
            properties,
            query.getScientificName(),
            query.getTaxonId()
        );
        this.amendOutcome(query, outcome);
        SensitivityReport report = this.translator.buildSensitivityReport(outcome);
        for (Generalisation generalisation: this.generalisations) {
            generalisation.process(query, report);
        }
        return report;
    }

    /**
     * Close the resource.
     */
    @Override
    public void close()  {
    }

    /**
     * Make additional amendments to the outcome not covered by
     * the SDS library
     * @param outcome
     */
    private void amendOutcome(SensitivityQuery query, ValidationOutcome outcome) {
        if (!outcome.isSensitive())
            return;
        Map<String, String> original = query.getProperties();

    }
}
