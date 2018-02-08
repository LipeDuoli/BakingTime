package com.example.android.bakingtime.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.Ingredient;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.util.IngredientUtil;

import java.util.List;

public class ListWidgetService extends RemoteViewsService {

    public static final String EXTRA_RECIPE = "extra_recipe_widget";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Recipe recipe = null;
        if (intent.hasExtra(EXTRA_RECIPE)) {
            Bundle bundleExtra = intent.getBundleExtra(EXTRA_RECIPE);
            recipe = bundleExtra.getParcelable(EXTRA_RECIPE);
        }
        return new ListRemoteViewFactory(getApplicationContext(), recipe);
    }
}

class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Ingredient> mIgredients;

    public ListRemoteViewFactory(Context context, @NonNull Recipe recipe) {
        mContext = context;
        mIgredients = recipe.getIngredients();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIgredients == null) return 0;
        return mIgredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_item);

        view.setTextViewText(R.id.tv_ingredient_name,
                IngredientUtil.formatIngredientName(mContext, mIgredients.get(position)));

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
