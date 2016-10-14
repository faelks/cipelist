package com.mad.cipelist.shoppinglist;

import android.content.Context;
import android.widget.TextView;

import com.mad.cipelist.R;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.ChildPosition;
import com.mindorks.placeholderview.annotations.expand.ParentPosition;

@Layout(R.layout.feed_item)
public class IngredientView {

    @ParentPosition
    private int mParentPosition;

    @ChildPosition
    private int mChildPosition;

    @View(R.id.titleTxt)
    private TextView titleTxt;

    private String mIngredient;
    private Context mContext;

    public IngredientView(Context context, String name) {
        mContext = context;
        mIngredient = name;
    }

    @Resolve
    private void onResolved() {
        titleTxt.setText(mIngredient);
    }
}