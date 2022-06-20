package au.org.ala.sds.ws;

import au.org.ala.sds.ws.core.SDSConfiguration;
import au.org.ala.sds.ws.resources.ConservationResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ALASensitiveDataServiceConfiguration extends Configuration {
    /** The swagger configuration */
    @Valid
    @NotNull
    @JsonProperty
    @Getter
    @Setter
    private SwaggerBundleConfiguration swagger = new SwaggerBundleConfiguration();
    /** The SDS configuration for conservation management */
    @Valid
    @NotNull
    @JsonProperty
    @Getter
    @Setter
    private SDSConfiguration conservation = new SDSConfiguration();

    /**
     * Construct with default setttings.
     */
    public ALASensitiveDataServiceConfiguration()  {
        this.swagger.setTitle("ALA Sensitive Data API");
        this.swagger.setDescription("A sensitive data service that maps taxon, location and date information into ");
        this.swagger.setContactUrl("https://ala.org.au");
        this.swagger.setContactEmail("support@ala.org.au");
        this.swagger.setResourcePackage(ConservationResource.class.getPackage().getName());
        this.swagger.setLicense("Mozilla Public Licence 1.1");
        this.swagger.setVersion("1.1.1-SNAPSHOT");
        this.swagger.getSwaggerViewConfiguration().setPageTitle("ALA Sensitive Data API");
    }
}
