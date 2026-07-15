package au.org.ala.sds.ws.client;

import au.org.ala.sds.api.*;
import au.org.ala.sds.generalise.Generalisation;
import au.org.ala.ws.ClientConfiguration;
import au.org.ala.ws.ClientException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
public class ALASDSServiceClient implements ConservationApi, Closeable {

    //Wrapped service
    private final ALASDSRetrofitService alaSdsService;

    private final OkHttpClient okHttpClient;

    /**
     * Creates an instance using the provided configuration settings.
     * 
     * @param configuration Rest client configuration
     *                      
     * @throws IOException if unable to build underlying services
     */
    public ALASDSServiceClient(ClientConfiguration configuration) throws IOException {
        this.okHttpClient = configuration.createClient();
        this.alaSdsService = configuration.createRetrofitClient(this.okHttpClient, ALASDSRetrofitService.class);
    }

    /**
     * Get the list of fields that should be provided for sensitivity checking.
     *
     * @return The list of potentially sensitive fields
     */
    @Override
    public List<String> getSensitiveDataFields() {
        return this.call(this.alaSdsService.getSensitiveDataFields());
    }


    /**
     * Get the list of fields that should be provided for sensitivity checking.
     *
     * @return The list of potentially sensitive fields
     */
    @Override
    public List<Generalisation> getGeneralisations() {
        return this.call(this.alaSdsService.getGeneralisations());
    }

    /**
     * Test to see whether a taxon is potentially sensitive
     *
     * @param check The species check data
     * @return True if this species is sensitive in some location
     */
    @Override
    public boolean isSensitive(SpeciesCheck check) {
        return this.call(this.alaSdsService.isSensitive(check));
    }

    /**
     * Test to see whether a taxon is potentially sensitive
     *
     * @param scientificName The scientific name
     * @param taxonId        The taxon identifier
     * @return True if this species is sensitive in some location
     */
    @Override
    public boolean isSensitive(String scientificName, String taxonId) {
        return this.call(this.alaSdsService.isSensitive(scientificName, taxonId));
    }


    /**
     * Provide a sensitivty report for an occurrence.
     *
     * @param query The information from the occurrence record.
     * @return A sensitivitiy report
     */
    @Override
    public SensitivityReport report(SensitivityQuery query) {
        return this.call(this.alaSdsService.report(query));
    }


    /**
     * Provide a sensitivty report for an occurrence.
     *
     * @param scientificName The scientific name
     * @param taxonId        The taxon identifier
     * @param dataResourceUid The source data resource
     * @param zones          The occurrence record zones
     * @return A sensitivitiy report
     */
    @Override
    public SensitivityReport report(String scientificName, String taxonId, String dataResourceUid, String stateProvince, String country, List<String> zones) {
        return this.call(this.alaSdsService.report(scientificName, taxonId, dataResourceUid, stateProvince, country, zones));
    }

    /**
     * Process an occurrence record and supply modified values.
     *
     * @param query The information from the occurrence record.
     * @return A report giving what is sensitive and what values need to be altered
     */
    @Override
    public SensitivityReport process(ProcessQuery query) {
        return this.call(this.alaSdsService.process(query));
    }

    @Override
    public void close() throws IOException {
        if (Objects.nonNull(okHttpClient) && Objects.nonNull(okHttpClient.cache())) {
            File cacheDirectory = okHttpClient.cache().directory();
            if (cacheDirectory.exists()) {
                try (Stream<File> files = Files.walk(cacheDirectory.toPath())
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)) {
                    files.forEach(File::delete);
                }
            }
        }
    }

    /**
     * Make a call to the web service and return teh result
     *
     * @param call The HTTP call
     *
     * @param <T> The type of response expected
     *
     * @return The response, returns null if a 204 (no content) result is returned from the server
     *
     * @throws HttpException to propagate an error
     * @throws ClientException if unable to contact the service
     */
    private <T> T call(Call<T> call) throws HttpException, ClientException {
        try {
            Response<T> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            log.debug("Response returned error - {}", response);
            throw new HttpException(response); // Propagates the failed response
        } catch (IOException ex) {
            throw new ClientException("Unable to contact service for " + call, ex);
        }
    }
}
