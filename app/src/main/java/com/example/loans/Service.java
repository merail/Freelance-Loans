package com.example.loans;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    String API_KEY = "qBOHDEPHgGMxVObkgQAtQunvj5EwNoIk";

    @GET("gifs/random")
    Call<Json> getJson(@Query("api_key") String api_key);
}
