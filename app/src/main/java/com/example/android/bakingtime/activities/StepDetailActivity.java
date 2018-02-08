package com.example.android.bakingtime.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.adapter.IngredientDetailPageAdapter;
import com.example.android.bakingtime.fragments.StepDetailFragment;
import com.example.android.bakingtime.model.Step;
import com.rd.PageIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity {

    public static final String TAG = StepDetailActivity.class.getSimpleName();

    public static final String EXTRA_STEPS = "extraStep";
    public static final String EXTRA_RECIPE_NAME = "recipeName";
    public static final String EXTRA_STEP_SELECTED = "extraSelectedStep";
    public static final int DEFAULT_SELECTED_VALUE = 0;

    @BindView(R.id.step_detail_frame)
    ViewPager mStepDetailViewPager;
    @BindView(R.id.step_detail_indicator)
    PageIndicatorView mStepDetailIndicator;

    private int mSelectedStep;
    private ArrayList<Step> mSteps;
    private IngredientDetailPageAdapter pageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);

        //check if need to back to tablet mode view
        if (getResources().getBoolean(R.bool.isTablet) &&
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            finish();
        }

        configureActionBar();

        mSelectedStep = getIntent().getIntExtra(EXTRA_STEP_SELECTED, DEFAULT_SELECTED_VALUE);
        mSteps = getIntent().getParcelableArrayListExtra(StepDetailFragment.EXTRA_STEP);

        configureViewPager();

    }

    private void configureViewPager() {
        pageAdapter =
                new IngredientDetailPageAdapter(getSupportFragmentManager(), mSteps);
        mStepDetailViewPager.setAdapter(pageAdapter);

        mStepDetailViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            private int mlastPosition = 0;

            @Override
            public void onPageSelected(int position) {
                StepDetailFragment outFragment = (StepDetailFragment) pageAdapter.getItem(mlastPosition);
                outFragment.pausePlayer();

                StepDetailFragment inFragment = (StepDetailFragment) pageAdapter.getItem(position);
                inFragment.resumePlayer();

                mlastPosition = position;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStepDetailViewPager.setCurrentItem(mSelectedStep);
    }

    private void configureActionBar() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
        } else {
            if (getIntent().hasExtra(EXTRA_RECIPE_NAME)) {
                getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_RECIPE_NAME));
            }
        }
    }
}
