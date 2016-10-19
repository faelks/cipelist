package com.mad.cipelist.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.cipelist.R;
import com.mad.cipelist.common.LocalSearch;
import com.mad.cipelist.common.Utils;
import com.mad.cipelist.main.adapter.MainRecyclerViewAdapter;
import com.mad.cipelist.settings.SettingsActivity;
import com.mad.cipelist.swiper.SwiperActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Displays the initial landing page with previous searches.
 * This method needs to load all searches associated with the
 * user and display them in the recycler view. Currently the
 * view is populated with default items.
 */
public class MainActivity extends AppCompatActivity {

    private static String LOG_TAG = "MainActivity";
    private RecyclerView mSearchRecyclerView;
    private MainRecyclerViewAdapter mAdapter;
    private List<LocalSearch> mLocalSearches;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utils.resetDatabase(this.getApplicationContext());
        mSearchRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (mAdapter == null) {
            mAdapter = new MainRecyclerViewAdapter(this, mLocalSearches);
            mSearchRecyclerView.setAdapter(mAdapter);
        }
        // For the findviewbyID methods = initialize();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!= null) {
                    // User is signed in
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


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
                Log.d(LOG_TAG, "Starting swiper for 7 recipes");
                intent.putExtra("recipeAmount", 7);
                startActivity(intent);
                return true;
            case R.id.action_anon_login:
                mAuth.signInAnonymously()
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(LOG_TAG, "signInAnonymously:onComplete:" + task.isSuccessful());
                                Toast.makeText(MainActivity.this, "Logged in as Anonymous",
                                        Toast.LENGTH_SHORT).show();

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w(LOG_TAG, "signInAnonymously", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
                return true;

        }

        try {
            mLocalSearches = getLocalSearches();
        } catch (Exception e) {
            Log.d("ERROR", "Couldn't load local searches, could be empty");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setOnItemClickListener(new MainRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + (position+1));
                mAdapter.notifyDataSetChanged();

            }
        });

        try {
            mLocalSearches = getLocalSearches();
            Log.d("Success!", "Loaded a local search into memory");
        } catch (Exception e) {
            Log.d("ERROR", "Couldn't load local searches, could be empty");
        }

        if (mLocalSearches != null) {
            mAdapter = new MainRecyclerViewAdapter(this, mLocalSearches);
            mSearchRecyclerView.setAdapter(mAdapter);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public List<String> removeDuplicates(List<String> ingredients) {
        Set<String> noDups = new HashSet<>();
        noDups.addAll(ingredients);
        return new ArrayList<>(noDups);
    }

    public List<LocalSearch> getLocalSearches() {
        return LocalSearch.listAll(LocalSearch.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }
}
