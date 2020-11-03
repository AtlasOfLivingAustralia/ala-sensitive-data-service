package au.org.ala.sds.ws.client;

import au.org.ala.sds.api.ConservationApi;
import au.org.ala.sds.api.SensitivityQuery;
import au.org.ala.sds.api.SensitivityReport;
import au.org.ala.sds.api.SpeciesCheck;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * ALA name matching Retrofit Service client.
 *
 * @see ConservationApi
 */
interface ALASDSRetrofitService {
    @GET("/api/sensitiveDataFields")
    @Headers({"Content-Type: application/json"})
    Call<List<String>> getSensitiveDataFields();

    @POST("/api/isSensitive")
    @Headers({"Content-Type: application/json"})
    Call<Boolean> isSensitive(@Body SpeciesCheck check);

    @GET("/api/isSensitive")
    Call<Boolean> isSensitive(
            @Query("scientificName") String scientificName,
            @Query("taxonId") String taxonId
    );

    @POST("/api/process")
    @Headers({"Content-Type: application/json"})
    Call<SensitivityReport> process(@Body SensitivityQuery query);
}
