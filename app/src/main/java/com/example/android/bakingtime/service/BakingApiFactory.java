package com.example.android.bakingtime.service;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BakingApiFactory {

    private static final String BAKING_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    public static BakingService getBakingService(final Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BAKING_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(BakingService.class);
    }
    
}
