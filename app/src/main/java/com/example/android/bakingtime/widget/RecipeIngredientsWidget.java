package com.example.android.bakingtime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.activities.RecipeStepActivity;
import com.example.android.bakingtime.model.Recipe;

public class RecipeIngredientsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {

        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.recipe_ingredients_widget);

        views.setTextViewText(R.id.appwidget_text, recipe.getName());

        Intent listIntent = new Intent(context, ListWidgetService.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable(ListWidgetService.EXTRA_RECIPE, recipe);

        listIntent.putExtra(ListWidgetService.EXTRA_RECIPE, bundle);
        views.setRemoteAdapter(R.id.appwidget_list, listIntent);

        views.setEmptyView(R.id.appwidget_list, R.id.appwidget_empty_view);

        Intent recipeDetail = new Intent(context, RecipeStepActivity.class);
        recipeDetail.putExtra(RecipeStepActivity.EXTRA_RECIPE, recipe);
        PendingIntent recipeDetailPendingIntent = PendingIntent
                .getActivity(context, appWidgetId, recipeDetail, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.appwidget_text, recipeDetailPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            WidgetConfigureActivity.loadWidgetRecipe(context, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            WidgetConfigureActivity.deleteWidgetPref(context, appWidgetId);
        }
    }
}

