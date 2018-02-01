package com.example.android.bakingtime.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.activities.RecipeStepActivity;
import com.example.android.bakingtime.adapter.IngredientAdapter;
import com.example.android.bakingtime.adapter.StepAdapter;
import com.example.android.bakingtime.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeStepFragment extends Fragment implements StepAdapter.StepAdapterOnClickHandler {

    @BindView(R.id.rv_recipe_ingredients)
    RecyclerView mIngredientsRecyclerView;
    @BindView(R.id.rv_recipe_step)
    RecyclerView mStepRecyclerView;
    private Unbinder mUnbinder;
    private Recipe mRecipe;

    OnStepClickListener mCallback;

    public interface OnStepClickListener {
        void onStepSelected(int stepPosition);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mRecipe = getArguments().getParcelable(RecipeStepActivity.EXTRA_RECIPE);

        configureIngredientRecyclerView();
        configureStepRecyclerView();

        return view;
    }

    private void configureIngredientRecyclerView() {
        IngredientAdapter ingredientAdapter = new IngredientAdapter(getContext(), mRecipe.getIngredients());

        mIngredientsRecyclerView.setAdapter(ingredientAdapter);
        mIngredientsRecyclerView.setHasFixedSize(true);
        mIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void configureStepRecyclerView() {
        StepAdapter stepAdapter = new StepAdapter(mRecipe.getSteps(), this);

        mStepRecyclerView.setAdapter(stepAdapter);
        mStepRecyclerView.setHasFixedSize(true);
        mStepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onClickStep(int stepPosition) {
        mCallback.onStepSelected(stepPosition);
    }
}
