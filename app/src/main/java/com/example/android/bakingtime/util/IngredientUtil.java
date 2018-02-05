package com.example.android.bakingtime.util;

import java.text.DecimalFormat;

public class IngredientUtil {

    public static String formatQuantity(double quantity){
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.#");
        return decimalFormat.format(quantity);
    }
}
