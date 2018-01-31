package com.example.android.bakingtime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> mRecipes;
    private Context mContext;
    private RecipeAdapterOnClickHandler mOnClickHandler;

    public RecipeAdapter(Context context, RecipeAdapterOnClickHandler onClickHandler) {
        mContext = context;
        mOnClickHandler = onClickHandler;
    }

    public interface RecipeAdapterOnClickHandler {
        void onClickRecipe(Recipe recipe);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);

        holder.mRecipeName.setText(recipe.getName());
        if (recipe.getImage() != null && !recipe.getImage().isEmpty()){
            Picasso.with(mContext).load(recipe.getImage()).into(holder.mRecipeImage);
            holder.mRecipeImage.setVisibility(View.VISIBLE);
        } else {
            holder.mRecipeImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;
        return mRecipes.size();
    }

    public void setRecipeList(List<Recipe> recipes){
        this.mRecipes = recipes;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_recipe_image)
        ImageView mRecipeImage;
        @BindView(R.id.tv_recipe_name)
        TextView mRecipeName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickHandler.onClickRecipe(mRecipes.get(getAdapterPosition()));
        }
    }
}
