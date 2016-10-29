package com.mad.cipelist.swiper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.mad.cipelist.R;
import com.mad.cipelist.common.BaseActivity;

/**
 * Created by Felix on 29/10/2016.
 */

public class SearchFilterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_search_filter, contentFrameLayout);

        Button mStartSearchBtn = (Button) findViewById(R.id.start_search_btn);
        mStartSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent swiperIntent = new Intent(SearchFilterActivity.this, SwiperActivity.class);
                swiperIntent.putExtra("recipeAmount", 7);
                startActivity(swiperIntent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        });
    }
}
