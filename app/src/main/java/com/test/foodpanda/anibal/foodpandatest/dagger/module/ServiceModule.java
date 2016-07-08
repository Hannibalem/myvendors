package com.test.foodpanda.anibal.foodpandatest.dagger.module;

import android.app.Application;

import com.google.gson.Gson;
import com.test.foodpanda.anibal.foodpandatest.service.TestService;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anibal on 13/5/16.
 */
@Module
public class ServiceModule {

    @Provides
    @Singleton // needs to be consistent with the component scope
    public TestService provideRetrofitService(String endpoint, OkHttpClient client) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(TestService.class);
    }

    @Provides
    @Singleton
    String provideRetrofitEndpoint() {
        return "https://api.foodpanda.sg/api/v4/";
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        OkHttpClient client
                = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder().addHeader("x-fp-api-key", "android").build();
                        return chain.proceed(request);
                    }
                }).build();
        return client;
    }


    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }
}
