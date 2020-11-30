package com.onlinecash.loanswithout;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface DateService {

    @GET
    Call<DateJson> getDate(@Url String dateLink);
}
