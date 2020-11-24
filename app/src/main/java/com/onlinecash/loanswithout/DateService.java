package com.onlinecash.loanswithout;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DateService {
    @GET("/date.json")
    Call<DateJson> getDate();
}
