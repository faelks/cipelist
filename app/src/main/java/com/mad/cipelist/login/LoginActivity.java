package com.mad.cipelist.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.cipelist.R;
import com.mad.cipelist.common.Utils;
import com.mad.cipelist.main.MainActivity;
import com.wang.avi.AVLoadingIndicatorView;


/**
 * Allows the user to login with prerecorded or new details and also provides an anonymous option.
 */

public class LoginActivity extends Activity {

    private final static String TAG = "LoginActivity";

    private TextInputEditText mInputEmailEt;
    private TextInputEditText mInputPasswordEt;
    private ScrollView mLoginContainer;

    private AVLoadingIndicatorView mAvi;
    private TextView mLoadTxt;

    private Button mLoginBtn;
    private Button mSignupBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Views
        TextView mAnonymousLoginTv = (TextView) findViewById(R.id.link_anon);
        mInputEmailEt = (TextInputEditText) findViewById(R.id.input_email);
        mInputPasswordEt = (TextInputEditText) findViewById(R.id.input_password);
        mAvi = (AVLoadingIndicatorView) findViewById(R.id.login_avi);
        mLoadTxt = (TextView) findViewById(R.id.login_load_text);
        mLoginContainer = (ScrollView) findViewById(R.id.login_container);

        // Buttons
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        mSignupBtn = (Button) findViewById(R.id.btn_signup);


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(mInputEmailEt.getText().toString(), mInputPasswordEt.getText().toString());
            }
        });

        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(mInputEmailEt.getText().toString(), mInputPasswordEt.getText().toString());
            }
        });

        mAnonymousLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginContainer.setVisibility(View.INVISIBLE);
                Utils.hideSoftKeyboard(LoginActivity.this);
                startLoadAnim("Logging in");
                mAuth.signInAnonymously()
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInAnonymously", task.getException());
                                    mLoginContainer.setVisibility(View.VISIBLE);
                                }
                                stopLoadAnim();
                            }
                        });
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    String username = (user.getEmail() == null) ? "Anonymous" : user.getEmail();
                    showToast("Signed in as: " + username);

                    // Start the main activity and end the current login activity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

    }

    /**
     * Called when the login button is pressed. Needs to talk to the firebase setup
     * TODO: Need to add a timeout on the login
     */
    public void signIn(String email, String password) {

        if (!validateForm()) {
            return;
        }

        mLoginBtn.setEnabled(false);
        mLoginContainer.setVisibility(View.INVISIBLE);
        startLoadAnim("Logging in");
        Utils.hideSoftKeyboard(this);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            showToast("Authentication Failed");
                            mLoginContainer.setVisibility(View.VISIBLE);
                        }
                        stopLoadAnim();
                        mLoginBtn.setEnabled(true);
                    }
                });
    }

    public void createUser(String email, String password) {

        if (!validateForm()) {
            return;
        }

        mSignupBtn.setEnabled(false);
        mLoginContainer.setVisibility(View.INVISIBLE);
        startLoadAnim("Registering User");
        Utils.hideSoftKeyboard(this);

        //TODO: Need to add a timeout on the createUser?
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            showToast("Registration Failed");
                            mSignupBtn.setEnabled(true);
                            mLoginContainer.setVisibility(View.VISIBLE);
                            stopLoadAnim();
                        }

                        // ...
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mInputEmailEt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mInputEmailEt.setError("Required");
            valid = false;
        } else {
            mInputEmailEt.setError(null);
        }

        String password = mInputPasswordEt.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mInputPasswordEt.setError("Required");
            valid = false;
        } else {
            mInputPasswordEt.setError(null);
        }

        // TODO add further validation of input

        return valid;
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
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

}
