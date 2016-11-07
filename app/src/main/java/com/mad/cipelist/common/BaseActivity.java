package com.mad.cipelist.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.cipelist.R;
import com.mad.cipelist.main.MainActivity;
import com.mad.cipelist.services.model.LocalRecipe;
import com.mad.cipelist.services.model.LocalSearch;
import com.mad.cipelist.swiper.SearchFilterActivity;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Defines the base activity which extends certain activities in the application.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected AVLoadingIndicatorView mAvi;
    protected TextView mLoadTxt;
    protected FirebaseAuth mAuth;
    protected Toolbar toolbar;
    protected TextView mDrawerHeaderTv;
    protected DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_drawer);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mAuth = FirebaseAuth.getInstance();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_swiper:
                        Intent searchIntent = new Intent(getApplicationContext(), SearchFilterActivity.class);
                        startActivity(searchIntent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_home:
                        if (!(BaseActivity.this instanceof MainActivity)) {
                            finish();
                        }
                        drawerLayout.closeDrawers();
                        break;


                }
                return false;
            }
        });
    }

    public void startLoadAnim(AVLoadingIndicatorView avi, TextView textView, String msg) {
        avi.smoothToShow();
        textView.setText(msg);
        textView.setVisibility(View.VISIBLE);
    }

    public void stopLoadAnim(AVLoadingIndicatorView avi, TextView textView) {
        avi.smoothToHide();
        textView.setVisibility(View.GONE);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    public String getUserId() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return getString(R.string.default_user);
        }
    }

    public String getUserEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            return user.getEmail();
        } else {
            return getString(R.string.anon);
        }
    }

    public void deleteSearch(String searchId) {
        LocalSearch.deleteAll(LocalSearch.class, getString(R.string.query_search_id), searchId);
        LocalRecipe.deleteAll(LocalRecipe.class, getString(R.string.query_search_id), searchId);
    }
}
