package au.org.ala.sds.ws.client;

import au.org.ala.sds.api.*;
import au.org.ala.sds.generalise.Generalisation;
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
    Call<List<String>> getSensitiveDataFields();

    @GET("/api/generalisations")
    Call<List<Generalisation>> getGeneralisations();

    @POST("/api/isSensitive")
    @Headers({"Content-Type: application/json"})
    Call<Boolean> isSensitive(@Body SpeciesCheck check);

    @GET("/api/isSensitive")
    Call<Boolean> isSensitive(
            @Query("scientificName") String scientificName,
            @Query("taxonId") String taxonId
    );

    @POST("/api/report")
    @Headers({"Content-Type: application/json"})
    Call<SensitivityReport> report(@Body SensitivityQuery query);

    @GET("/api/report")
     Call<SensitivityReport> report(
        @Query("scientificName") String scientificName,
        @Query("taxonId") String taxonId,
        @Query("dataResourceUid") String dataResourceUid,
        @Query("stateProvince") String stateProvince,
        @Query("country") String country,
        @Query("zone") List<String> zones
    );

    @POST("/api/process")
    @Headers({"Content-Type: application/json"})
    Call<SensitivityReport> process(@Body ProcessQuery query);
}
