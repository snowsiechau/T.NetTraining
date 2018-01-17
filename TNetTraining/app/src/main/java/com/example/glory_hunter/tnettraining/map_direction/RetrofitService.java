package com.example.glory_hunter.tnettraining.map_direction;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by qklahpita on 11/26/17.
 */

public interface RetrofitService {
    @GET("json")
    Call<DirectionResponse> getDirection(@Query("origin") String origin,
                                         @Query("destination") String destination);
}
