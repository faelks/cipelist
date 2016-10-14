package com.mad.cipelist.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.mad.cipelist.settings.SettingsActivity;
import com.mad.cipelist.swiper.SwiperActivity;
import com.mad.cipelist.main.adapter.MyRecyclerViewAdapter;
import com.mad.cipelist.R;
import com.mad.cipelist.yummly.search.model.Recipe;
import com.mad.cipelist.yummly.search.model.SearchResult;

import java.util.ArrayList;

/**
 * Displays the initial landing page with previous searches.
 * This method needs to load all searches associated with the
 * user and display them in the recycler view. Currently the
 * view is populated with default items.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView mSearchRecyclerView;
    private MyRecyclerViewAdapter mAdapter;
    private ArrayList<SearchResult> mSearches = new ArrayList<>();
    private static String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSearchRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String [] images = {"http://creationview.com/image/Birds4F.jpg", null};
        Recipe m1 = new Recipe(null, "1", "Pasta", "3600", images, null, null, "5", null);
        Recipe[] recipes = {m1};
        SearchResult sr = new SearchResult(recipes, null, null, "1", null);
        mSearches.add(sr);

        mAdapter = new MyRecyclerViewAdapter(this, mSearches);
        mSearchRecyclerView.setAdapter(mAdapter);


        // For the findviewbyID methods = initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_about:
                Snackbar.make(findViewById(R.id.mainLayout),
                        "About Selected", Snackbar.LENGTH_LONG )
                        .setAction("Action", null).show();
                return true;
            case R.id.action_swipe:
                Intent intent = new Intent(this, SwiperActivity.class);
                startActivity(intent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + (position+1));
                mAdapter.updateImage(position);
                mAdapter.notifyDataSetChanged();

            }
        });
    }
}
