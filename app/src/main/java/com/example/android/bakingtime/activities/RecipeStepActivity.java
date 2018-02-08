package com.example.android.bakingtime.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.fragments.RecipeStepFragment;
import com.example.android.bakingtime.fragments.StepDetailFragment;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.model.Step;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class RecipeStepActivity extends AppCompatActivity implements RecipeStepFragment.OnStepClickListener {

    public static final String EXTRA_RECIPE = "extraRecipe";

    private Recipe mRecipe;
    private boolean mIsTwoPaneMode;
    private Intent mCurrentIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        ButterKnife.bind(this);

        mCurrentIntent = getIntent();

        if (mCurrentIntent.hasExtra(EXTRA_RECIPE)) {
            mRecipe = mCurrentIntent.getParcelableExtra(EXTRA_RECIPE);
            getSupportActionBar().setTitle(mRecipe.getName());
        }

        mIsTwoPaneMode = findViewById(R.id.step_detail_frame) != null;

        // if saved instance is not null, fragment is already initialized
        if (savedInstanceState == null) {
            initStepFragment();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(EXTRA_RECIPE)) {
            mCurrentIntent = intent;
            mRecipe = mCurrentIntent.getParcelableExtra(EXTRA_RECIPE);
            getSupportActionBar().setTitle(mRecipe.getName());
            initStepFragment();
        }
    }

    private void initStepFragment() {
        RecipeStepFragment stepFragment = new RecipeStepFragment();
        stepFragment.setArguments(mCurrentIntent.getExtras());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_step_frame, stepFragment)
                .commit();
    }

    @Override
    public void onStepSelected(int stepPosition) {
        Step stepSelected = mRecipe.getSteps().get(stepPosition);

        if (mIsTwoPaneMode) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable(StepDetailFragment.EXTRA_STEP, stepSelected);

            stepDetailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_frame, stepDetailFragment)
                    .commit();

        } else {
            Intent stepDetailIntent = new Intent(this, StepDetailActivity.class);
            stepDetailIntent.putParcelableArrayListExtra(StepDetailActivity.EXTRA_STEPS, new ArrayList<Parcelable>(mRecipe.getSteps()));
            stepDetailIntent.putExtra(StepDetailActivity.EXTRA_STEP_SELECTED, stepPosition);
            stepDetailIntent.putExtra(StepDetailActivity.EXTRA_RECIPE_NAME, mRecipe.getName());
            startActivity(stepDetailIntent);
        }
    }
}
