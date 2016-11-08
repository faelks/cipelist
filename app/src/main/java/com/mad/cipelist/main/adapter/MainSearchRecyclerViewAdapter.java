package com.mad.cipelist.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mad.cipelist.R;
import com.mad.cipelist.common.Utils;
import com.mad.cipelist.services.model.LocalSearch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for managing the searches and displaying them.
 * The class initialises new view holders and adds new or
 * removes items on request.
 */
public class MainSearchRecyclerViewAdapter extends RecyclerView
        .Adapter<MainSearchRecyclerViewAdapter.SearchHolder> {

    private static SearchClickListener searchClickListener;

    private List<LocalSearch> mDataset;
    private int mPosition;
    private Context mContext;

    /**
     * Constructor that sets the context and data to be displayed
     *
     * @param context application context
     * @param dataset The searches to be displayed in the rv
     */
    public MainSearchRecyclerViewAdapter(Context context, List<LocalSearch> dataset, String id, String email) {
        mDataset = dataset;
        mContext = context;
    }

    /**
     * Binds a click listener to the adapter.
     *
     * @param searchClickListener custom click listener
     */
    public void setOnItemClickListener(SearchClickListener searchClickListener) {
        MainSearchRecyclerViewAdapter.searchClickListener = searchClickListener;
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.square_card_main, parent, false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchHolder holder, int position) {
        String viewTitle = (mDataset.get(position).title != null) ? mDataset.get(position).title : mContext.getString(R.string.search) + (position + 1);
        String nOfRecipes = mContext.getString(R.string.number_of_recipes_main) + mDataset.get(position).getRecipes().size();

        // Reformat the date to be more readable
        SimpleDateFormat date = new SimpleDateFormat(mContext.getString(R.string.date_format_one));
        SimpleDateFormat formatter = new SimpleDateFormat(mContext.getString(R.string.date_format_two), Locale.US);
        try {
            Date converted = date.parse(mDataset.get(position).searchTimeStamp);
            holder.date.setText(formatter.format(converted));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.info.setText(viewTitle);
        holder.amount.setText(nOfRecipes);

        // Verify that there are recipes to load from the search
        if (!mDataset.get(position).getRecipes().isEmpty()) {
            // Retrieves the image of the first recipe in the search
            String url = mDataset.get(position).getRecipes().get(0).getImageUrl();
            url = Utils.removeUrlImageSize(url);
            Glide.with(holder.itemView.getContext()).load(url).into(holder.image);
        }

        // Display a context menu on long click
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
    }

    /**
     * Remove an item from the current dataset
     *
     * @param index postition of item
     */
    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        if (mDataset != null) {
            return mDataset.size();
        } else {
            return -1;
        }
    }

    /**
     * @return position
     */
    public int getPosition() {
        return mPosition;
    }

    /**
     * @param position Position to be set
     */
    private void setPosition(int position) {
        this.mPosition = position;
    }

    /**
     * retrieves the unique search id from the defined search
     * @param position index of search
     * @return String search id
     */
    public String getSearchId(int position) {
        return mDataset.get(position).searchId;
    }

    @Override
    public void onViewRecycled(SearchHolder holder) {
        // Remove listener to avoid reference issues
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    /**
     * Interface for the click listener
     */
    public interface SearchClickListener {
        void onItemClick(int position, View v);
    }

    /**
     * Creates view holders for the recycler view.
     * Rows are created and populated with data from
     * the model
     */
    static class SearchHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener, View.OnCreateContextMenuListener {
        TextView info;
        TextView date;
        TextView amount;
        ImageView image;

        /**
         * Constructor for new view holders.
         * @param view the search square view
         */
        SearchHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.search_date);
            info = (TextView) view.findViewById(R.id.search_title);
            image = (ImageView) view.findViewById(R.id.search_image);
            amount = (TextView) view.findViewById(R.id.search_recipe_amount);
            view.setOnClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            searchClickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.setHeaderTitle(R.string.select_action);
            menu.add(0, R.id.ctx_menu_view, 0, R.string.view);
            menu.add(0, R.id.ctx_menu_delete, 0, R.string.delete);
        }
    }
}