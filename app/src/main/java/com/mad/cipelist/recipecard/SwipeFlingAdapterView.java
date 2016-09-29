package com.mad.cipelist.recipecard;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.mad.cipelist.activity.SwiperActivity;

/**
 * Created by Felix on 29/09/16.
 */
public class SwipeFlingAdapterView extends AdapterView<Data> {

    public SwipeFlingAdapterView(Context context) {
        super(context);
    }

    @Override
    public Data getAdapter() {
        return null;
    }

    @Override
    public void setAdapter(Data data) {

    }

    @Override
    public View getSelectedView() {
        return null;
    }

    @Override
    public void setSelection(int i) {

    }

    public void setFlingListener(OnFlingListener listener) {

    }

    public void setAdapter(SwiperActivity.MyAppAdapter myAppAdapter) {

    }

    public static class OnFlingListener {
        public void removeFirstObjectInAdapter() {

        }

        public void onLeftCardExit(Object dataObject) {

        }

        public void onRightCardExit(Object dataObject) {

        }

        public void onAdapterAboutToEmpty(int itemsInAdapter) {

        }

        public void onScroll(float scrollProgressPercent) {

        }

    }
}
