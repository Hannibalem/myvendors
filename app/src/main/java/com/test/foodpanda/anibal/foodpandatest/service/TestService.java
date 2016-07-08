package com.test.foodpanda.anibal.foodpandatest.service;

import com.test.foodpanda.anibal.foodpandatest.api.BaseResponse;
import com.test.foodpanda.anibal.foodpandatest.api.VendorsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Anibal on 13/5/16.
 */
public interface TestService {

    @GET("vendors")
    Observable<VendorsResponse> getVendors(
            //@Query("area_id") long area_id,
            //@Query("language_id") int language_id,
            //@Query("latitude") float latitude,
            //@Query("longitude") long longitude
            @QueryMap Map<String, Object> body);
}
