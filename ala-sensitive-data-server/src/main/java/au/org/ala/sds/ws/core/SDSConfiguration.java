package au.org.ala.sds.ws.core;

import au.org.ala.sds.util.Configuration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Configuration for name search operations.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@EqualsAndHashCode
@Slf4j
public class SDSConfiguration {
    /** The path to the name matching index. Defaults to <code>/data/lucene/namematching</code> */
    @JsonProperty
    private String index = "/data/lucene/namematching";
    /** Source of SDS data (an XML file, by default read from the SDS server) */
    @JsonProperty
    private String speciesUrl = "https://sds.ala.org.au/sensitive-species-data.xml";
    /** Source of zone data (an XML file, by default read dfrom the SDS server) */
    @JsonProperty
    private String zonesUrl = "https://sds.ala.org.au/sensitivity-zones.xml";
    /** Source of zone data (an XML file, by default read from the SDS server) */
    @JsonProperty
    private String categoriesUrl = "https://sds.ala.org.au/sensitivity-categories.xml";
    /** A direct list of spatial layers */
    @JsonProperty
    private List<String> layers = null;
    /** Source of spatial layers, used if there isn't a specific list of layers (a JSON file) */
    @JsonProperty
    private String layersUrl = "https://sds.ala.org.au/ws/layers";
    /** The source of layer information */
    @JsonProperty
    private String layersServiceUrl = "https://spatial.ala.org.au/layers-service";
    /** The cache configuration */
    @JsonProperty
    private CacheConfiguration cache = new CacheConfiguration();

    /**
     * Configure the SDS system using the values contained in this configuration.
     */
    public void configureSds() {
        Configuration config = Configuration.getInstance();
        if (this.layers != null) {
            config.setSpatialLayers(this.layers);
        } else if (this.layersUrl != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                config.setSpatialLayers(mapper.readValue(new URL(this.layersUrl), List.class));
            } catch (Exception ex) {
                log.error("Unable to read layer URL " + this.layers, ex);
            }
        }
        config.setCategoriesUrl(this.categoriesUrl);
        config.setZoneUrl(this.zonesUrl);
        config.setSpeciesUrl(this.speciesUrl);
        config.setLayersServiceUrl(this.layersServiceUrl);
    }
}
