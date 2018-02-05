package com.example.android.bakingtime.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.fragments.StepDetailFragment;

import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity {

    public static final String EXTRA_STEP = "extraStep";
    public static final String EXTRA_RECIPE_NAME = "recipeName";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            getSupportActionBar().hide();
        } else {
            if(getIntent().hasExtra(EXTRA_RECIPE_NAME)){
                getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_RECIPE_NAME));
            }
        }

        // if saved instance is not null, fragment is already initialized
        if (savedInstanceState == null){
            initStepFragment();
        }
    }

    private void initStepFragment() {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.step_detail_frame, stepDetailFragment)
                .commit();
    }
}
