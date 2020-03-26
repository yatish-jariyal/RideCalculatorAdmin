package com.example.ridecalculatoradmin;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("updateFuelPrices/")
    Call<ResponseBody> updateFuelPrices(@Field("state") String state, @Field("city") String city, @Field("price") double price, @Field("fuel_type") String fuel_type);

}
