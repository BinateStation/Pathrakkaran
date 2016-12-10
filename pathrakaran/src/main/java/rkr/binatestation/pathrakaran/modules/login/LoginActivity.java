package rkr.binatestation.pathrakaran.modules.login;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import rkr.binatestation.pathrakaran.R;
import rkr.binatestation.pathrakaran.activities.SplashScreen;
import rkr.binatestation.pathrakaran.modules.register.RegisterActivity;

import static android.Manifest.permission.READ_CONTACTS;
import static rkr.binatestation.pathrakaran.utils.Constants.REQUEST_READ_CONTACTS;
import static rkr.binatestation.pathrakaran.utils.GeneralUtils.alert;

/**
 * A login screen that offers login via email/phone number and password.
 */
public class LoginActivity extends AppCompatActivity implements LoginListeners.ViewListener, OnClickListener {

    private static final String TAG = "LoginActivity";
    LoginListeners.PresenterListener mPresenterListener;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private ContentLoadingProgressBar mProgressBar;

    private boolean isPresenterLive() {
        return mPresenterListener != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mPresenterListener = new LoginPresenter(this);
        // Set up the login form.
        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.L_progress_bar);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.L_username);
        mPasswordView = (EditText) findViewById(R.id.L_password);

        mProgressBar.hide();
        populateAutoComplete();

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    Log.d(TAG, "onEditorAction() called with: textView = [" + textView + "], id = [" + id + "], keyEvent = [" + keyEvent + "]");
                    if (isPresenterLive()) {
                        mPresenterListener.attemptLogin(
                                mEmailView.getText().toString().trim(),
                                mPasswordView.getText().toString().trim()
                        );
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void populateAutoComplete() {
        Log.d(TAG, "populateAutoComplete() called");
        if (mayRequestContacts() && isPresenterLive()) {
            mPresenterListener.populateAutoComplete(getSupportLoaderManager());
        }
    }

    private boolean mayRequestContacts() {
        Log.d(TAG, "mayRequestContacts() called");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.contacts_permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    }).show();
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult() called with: requestCode = [" + requestCode + "], permissions = [" + Arrays.toString(permissions) + "], grantResults = [" + Arrays.toString(grantResults) + "]");
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        Log.d(TAG, "addEmailsToAutoComplete() called with: emailAddressCollection = [" + emailAddressCollection + "]");
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                LoginActivity.this,
                android.R.layout.simple_dropdown_item_1line,
                emailAddressCollection
        );
        if (mEmailView != null) {
            mEmailView.setAdapter(adapter);
        }
    }

    @Override
    public void resetErrors() {
        // Reset errors.
        Log.d(TAG, "resetErrors() called");
        mProgressBar.show();
        if (mEmailView != null && mPasswordView != null) {
            mEmailView.setError(null);
            mPasswordView.setError(null);
        }
    }

    @Override
    public void passwordError() {
        mProgressBar.hide();
        if (mPasswordView != null) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordView.requestFocus();
        }
    }

    @Override
    public void usernameError() {
        mProgressBar.hide();
        if (mEmailView != null) {
            mEmailView.setError(getString(R.string.error_field_required));
            mEmailView.requestFocus();
        }
    }

    @Override
    public void onSuccessfulLogin(String message) {
        mProgressBar.hide();
        startActivity(new Intent(LoginActivity.this, SplashScreen.class));
        finish();
    }

    @Override
    public void onErrorLogin(String message) {
        mProgressBar.hide();
        alert(getContext(), "Error", message);
    }

    @Override
    public Context getContext() {
        return LoginActivity.this;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick() called with: v = [" + v + "]");
        switch (v.getId()) {
            case R.id.L_action_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
                break;
            case R.id.L_action_login:
                if (isPresenterLive()) {
                    mPresenterListener.attemptLogin(
                            mEmailView.getText().toString().trim(),
                            mPasswordView.getText().toString().trim()
                    );
                }
                break;

        }
    }
}

