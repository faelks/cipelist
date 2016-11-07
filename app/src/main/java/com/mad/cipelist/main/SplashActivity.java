package com.mad.cipelist.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mad.cipelist.login.LoginActivity;

/**
 * Displays the app logo on startup before the application is fully loaded
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Only used while testing with reoccuring restarts
        // Utils.resetDatabase(this.getApplicationContext());

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
