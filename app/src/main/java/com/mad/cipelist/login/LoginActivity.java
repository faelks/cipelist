package com.mad.cipelist.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Allows the user to login with prerecorded or new details and also provides an anonymous option.
 * Password visibility can be toggled on and off.
 */

public class LoginActivity extends Activity {

    private final static String TAG = "LoginActivity";
    @BindView(R.id.input_email_layout)
    TextInputLayout inputEmailLayout;
    @BindView(R.id.input_email)
    TextInputEditText inputEmailEt;
    @BindView(R.id.input_password_layout)
    TextInputLayout inputPasswordLayout;
    @BindView(R.id.input_password)
    TextInputEditText inputPassordEt;
    @BindView(R.id.login_anonymously_tv)
    TextView anonymousLoginTv;
    @BindView(R.id.login_avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.login_load_text)
    TextView loadTv;
    @BindView(R.id.login_container)
    LinearLayout loginContainer;
    @BindView(R.id.btn_login)
    Button loginBtn;
    @BindView(R.id.btn_signup)
    Button signupBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @OnClick(R.id.login_anonymously_tv)
    public void loginAnonymously() {
        startLoadAnim(getString(R.string.logging_in));
        // Explicitly hide the keyboard
        Utils.hideSoftKeyboard(this);
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
                            stopLoadAnim();
                        }
                    }
                });
    }

    @OnClick(R.id.btn_login)
    public void login() {
        Utils.hideSoftKeyboard(this);
        signIn(inputEmailEt.getText().toString(), inputPassordEt.getText().toString());
    }

    @OnClick(R.id.btn_signup)
    public void signup() {
        Utils.hideSoftKeyboard(this);
        createUser(inputEmailEt.getText().toString(), inputPassordEt.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Get the firebase instance so that users can sign in
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    // Start the main activity and end the current login activity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        // Press the enter button on the keyboard and the login function is automatically called
        inputPassordEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT) {
                    login();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Called when the login button is pressed
     */
    public void signIn(String email, String password) {

        // Validate the form, make sure that fields are filled in
        if (!validateForm()) {
            return;
        }

        loginBtn.setEnabled(false);
        startLoadAnim(getString(R.string.logging_in));

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
                            Toast.makeText(LoginActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                            stopLoadAnim();
                            loginBtn.setEnabled(true);
                        }

                    }
                });
    }

    /**
     * Signup a user with provided email and password
     *
     * @param email    user email
     * @param password user password
     */
    public void createUser(String email, String password) {

        if (!validateForm()) {
            return;
        }

        signupBtn.setEnabled(false);
        startLoadAnim(getString(R.string.registering_user));
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, R.string.registration_failed, Toast.LENGTH_SHORT).show();
                            signupBtn.setEnabled(true);
                            stopLoadAnim();
                        }
                    }
                });
    }

    /**
     * Short function that validates the user input in the input fieldss
     *
     * @return validity of form
     */
    private boolean validateForm() {
        boolean valid = true;

        String email = inputEmailEt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            inputEmailLayout.setErrorEnabled(true);
            inputEmailLayout.setError(getString(R.string.need_email));
            valid = false;
        } else {
            inputEmailLayout.setError(null);
            inputEmailLayout.setErrorEnabled(false);
        }

        String password = inputPassordEt.getText().toString();
        if (TextUtils.isEmpty(password)) {
            inputPasswordLayout.setErrorEnabled(true);
            inputPasswordLayout.setError(getString(R.string.need_password));
            valid = false;
        } else {
            inputPasswordLayout.setError(null);
            inputPasswordLayout.setErrorEnabled(false);
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        // Disable going back button
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

    /**
     * Initiates a loading animation with a custom text and hides the content views
     *
     * @param msg custom text
     */
    public void startLoadAnim(String msg) {
        avi.smoothToShow();
        loadTv.setText(msg);
        loadTv.setVisibility(View.VISIBLE);
        loginContainer.setVisibility(View.INVISIBLE);
    }

    /**
     * Stops the loading animation in case of failure and shows the content view.
     */
    public void stopLoadAnim() {
        avi.smoothToHide();
        loadTv.setVisibility(View.INVISIBLE);
        loginContainer.setVisibility(View.VISIBLE);
    }
}
