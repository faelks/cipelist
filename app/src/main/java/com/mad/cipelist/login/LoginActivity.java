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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.cipelist.R;
import com.mad.cipelist.main.MainActivity;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Allows the user to login with prerecorded or new details and also provides an anonymous option.
 */

public class LoginActivity extends Activity {

    private final static String TAG = "LoginActivity";
    @BindView(R.id.input_email)
    TextInputEditText mInputEmailEt;
    @BindView(R.id.input_password)
    TextInputEditText mInputPasswordEt;
    @BindView(R.id.login_anonymously_tv)
    TextView mAnonymousLoginTv;
    @BindView(R.id.login_avi)
    AVLoadingIndicatorView mAvi;
    @BindView(R.id.login_load_text)
    TextView mLoadTxt;
    @BindView(R.id.login_container)
    LinearLayout mLoginContainer;
    @BindView(R.id.btn_login)
    Button mLoginBtn;
    @BindView(R.id.btn_signup)
    Button mSignupBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @OnClick(R.id.login_anonymously_tv)
    public void loginAnonymously() {
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
                            stopLoadAnim();
                        }
                    }
                });
    }

    @OnClick(R.id.btn_login)
    public void login() {
        signIn(mInputEmailEt.getText().toString(), mInputPasswordEt.getText().toString());
    }

    @OnClick(R.id.btn_signup)
    public void signup() {
        createUser(mInputEmailEt.getText().toString(), mInputPasswordEt.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    String username = (user.getEmail() == null) ? "Anonymous" : user.getEmail();
                    Toast.makeText(LoginActivity.this, "Signed in as: " + username, Toast.LENGTH_SHORT).show();

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
     */
    public void signIn(String email, String password) {

        if (!validateForm()) {
            return;
        }

        mLoginBtn.setEnabled(false);
        startLoadAnim("Logging in");

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
                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            stopLoadAnim();
                            mLoginBtn.setEnabled(true);
                        }

                    }
                });
    }

    public void createUser(String email, String password) {

        if (!validateForm()) {
            return;
        }

        mSignupBtn.setEnabled(false);
        startLoadAnim("Registering User");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            mSignupBtn.setEnabled(true);
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
     * @param msg custom text
     */
    public void startLoadAnim(String msg) {
        mAvi.smoothToShow();
        mLoadTxt.setText(msg);
        mLoadTxt.setVisibility(View.VISIBLE);
        mLoginContainer.setVisibility(View.INVISIBLE);
    }

    /**
     * Stops the loading animation in case of failure and shows the content view.
     */
    public void stopLoadAnim() {
        mAvi.smoothToHide();
        mLoadTxt.setVisibility(View.INVISIBLE);
        mLoginContainer.setVisibility(View.VISIBLE);
    }
}
