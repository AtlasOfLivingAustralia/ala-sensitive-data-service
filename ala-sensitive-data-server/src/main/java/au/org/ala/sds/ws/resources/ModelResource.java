package au.org.ala.sds.ws.resources;

import au.org.ala.sds.api.SensitiveTaxon;
import au.org.ala.sds.api.SensitivityCategory;
import au.org.ala.sds.api.SensitivityZone;
import au.org.ala.sds.dao.SensitiveSpeciesXmlDao;
import au.org.ala.sds.dao.SensitivityCategoryXmlDao;
import au.org.ala.sds.dao.SensitivityZonesXmlDao;
import au.org.ala.sds.util.Configuration;
import au.org.ala.sds.ws.core.SDSConfiguration;
import au.org.ala.sds.ws.health.Checkable;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO add diagnostics to payload - similar to GBIF
 */
@Api(
    value = "Sensitive data information"
)
@Produces(MediaType.APPLICATION_JSON)
@Path("/ws")
@Slf4j
@Singleton
public class ModelResource implements Closeable, Checkable {
    /** Hold the zones in memory */
    List<SensitivityZone> zones;
    /** Hold the categories in memory */
    List<SensitivityCategory> categories;
    /** Hold the sensitive data in memory */
    List<SensitiveTaxon> sensitive;
    /** Hold the SDS layers in memory */
    List<String> layers;

    public ModelResource(SDSConfiguration configuration){
        InputStream zis = null, cis = null, sis = null;
        configuration.configureSds();
        Configuration sdsConfig = Configuration.getInstance();
        try {
            log.info("Initialising ModelResource.....");
            final ApiTranslator translator = new ApiTranslator(false);
            zis = new URL(sdsConfig.getZoneUrl()).openStream();
            this.zones = new SensitivityZonesXmlDao().getMap(zis).values().stream()
                .map(zone -> translator.buildSensitivityZone(zone))
                .collect(Collectors.toList());
            cis = new URL(sdsConfig.getCategoryUrl()).openStream();
            this.categories = new SensitivityCategoryXmlDao(cis).getMap().values().stream()
                .map(category -> translator.buildSensitivityCategory(category))
                .collect(Collectors.toList());
            sis = new URL(sdsConfig.getSpeciesUrl()).openStream();
            this.sensitive = new SensitiveSpeciesXmlDao(sis).getAll().stream()
                .map(taxon -> translator.buildSensitiveTaxon(taxon, taxon.getInstances()))
                .collect(Collectors.toList());
            this.layers = sdsConfig.getGeospatialLayers();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            throw new RuntimeException("Unable to initialise SDS: " + e.getMessage(), e);
        } finally {
            this.safeClose(zis);
            this.safeClose(cis);
            this.safeClose(sis);
        }
    }

    /** Provide error-swallowed close for streams */
    private void safeClose(Closeable closeable) {
        if (closeable == null)
            return;
        try {
            closeable.close();
        } catch (IOException ex) {
            log.error("Unable to close " + closeable, ex);
        }
    }

    /**
     * Make sure that the system is still operating.
     *
     * @return True if things can still be found, etc.
     */
    @Override
    public boolean check() {
        return this.sensitive != null && !this.sensitive.isEmpty();
    }

    @ApiOperation(
        value = "Get the geospatial layers used by the sensitive data service",
        notes = "Layers are referenced by id. See getZones for zones linking to layers."
    )
    @GET
    @Path("/layers")
    public List<String> getLayers() {
        return this.layers;
    }

    @ApiOperation(
        value = "Get the sensitive data zones",
        notes = "Each zone corresponds to a geospatial region, backed by a layer identifier"
    )
    @GET
    @Path("/zones")
     public List<SensitivityZone> getZones() {
        return this.zones;
    }


    @ApiOperation(
        value = "Get the sensitive data categories",
        notes = "Each category refers to a regulatory sensitivity category"
    )
    @GET
    @Path("/categories")
    public List<SensitivityCategory> getCategories() {
        return this.categories;
    }


    @ApiOperation(
        value = "Get the sensitive data species",
        notes = "The list of species that are sensitive, along with the specific zone/category sensitivity instances."
    )
    @GET
    @Path("/species")
    public List<SensitiveTaxon> getSensitive() {
        return this.sensitive;
    }


    /**
     * Close the resource.
     */
    @Override
    public void close()  {
    }
}
