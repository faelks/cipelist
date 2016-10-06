package com.mad.cipelist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mad.cipelist.R;
import com.mad.cipelist.yummly.SearchResult;

import java.util.ArrayList;

/**
 * Adapter for managing the searches and displaying them.
 * The class initialises new view holders and adds new or
 * removes items on request.
 */
public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {

    // Tag for log statements, has the name of the class
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    // The model which is intitialised in the constructor
    private ArrayList<SearchResult> mDataset;
    // A listener that handles clicks on the items in the view
    private static MyClickListener myClickListener;
    // The context of the parent activity, useless?
    private Context mContext;

    /**
     * Creats view holders for the recycler view.
     * Rows are created and populated with data from
     * the model
     */
    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        TextView dateTime;
        ImageView image;

        /**
         * Constructor for new view holders.
         * @param itemView
         */
        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.textView);
            dateTime = (TextView) itemView.findViewById(R.id.textView2);
            image = (ImageView) itemView.findViewById(R.id.cardImage);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    /**
     * Binds a click listener to the adapter.
     * @param myClickListener
     */
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    /**
     * Constructor that sets the context and data to be displayed
     * @param context
     * @param myDataset
     */
    public MyRecyclerViewAdapter(Context context, ArrayList<SearchResult> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getMatch().getRecipeName());
        holder.dateTime.setText(mDataset.get(position).getTotalMatchCount());
    }

    public void addItem(SearchResult dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public void updateImage(int index) {
        mDataset.get(index).setTotalMatchCount("0");
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * Interface for the click listener which currently does nothing?
     */
    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}