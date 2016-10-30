package com.mad.cipelist.result.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mad.cipelist.R;
import com.mad.cipelist.services.yummly.model.LocalRecipe;

import java.util.ArrayList;

/**
 * Binds the selected recipes to the fragments recyclerview.
 */

public class RecipeRecyclerViewAdapter extends RecyclerView
        .Adapter<RecipeRecyclerViewAdapter
        .RecipeHolder> {

    private static final String LOG_TAG = "RecipeRvAdapter";
    private final ArrayList<LocalRecipe> mDataset;
    private final OnRecipeClickListener mListener;

    /**
     * Constructor that sets the context and data to be displayed
     * @param dataset A set of recipes that the user has selected
     */
    public RecipeRecyclerViewAdapter(ArrayList<LocalRecipe> dataset, OnRecipeClickListener listener) {
        this.mDataset = dataset;
        this.mListener = listener;
        //Log.d(LOG_TAG, "Initialising Adapter");
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_recipe_row, parent, false);
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
        private ImageView image;


        /**
         * Constructor for new view holders.
         *
         * @param itemView Presents the model data in a view
         */
        RecipeHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.recipeLbl);
            image = (ImageView) itemView.findViewById(R.id.recipeImg);
        }

        void bind(final LocalRecipe item, final OnRecipeClickListener listener) {
            label.setText(item.getRecipeName());

            String url = item.getImageUrl();
            if (url.substring(url.length() - 4, url.length()).equals("=s90")) {
                url = url.substring(0, url.length() - 4);
            }

            Glide.with(itemView.getContext()).load(url).into(image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
