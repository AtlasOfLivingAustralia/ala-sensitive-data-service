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
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

public class ALASensitiveDataServiceConfiguration extends Configuration {
    private static final ResourceBundle SWAGGER_MESSAGES = ResourceBundle.getBundle("messages");

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
        this.swagger.setTitle(SWAGGER_MESSAGES.getString("swagger.title"));
        this.swagger.setDescription(SWAGGER_MESSAGES.getString("swagger.description"));
        this.swagger.setContactUrl(SWAGGER_MESSAGES.getString("swagger.contactUrl"));
        this.swagger.setContactEmail(SWAGGER_MESSAGES.getString("swagger.contactEmail"));
        this.swagger.setResourcePackage(ConservationResource.class.getPackage().getName());
        this.swagger.setLicense(SWAGGER_MESSAGES.getString("swagger.license"));
        this.swagger.setVersion(resolveVersion());
        this.swagger.getSwaggerViewConfiguration().setPageTitle(SWAGGER_MESSAGES.getString("swagger.pageTitle"));
    }

    /**
     * Resolve the application version from the runtime package metadata, falling back to the
     * Maven-generated {@code pom.properties} resource when the manifest does not contain it.
     *
     * @return the application version string
     * @throws IllegalStateException if the version cannot be determined
     */
    private static String resolveVersion() {
        String implementationVersion = ALASensitiveDataServiceConfiguration.class.getPackage().getImplementationVersion();
        if (implementationVersion != null && !implementationVersion.trim().isEmpty()) {
            return implementationVersion;
        }

        Properties pomProperties = new Properties();
        try (InputStream inputStream = ALASensitiveDataServiceConfiguration.class.getResourceAsStream(
                "/META-INF/maven/au.org.ala.sds/ala-sensitive-data-service/pom.properties")) {
            if (inputStream != null) {
                pomProperties.load(inputStream);
                String version = pomProperties.getProperty("version");
                if (version != null && !version.trim().isEmpty()) {
                    return version;
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to determine application version", e);
        }

        throw new IllegalStateException("Unable to determine application version");
    }
}
