package com.example.android.bakingtime.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private List<Step> mSteps;
    private StepAdapterOnClickHandler mOnClickHandler;
    private View mLastSelectedView;

    public StepAdapter(List<Step> steps, StepAdapterOnClickHandler onClickHandler) {
        this.mSteps = steps;
        this.mOnClickHandler = onClickHandler;
    }

    public interface StepAdapterOnClickHandler {
        void onClickStep(int stepPosition);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.step_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Step step = mSteps.get(position);

        holder.mIngredientName.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) return 0;
        return mSteps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_step_name)
        TextView mIngredientName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            changeSelectedBackground(view);
            mOnClickHandler.onClickStep(getAdapterPosition());
        }

        private void changeSelectedBackground(View view) {
            view.setSelected(true);
            if (mLastSelectedView != null){
                mLastSelectedView.setSelected(false);
            }
            mLastSelectedView = view;
        }
    }
}
