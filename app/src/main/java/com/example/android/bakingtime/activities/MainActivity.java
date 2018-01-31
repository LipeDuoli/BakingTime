package com.example.android.bakingtime.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.adapter.RecipeAdapter;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.service.BakingApiFactory;
import com.example.android.bakingtime.service.BakingService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private static String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rv_recipes)
    RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        configureRecyclerView();
        fetchRecipes();
    }

    private void fetchRecipes() {
        BakingService bakingService = BakingApiFactory.getBakingService(this);
        bakingService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                mRecipeAdapter.setRecipeList(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void configureRecyclerView() {
        mRecipeAdapter = new RecipeAdapter(this, this);

        mRecipeRecyclerView.setAdapter(mRecipeAdapter);
        mRecipeRecyclerView.setHasFixedSize(true);

        if (getResources().getBoolean(R.bool.isTabletMode)) {
            mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    public void onClickRecipe(Recipe recipe) {

    }
}
