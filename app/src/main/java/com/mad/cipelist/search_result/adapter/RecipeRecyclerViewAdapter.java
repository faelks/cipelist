package com.mad.cipelist.search_result.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mad.cipelist.R;
import com.mad.cipelist.common.Utils;
import com.mad.cipelist.services.model.LocalRecipe;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Binds the selected recipes to the fragments recyclerview.
 */

public class RecipeRecyclerViewAdapter extends RecyclerView
        .Adapter<RecipeRecyclerViewAdapter
        .RecipeHolder> {

    //private static final String LOG_TAG = "RecipeRvAdapter";
    private final List<LocalRecipe> mDataset;
    private final OnRecipeClickListener mListener;

    /**
     * Constructor that sets the context and data to be displayed
     *
     * @param dataset A set of recipes that the user has selected
     */
    public RecipeRecyclerViewAdapter(List<LocalRecipe> dataset, OnRecipeClickListener listener) {
        this.mDataset = dataset;
        this.mListener = listener;
        //Log.d(LOG_TAG, "Initialising Adapter");
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {
        holder.bind(mDataset.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnRecipeClickListener {
        void onItemClick(LocalRecipe recipe);
    }

    /**
     * Creats view holders for the recycler view.
     * Rows are created and populated with data from
     * the model
     */
    static class RecipeHolder extends RecyclerView.ViewHolder {


        private TextView label;
        private TextView ingredientsAmountTv;
        private ImageView image;
        private ProgressBar progress;
        private LinearLayout infoHolder;


        /**
         * Constructor for new view holders.
         *
         * @param itemView Presents the model data in a view
         */
        RecipeHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.recipe_title_tv);
            image = (ImageView) itemView.findViewById(R.id.recipeImg);
            ingredientsAmountTv = (TextView) itemView.findViewById(R.id.recipe_ingredient_amount_tv);
            progress = (ProgressBar) itemView.findViewById(R.id.recipe_card_progress_bar);
            infoHolder = (LinearLayout) itemView.findViewById(R.id.recipe_card_info_holders);
        }

        void bind(final LocalRecipe item, final OnRecipeClickListener listener) {
            label.setText(item.getRecipeName());

            Type type = new TypeToken<List<String>>() {
            }.getType();
            ArrayList<String> ingredients = new Gson().fromJson(item.getIngredients(), type);
            String ingredientsAmount = ingredients.size() + itemView.getContext().getString(R.string._ingredients);
            ingredientsAmountTv.setText(ingredientsAmount);

            String url = Utils.removeUrlImageSize(item.getImageUrl());

            Glide.with(itemView.getContext()).load(url).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    infoHolder.setVisibility(View.VISIBLE);
                    image.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                    return false;
                }
            }).into(image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
