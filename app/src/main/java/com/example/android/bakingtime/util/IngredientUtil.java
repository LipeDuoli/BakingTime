package com.example.android.bakingtime.util;

import android.content.Context;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.Ingredient;

import java.text.DecimalFormat;

public class IngredientUtil {

    public static String formatIngredientName(Context context, Ingredient ingredient){
        return context.getString(R.string.ingredients_display,
                IngredientUtil.formatQuantity(ingredient.getQuantity()),
                ingredient.getMeasure(),
                ingredient.getIngredient());
    }

    public static String formatQuantity(double quantity){
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.#");
        return decimalFormat.format(quantity);
    }
}
