package com.mad.cipelist.result;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mad.cipelist.R;
import com.mad.cipelist.common.BaseActivity;

/**
 * Created by Felix on 30/10/2016.
 */

public class RecipeDetail extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_recipe_detail, contentFrameLayout);

        TextView idView = (TextView) findViewById(R.id.recipe_id);

        idView.setText(getIntent().getStringExtra("recipeId"));
    }
}
