package com.mad.cipelist.main;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.cipelist.R;
import com.mad.cipelist.common.BaseActivity;
import com.mad.cipelist.login.LoginActivity;
import com.mad.cipelist.main.adapter.MainSearchRecyclerViewAdapter;
import com.mad.cipelist.search_result.SearchResultActivity;
import com.mad.cipelist.services.model.LocalSearch;
import com.mad.cipelist.settings.SettingsActivity;
import com.mad.cipelist.swiper.SearchFilterActivity;
import com.mad.cipelist.swiper.SwiperActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Displays the initial landing page with previous searches.
 * This method needs to load all searches associated with the
 * user and display them in the recycler view.
 */
public class MainActivity extends BaseActivity {

    private static String LOG_TAG = "MainActivity";

    @Nullable
    @BindView(R.id.main_search_recycler_view)
    RecyclerView searchRecyclerView;
    @BindView(R.id.add_recipe_fab)
    FloatingActionButton addRecipeFab;
    @BindView(R.id.main_hint)
    TextView mainHint;
    private MainSearchRecyclerViewAdapter mAdapter;
    private List<LocalSearch> mLocalSearches;
    private String mCurrentUserId;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @OnClick(R.id.add_recipe_fab)
    public void onFabClick() {
        mainHint.setVisibility(View.INVISIBLE);
        startNewSearchActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_main, contentFrameLayout);
        ButterKnife.bind(this, contentFrameLayout);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        // Set the current user id
        mCurrentUserId = (mAuth.getCurrentUser() != null) ? mAuth.getCurrentUser().getUid() : getString(R.string.anon);

        // Initialise the layout manager and adapter for the main recycler view
        if (searchRecyclerView != null) {
            searchRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            createAdapter(MainActivity.this, mLocalSearches, mCurrentUserId, getUserEmail());
        }

        // Register a context menu with the recycler view that is displayed on long clicks
        registerForContextMenu(searchRecyclerView);

    }

    /**
     * Animation that displays a hint for the user after 5 seconds
     */
    private void runAnimation() {
        mainHint.setVisibility(View.INVISIBLE);
        Animation a = AnimationUtils.loadAnimation(this, R.anim.anim_hint);
        a.reset();
        a.setStartOffset(3000);
        mainHint.clearAnimation();
        mainHint.startAnimation(a);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position;
        try {
            position = mAdapter.getPosition();
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.ctx_menu_view:
                startNewResultActivity(mAdapter.getSearchId(position));
                break;
            case R.id.ctx_menu_delete:
                deleteSearch(mAdapter.getSearchId(position));
                mAdapter.deleteItem(position);
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Create an adapter for the recyclerview, allows you to reload the items shown
     *
     * @param activity  Current activity
     * @param searches  The current list of searches for the user
     * @param userId    the unique id of the user
     * @param userEmail the email of the user
     */
    private void createAdapter(Activity activity, List<LocalSearch> searches, String userId, String userEmail) {
        mAdapter = new MainSearchRecyclerViewAdapter(activity, searches, userId, userEmail);
        if (searchRecyclerView != null) {
            searchRecyclerView.setAdapter(mAdapter);
        }
    }

    /**
     * Starts a new search for recipes
     */
    public void startNewSearchActivity() {
        Intent intent = new Intent(this, SearchFilterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * Returns the email of the current user
     */
    public String getUserEmail() {
        return (mAuth.getCurrentUser() != null) ? mAuth.getCurrentUser().getEmail() : getString(R.string.n_a);
    }

    /**
     * Start a new result activity where the recipes and groceries of a search are displayed/
     *
     * @param searchId The unique search id
     */
    public void startNewResultActivity(String searchId) {
        Intent shoppingListIntent = new Intent(getApplicationContext(), SearchResultActivity.class);
        shoppingListIntent.putExtra(SwiperActivity.SEARCH_ID, searchId);
        startActivity(shoppingListIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * Retrieves all the searches that are related to the current user
     *
     * @param id the users id
     * @return relevant searches
     */
    public List<LocalSearch> getLocalSearches(String id) {
        return LocalSearch.find(LocalSearch.class, getString(R.string.query_user_id), id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_about:
                showToast(getString(R.string.about_selected));
                return true;
            case R.id.action_logout:
                mAuth.signOut();
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            case R.id.action_clear_searches:
                for (LocalSearch search : mLocalSearches) {
                    deleteSearch(search.searchId);
                }
                mLocalSearches.clear();
                createAdapter(MainActivity.this, null, mCurrentUserId, getUserEmail());
                runAnimation();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainHint.setVisibility(View.INVISIBLE);
        mAdapter.setOnItemClickListener(new MainSearchRecyclerViewAdapter.SearchClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                startNewResultActivity(mAdapter.getSearchId(position));
            }
        });

        try {
            mLocalSearches = getLocalSearches(mCurrentUserId);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "The database query returned a null exception");
            e.printStackTrace();
        } catch (SQLiteException e) {
            Log.d(LOG_TAG, "The database returned a SQLiteException");
            e.printStackTrace();
        }

        if (mLocalSearches != null && mLocalSearches.size() > 0) {
            createAdapter(MainActivity.this, mLocalSearches, mCurrentUserId, getUserEmail());
        } else {
            runAnimation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mainHint.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Add a listener that listens for logout events
        mAuth.addAuthStateListener(mAuthListener);
        mainHint.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        mainHint.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Log the user out since the application is destroyed
        mAuth.signOut();
    }
}
