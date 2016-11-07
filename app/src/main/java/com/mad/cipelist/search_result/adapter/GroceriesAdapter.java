package com.mad.cipelist.search_result.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mad.cipelist.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generates a grocerylist for the user based on the selected recipes.
 */
public class GroceriesAdapter implements android.widget.ExpandableListAdapter {

    private final Set<Pair<Long, Long>> mCheckedItems = new HashSet<>();
    private List<String> headers;
    private HashMap<String, List<String>> data;
    private Context context;

    public GroceriesAdapter(Context context, List<String> headers, HashMap<String, List<String>> data, Set<Pair<Long, Long>> checkedItems) {
        this.context = context;
        this.headers = headers;
        this.data = data;
        //Log.d("GroceriesAdapter", "We have headers: " + headers);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.grocery_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    public Set<Pair<Long, Long>> getCheckedItems() {
        return mCheckedItems;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.grocery_list_item, null);
        }

        final String childText = (String) getChild(groupPosition, childPosition);
        final CheckBox childCb = (CheckBox) convertView.findViewById(R.id.item_acquired_checkbox);
        // add tag to remember groupId/childId
        final Pair<Long, Long> tag = new Pair<Long, Long>(
                getGroupId(groupPosition),
                getChildId(groupPosition, childPosition));
        childCb.setTag(tag);
        childCb.setChecked(mCheckedItems.contains(tag));
        childCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CheckBox cb = (CheckBox) view;
                final Pair<Long, Long> tag = (Pair<Long, Long>) view.getTag();
                if (cb.isChecked()) {
                    mCheckedItems.add(tag);
                } else {
                    mCheckedItems.remove(tag);
                }
            }
        });

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.item_label);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return this.headers.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.data.get(this.headers.get(i)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.headers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.data.get(this.headers.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l1) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }
}
