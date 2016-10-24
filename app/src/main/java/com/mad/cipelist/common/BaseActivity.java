package com.mad.cipelist.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by Felix on 24/10/2016.
 */

public abstract class BaseActivity extends Activity {

    protected AVLoadingIndicatorView mAvi;
    protected TextView mLoadTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void startLoadAnim(String msg) {
        mAvi.smoothToShow();
        mLoadTxt.setText(msg);
        mLoadTxt.setVisibility(View.VISIBLE);
    }

    public void stopLoadAnim() {
        mAvi.smoothToHide();
        mLoadTxt.setVisibility(View.GONE);
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


}
