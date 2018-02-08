package com.example.android.bakingtime.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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

public class WidgetConfigureActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String PREFS_NAME = "com.example.android.widget.bakingtime";
    private static final String PREFS_PREFIX_KEY = "widget_id_";
    private static final int INVALID_RECIPE_ID = -1;
    private static String TAG = WidgetConfigureActivity.class.getSimpleName();

    @BindView(R.id.rv_recipes)
    RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private int mAppWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setResult(RESULT_CANCELED);

        getWidgetId();

        configureRecyclerView();
        fetchRecipes();
    }

    private void getWidgetId() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
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

        mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, getSpanCount()));
    }

    private int getSpanCount() {
        int orientation = getResources().getConfiguration().orientation;
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            if (isTablet) return 2;
            else return 1;
        } else {
            if (isTablet) return 3;
            else return 2;
        }
    }

    @Override
    public void onClickRecipe(Recipe recipe) {
        saveWidgetPref(this, mAppWidgetId, recipe.getId());

        // Push widget update to surface with newly set prefix
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RecipeIngredientsWidget.updateAppWidget(this, appWidgetManager,
                mAppWidgetId, recipe);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public static void saveWidgetPref(Context context, int appWidgetId, int recipeId){
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREFS_PREFIX_KEY + appWidgetId, recipeId);
        prefs.apply();
    }

    public static void deleteWidgetPref(Context context, int appWidgetId){
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREFS_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    public static void loadWidgetRecipe(final Context context, final int appWidgetId){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        final int recipeId = prefs.getInt(PREFS_PREFIX_KEY + appWidgetId, INVALID_RECIPE_ID);
        if (recipeId == INVALID_RECIPE_ID){
            return;
        }

        BakingService bakingService = BakingApiFactory.getBakingService(context);
        bakingService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();

                for (Recipe recipe: recipes){
                    if (recipe.getId() == recipeId){

                        // Push widget update to surface with newly set prefix
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                        RecipeIngredientsWidget.updateAppWidget(context, appWidgetManager,
                                appWidgetId, recipe);

                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG, "loadWidgetRecipe: " + t.getMessage());
            }
        });
    }
}
