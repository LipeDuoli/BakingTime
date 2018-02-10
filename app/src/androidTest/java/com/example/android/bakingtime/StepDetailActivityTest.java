package com.example.android.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingtime.activities.StepDetailActivity;
import com.example.android.bakingtime.model.Step;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class StepDetailActivityTest {

    private List<Step> testSteps;
    private String recipeName = "Test Recipe";

    @Rule
    public ActivityTestRule<StepDetailActivity> mStepActivityTestRule = new ActivityTestRule<StepDetailActivity>(StepDetailActivity.class) {
        @Override
        protected Intent getActivityIntent() {

            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();

            testSteps = createTestSteps();

            Intent result = new Intent(targetContext, StepDetailActivity.class);
            result.putExtra(StepDetailActivity.EXTRA_STEPS, new ArrayList<Parcelable>(testSteps));
            result.putExtra(StepDetailActivity.EXTRA_STEP_SELECTED, 0);
            result.putExtra(StepDetailActivity.EXTRA_RECIPE_NAME, recipeName);
            return result;
        }
    };

    private List<Step> createTestSteps() {
        List<Step> steps = new ArrayList<>();
        steps.add(new Step(0, "shortDescription", "description", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4", ""));
        steps.add(new Step(1, "shortDescription", "description", "", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4"));
        steps.add(new Step(2, "shortDescription", "description", "", ""));

        return steps;
    }

    @Test
    public void viewPagerIsDisplayed() {
        onView(withId(R.id.step_detail_frame)).check(matches(isDisplayed()));
    }

    @Test
    public void checkActivityActionBar() {
        StepDetailActivity activity = mStepActivityTestRule.getActivity();
        int orientation = activity.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            boolean isActionBarhide = activity.getSupportActionBar().isShowing();
            assertEquals(false, isActionBarhide);
        } else {
            CharSequence title = activity.getSupportActionBar().getTitle();
            Assert.assertEquals(title, recipeName);
        }
    }


}
