package com.onlinecash.loanswithout;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DatabaseService {

    @GET("/db.json")
    Call<DatabaseJson> getDatabase();
}
