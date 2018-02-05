package com.example.android.bakingtime.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.fragments.RecipeStepFragment;
import com.example.android.bakingtime.fragments.StepDetailFragment;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.model.Step;

import butterknife.ButterKnife;

public class RecipeStepActivity extends AppCompatActivity implements RecipeStepFragment.OnStepClickListener {

    public static final String EXTRA_RECIPE = "extraRecipe";

    private Recipe mRecipe;
    private boolean mIsTwoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(EXTRA_RECIPE)) {
            mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
            getSupportActionBar().setTitle(mRecipe.getName());
        }

        mIsTwoPaneMode = findViewById(R.id.step_detail_frame) != null;

        // if saved instance is not null, fragment is already initialized
        if (savedInstanceState == null){
            initStepFragment();
        }
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
        Step stepSelected = mRecipe.getSteps().get(stepPosition);

        if (mIsTwoPaneMode){
            StepDetailFragment stepDetailFragment = new StepDetailFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable(StepDetailActivity.EXTRA_STEP, stepSelected);

            stepDetailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_frame, stepDetailFragment)
                    .commit();

        } else {
            Intent stepDetailIntent = new Intent(this, StepDetailActivity.class);
            stepDetailIntent.putExtra(StepDetailActivity.EXTRA_STEP, stepSelected);
            stepDetailIntent.putExtra(StepDetailActivity.EXTRA_RECIPE_NAME, mRecipe.getName());
            startActivity(stepDetailIntent);
        }
    }
}
