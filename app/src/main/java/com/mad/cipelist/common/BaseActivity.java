package com.mad.cipelist.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.cipelist.R;
import com.mad.cipelist.main.MainActivity;
import com.mad.cipelist.swiper.SearchFilterActivity;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Defines the base activity which extends certain activities in the application.
 * TODO: Add more base activities, like loader, animations, menu things? Settings?
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected AVLoadingIndicatorView mAvi;
    protected TextView mLoadTxt;
    protected FirebaseAuth mAuth;
    protected Toolbar toolbar;
    protected TextView mDrawerHeaderTv;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_drawer);

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
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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

    public void startLoadAnim(String msg) {
        mAvi.smoothToShow();
        mLoadTxt.setText(msg);
        mLoadTxt.setVisibility(View.VISIBLE);
    }

    public void stopLoadAnim() {
        mAvi.smoothToHide();
        mLoadTxt.setVisibility(View.GONE);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    public String getUserId() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return "Default";
        }
    }

    public String getUserEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            return user.getEmail();
        } else {
            return "Anonymous";
        }
    }

    public void crossfade(View displayedView, final View toBeLoadedView) {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        displayedView.setAlpha(0f);
        displayedView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        displayedView.animate()
                .alpha(1f)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        toBeLoadedView.animate()
                .alpha(0f)
                .setDuration(android.R.integer.config_shortAnimTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toBeLoadedView.setVisibility(View.GONE);
                    }
                });
    }



}
