package com.example.android.bakingtime.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.fragments.RecipeStepFragment;
import com.example.android.bakingtime.model.Recipe;

import butterknife.ButterKnife;

public class RecipeStepActivity extends AppCompatActivity implements RecipeStepFragment.OnStepClickListener {

    public static final String EXTRA_RECIPE = "extraRecipe";

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(EXTRA_RECIPE)) {
            mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
        }

        initStepFragment();

    }

    private void initStepFragment() {
        RecipeStepFragment stepFragment = new RecipeStepFragment();
        stepFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.recipe_step_frame, stepFragment)
                .commit();
    }

    @Override
    public void onStepSelected(int stepPosition) {
        Intent stepDetailIntent = new Intent(this, StepDetailActivity.class);
        stepDetailIntent.putExtra(StepDetailActivity.EXTRA_STEP, mRecipe.getSteps().get(stepPosition));
        startActivity(stepDetailIntent);
    }
}
