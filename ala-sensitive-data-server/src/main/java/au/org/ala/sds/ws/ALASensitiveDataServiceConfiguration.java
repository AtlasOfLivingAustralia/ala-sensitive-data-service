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
import java.text.MessageFormat;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ALASensitiveDataServiceConfiguration extends Configuration {
    private static final ResourceBundle SWAGGER_MESSAGES = ResourceBundle.getBundle("messages");
    private static final String DEFAULT_VERSION = "UNKNOWN";

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
     * Construct with default settings.
     */
    public ALASensitiveDataServiceConfiguration()  {
        this.swagger.setTitle(SWAGGER_MESSAGES.getString("swagger.title"));
        this.swagger.setDescription(MessageFormat.format(
                SWAGGER_MESSAGES.getString("swagger.description"),
                SWAGGER_MESSAGES.getString("swagger.nameMatchingServiceUrl"),
                SWAGGER_MESSAGES.getString("swagger.moreInfoLinkUrl"),
                SWAGGER_MESSAGES.getString("swagger.apiDocsUrl")));
        this.swagger.setContact(SWAGGER_MESSAGES.getString("swagger.contactName"));
        this.swagger.setContactUrl(SWAGGER_MESSAGES.getString("swagger.contactUrl"));
        this.swagger.setContactEmail(SWAGGER_MESSAGES.getString("swagger.contactEmail"));
        this.swagger.setResourcePackage(ConservationResource.class.getPackage().getName());
        this.swagger.setLicense(SWAGGER_MESSAGES.getString("swagger.license"));
        this.swagger.setLicenseUrl(SWAGGER_MESSAGES.getString("swagger.licenseUrl"));
        this.swagger.setVersion(resolveVersion());
        this.swagger.getSwaggerViewConfiguration().setPageTitle(SWAGGER_MESSAGES.getString("swagger.pageTitle"));
    }

    /**
     * Resolve the application version from runtime metadata, preferring the package manifest and
     * then classpath Maven metadata, with a filesystem {@code pom.xml} fallback for local runs.
     * If no version source can be resolved, {@value #DEFAULT_VERSION} is returned.
     *
     * @return the application version string
     */
    private static String resolveVersion() {
        String implementationVersion = ALASensitiveDataServiceConfiguration.class.getPackage().getImplementationVersion();
        if (implementationVersion != null && !implementationVersion.trim().isEmpty()) {
            return implementationVersion;
        }

        String pomPropertiesVersion = readVersionFromPomProperties();
        if (pomPropertiesVersion != null && !pomPropertiesVersion.trim().isEmpty()) {
            return pomPropertiesVersion;
        }

        String pomVersion = readVersionFromPom();
        if (pomVersion != null && !pomVersion.trim().isEmpty()) {
            return pomVersion;
        }

        return DEFAULT_VERSION;
    }

    private static String readVersionFromPomProperties() {
        String[] resourcePaths = {
                "/META-INF/maven/au.org.ala.sds/ala-sensitive-data-server/pom.properties",
                "/META-INF/maven/au.org.ala.sds/ala-sensitive-data-service/pom.properties"
        };

        for (String resourcePath : resourcePaths) {
            try (InputStream inputStream = ALASensitiveDataServiceConfiguration.class.getResourceAsStream(resourcePath)) {
                if (inputStream == null) {
                    continue;
                }

                Properties properties = new Properties();
                properties.load(inputStream);
                String version = properties.getProperty("version");
                if (version != null && !version.trim().isEmpty()) {
                    return version;
                }
            } catch (IOException e) {
                // Keep searching other metadata sources.
            }
        }

        return null;
    }

    /**
     * Read the version from the nearest visible {@code pom.xml} on disk.
     *
     * @return the version string, or {@code null} when no pom can be found
     */
    private static String readVersionFromPom() {
        try {
            Path codeSourceLocation = Paths.get(
                    ALASensitiveDataServiceConfiguration.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            Path directory = Files.isDirectory(codeSourceLocation) ? codeSourceLocation : codeSourceLocation.getParent();

            while (directory != null) {
                Path pomFile = directory.resolve("pom.xml");
                if (Files.isRegularFile(pomFile)) {
                    String version = readVersionFromPomFile(pomFile);
                    if (version != null && !version.trim().isEmpty()) {
                        return version;
                    }
                }
                directory = directory.getParent();
            }
        } catch (URISyntaxException | IOException | ParserConfigurationException | SAXException | XPathExpressionException e) {
            return null;
        }

        return null;
    }

    private static String readVersionFromPomFile(Path pomFile)
            throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pomFile.toFile());
        String version = XPathFactory.newInstance().newXPath().evaluate("/project/version", document);
        if (version == null || version.trim().isEmpty()) {
            version = XPathFactory.newInstance().newXPath().evaluate("/project/parent/version", document);
        }
        return version;
    }
}
