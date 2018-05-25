package com.example.chiunguo.myapplication.ServiceApi;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

public interface DemoService {
    @GET("20131119")
    Call<ResponseBody> getDemo();
}
