package com.example.android.bakingtime.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.android.bakingtime.fragments.StepDetailFragment;
import com.example.android.bakingtime.model.Step;

import java.util.ArrayList;
import java.util.List;

public class IngredientDetailPageAdapter extends FragmentStatePagerAdapter {

    private List<StepDetailFragment> mFragments;
    private List<Step> mSteps;

    public IngredientDetailPageAdapter(FragmentManager fm, List<Step> steps) {
        super(fm);
        mSteps = steps;
        mFragments = new ArrayList<>();
        for (Step step: steps) {
            addFragment(step);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    private void addFragment(Step step){
        Bundle bundle = new Bundle();
        bundle.putParcelable(StepDetailFragment.EXTRA_STEP, step);
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(bundle);

        mFragments.add(stepDetailFragment);
    }

    @Override
    public int getCount() {
        return mSteps == null ? 0 : mSteps.size();
    }
}
