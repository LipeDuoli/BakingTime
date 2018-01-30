package com.example.android.bakingtime.service;

import com.example.android.bakingtime.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingService {

    String RECIPIE_PATH = "baking.json";

    @GET(RECIPIE_PATH)
    Call<List<Recipe>> getRecipes();
}
