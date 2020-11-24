package com.onlinecash.loanswithout;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ActualBackendService {
    @GET("/")
    Call<ActualBackendJson> getActualBackend(@Query("p1") String p1, @Query("p2") String p2, @Query("p3") String p3, @Query("p4") String p4, @Query("p5") String p5,
                                             @Query("p6") String p6, @Query("p7") String p7, @Query("p8") String p8, @Query("p9") String p9);
}
