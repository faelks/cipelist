package com.mad.cipelist.result.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mad.cipelist.R;
import com.mad.cipelist.common.LocalRecipe;

import java.util.List;

/**
 * Created by Felix on 19/10/2016.
 */

public class RecipeRecyclerViewAdapter extends RecyclerView
        .Adapter<RecipeRecyclerViewAdapter
        .RecipeHolder> {

    // Tag for log statements, has the name of the class
    private static String LOG_TAG = "RecipeRVAdapter";
    // A listener that handles clicks on the items in the view
    private static RecipeClickListener mRecipeClickListener;
    // The model which is intitialised in the constructor
    private List<LocalRecipe> mDataset;
    // The context of the parent activity, useless?
    private Context mContext;

    /**
     * Constructor that sets the context and data to be displayed
     *
     * @param context
     * @param myDataset
     */
    public RecipeRecyclerViewAdapter(Context context, List<LocalRecipe> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    /**
     * Binds a click listener to the adapter.
     */
    public void setOnItemClickListener(RecipeClickListener mRecipeClickListener) {
        RecipeRecyclerViewAdapter.mRecipeClickListener = mRecipeClickListener;
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        RecipeHolder recipeHolder = new RecipeHolder(view);
        return recipeHolder;
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getRecipeName());
        //holder.label.setText(mDataset.get(position).searchId);
        //holder.dateTime.setText(mDataset.get(position).getTotalMatchCount());
    }

    public void addItem(LocalRecipe dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * Interface for the click listener
     */
    public interface RecipeClickListener {
        void onItemClick(int position, View v);
    }

    /**
     * Creats view holders for the recycler view.
     * Rows are created and populated with data from
     * the model
     */
    public static class RecipeHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        TextView dateTime;
        ImageView image;

        /**
         * Constructor for new view holders.
         *
         * @param itemView
         */
        public RecipeHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.textView);
            dateTime = (TextView) itemView.findViewById(R.id.textView2);
            image = (ImageView) itemView.findViewById(R.id.cardImage);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mRecipeClickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}
