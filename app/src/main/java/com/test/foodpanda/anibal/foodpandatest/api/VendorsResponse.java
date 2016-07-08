package com.test.foodpanda.anibal.foodpandatest.api;

import com.google.gson.annotations.SerializedName;
import com.test.foodpanda.anibal.foodpandatest.model.Vendor;

/**
 * Created by Anibal on 13/5/16.
 */
public class VendorsResponse extends BaseResponse {

    public Data data;

    public static class Data {

        public Vendor[] items;
    }
}
