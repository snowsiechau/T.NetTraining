package com.example.glory_hunter.tnettraining.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by glory-hunter on 17/01/2018.
 */

public interface GetAddressService {
    @GET("https://maps.googleapis.com/maps/api/geocode/json?")
    Call<MainObjectJSON> getAddress(@Query("latlng") String latlng,
                                    @Query("sensor") boolean sensor);
}
