package com.onlinecash.loanswithout;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface DatabaseService {

    @GET
    Call<DatabaseJson> getDatabase(@Url String databaseLink);
}
