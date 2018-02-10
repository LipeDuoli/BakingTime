package com.example.android.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingtime.activities.RecipeStepActivity;
import com.example.android.bakingtime.model.Ingredient;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.model.Step;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.android.bakingtime.util.RecyclerViewItemCountAssertion.withItemCount;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(AndroidJUnit4.class)
public class RecipeStepActivityTest {

    private Recipe testRecipe;

    @Rule
    public ActivityTestRule<RecipeStepActivity> mStepActivityTestRule = new ActivityTestRule<RecipeStepActivity>(RecipeStepActivity.class) {
        @Override
        protected Intent getActivityIntent() {

            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();

            testRecipe = createTestRecipe();

            Intent result = new Intent(targetContext, RecipeStepActivity.class);
            result.putExtra(RecipeStepActivity.EXTRA_RECIPE, testRecipe);
            return result;
        }
    };

    private Recipe createTestRecipe() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(3, "k", "Ingredient 1"));
        ingredients.add(new Ingredient(2, "k", "Ingredient 2"));
        ingredients.add(new Ingredient(1, "l", "Ingredient 3"));

        List<Step> steps = new ArrayList<>();
        steps.add(new Step(0, "shortDescription", "description", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4", ""));
        steps.add(new Step(1, "shortDescription", "description", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4", ""));
        steps.add(new Step(2, "shortDescription", "description", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4", ""));

        return new Recipe(0, "Test Recipe", 4, "", ingredients, steps);
    }

    @Test
    public void checkIfIngredientsIsDisplayed() {
        onView(withId(R.id.rv_recipe_ingredients)).check(withItemCount(greaterThan(0)));
    }

    @Test
    public void checkIfStepsIsDisplayed() {
        onView(withId(R.id.rv_recipe_step)).check(withItemCount(greaterThan(0)));
    }

    @Test
    public void checkIngredientsDisplayedCount() {
        onView(withId(R.id.rv_recipe_ingredients)).check(withItemCount(testRecipe.getIngredients().size()));
    }

    @Test
    public void checkStepsDisplayedCount() {
        onView(withId(R.id.rv_recipe_step)).check(withItemCount(testRecipe.getSteps().size()));
    }

    @Test
    public void checkActivityTitle() {
        CharSequence title = mStepActivityTestRule.getActivity().getSupportActionBar().getTitle();
        assertEquals(title, testRecipe.getName());
    }

    @Test
    public void onClickStepItemMustShowStepDetail() {
        boolean isTwoPaneMode = mStepActivityTestRule.getActivity().isTwoPaneMode();
        onView(ViewMatchers.withId(R.id.rv_recipe_step))
                .perform(actionOnItemAtPosition(0, click()));

        if (isTwoPaneMode) {
            onView(withId(R.id.exo_step_video)).check(matches(isDisplayed()));
        } else {
            onView(withId(R.id.step_detail_frame)).check(matches(isDisplayed()));
        }
    }
}
