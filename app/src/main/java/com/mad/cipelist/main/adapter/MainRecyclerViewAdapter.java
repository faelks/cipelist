package com.mad.cipelist.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mad.cipelist.R;
import com.mad.cipelist.services.yummly.LocalSearch;

import java.util.List;

/**
 * Adapter for managing the searches and displaying them.
 * The class initialises new view holders and adds new or
 * removes items on request.
 */
public class MainRecyclerViewAdapter extends RecyclerView
        .Adapter<MainRecyclerViewAdapter
        .DataObjectHolder> {

    // Tag for log statements, has the name of the class
    private static String LOG_TAG = "MainRecyclerViewAdapter";
    // A listener that handles clicks on the items in the view
    private static MyClickListener myClickListener;
    // The model which is intitialised in the constructor
    private List<LocalSearch> mDataset;
    // The context of the parent activity, useless?
    private Context mContext;
    private String mUserId;
    private String mUserEmail;

    /**
     * Constructor that sets the context and data to be displayed
     * @param context
     * @param dataset
     */
    public MainRecyclerViewAdapter(Context context, List<LocalSearch> dataset, String id, String email) {
        mContext = context;
        mDataset = dataset;
        mUserId = id;
        mUserEmail = email;
    }

    /**
     * Binds a click listener to the adapter.
     * @param myClickListener
     */
    public void setOnItemClickListener(MyClickListener myClickListener) {
        MainRecyclerViewAdapter.myClickListener = myClickListener;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.square_card_main, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        //String searchLabel = mUserEmail + (position+1);
        //holder.label.setText(searchLabel);
        if (mDataset.size() > 0) {
            String url = mDataset.get(position).getRecipes().get(0).getImageUrl();

            // Looks for a size restriction at the end of the url and removes it if found
            if (url.substring(url.length() - 4, url.length()).equals("=s90")) {
                url = url.substring(0, url.length() - 4);
            }
            Log.d(LOG_TAG, "Trying to glide with url " + url);
            Glide.with(holder.itemView.getContext()).load(url).into(holder.image);
        }
        //holder.dateTime.setText(mDataset.get(position).getTotalMatchCount());
    }

    public void addItem(LocalSearch dataObj, int index) {
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

    public String getSearchId(int position) {
        return mDataset.get(position).searchId;
    }

    /**
     * Interface for the click listener
     */
    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

    /**
     * Creats view holders for the recycler view.
     * Rows are created and populated with data from
     * the model
     */
    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        //TextView dateTime;
        ImageView image;

        /**
         * Constructor for new view holders.
         *
         * @param itemView
         */
        public DataObjectHolder(View itemView) {
            super(itemView);
            //label = (TextView) itemView.findViewById(R.id.square_card_label);
            //dateTime = (TextView) itemView.findViewById(R.id.textView2);
            image = (ImageView) itemView.findViewById(R.id.searchIv);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}