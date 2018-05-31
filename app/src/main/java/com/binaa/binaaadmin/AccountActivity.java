package com.binaa.binaaadmin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.binaa.binaaadmin.models.UserModel;
import com.binaa.binaaadmin.server.ContentVolley;
import com.binaa.binaaadmin.utils.ApplicationBase;
import com.binaa.binaaadmin.utils.SettingsManager;


public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private View viewLoading;
    private TextView textViewTitle;
    private EditText editTextEMail;
    private EditText editTextPassword;
    private Button buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_account);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setToolBar();
        viewLoading = findViewById(R.id.viewLoading);
        editTextEMail = findViewById(R.id.editTextEMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);

        buttonSignIn.setOnClickListener(this);
        setActivityTitle(getString(R.string.sign_in));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }


    public void setLoading(boolean isLoading) {
        viewLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void setToolBar() {

        Toolbar toolBar = findViewById(R.id.toolbar);
        View actionBarView = getLayoutInflater().inflate(R.layout.toolbar_customview, toolBar, false);
        textViewTitle = actionBarView.findViewById(R.id.textViewActivityTitle);

        setSupportActionBar(toolBar);

        toolBar.setPadding(0, getStatusBarHeight(), 0, 0);


        // Set up the drawer.
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(actionBarView);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

    }

    public void setActivityTitle(String title) {
        if (textViewTitle != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textViewTitle.setText(Html.fromHtml("<font color='#ffffff'>" + title + "</font>", Html.FROM_HTML_OPTION_USE_CSS_COLORS));
            } else {
                //noinspection deprecation
                textViewTitle.setText(Html.fromHtml("<font color='#ffffff'>" + title + "</font>"));
            }
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = (int) getResources().getDimension(resourceId);
        }
        return result;
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignIn) {

            boolean dataIsSufficient;
            boolean userWarned = false;
            String email = editTextEMail.getText() + "";

            dataIsSufficient = email.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9_-]+([.][a-zA-Z0-9]+)+");
            if (!dataIsSufficient) {
                userWarned = true;
                Toast.makeText(this, "Mail is not valid format", Toast.LENGTH_LONG).show();
            }

            String password = editTextPassword.getText() + "";
            dataIsSufficient &= password.length() > 5;
            if (!dataIsSufficient && !userWarned) {
                Toast.makeText(this, "Password is too short min 6 characters", Toast.LENGTH_LONG).show();
            }

            if (dataIsSufficient) {
                Account account = new Account();
                account.logIn(email, password);
            }
        }
    }


    private class Account extends ContentVolley {

        public Account() {
            super("AccountActivity", AccountActivity.this);
        }


        @Override
        public void onPreExecute(ActionType actionType) {
            setLoading(true);
        }

        @Override
        protected void onPostExecuteUser(ActionType actionType, boolean succsess, String message, UserModel user) {
            setLoading(false);
            SettingsManager settingsManager = new SettingsManager(AccountActivity.this);
            if (succsess) {
                String registrationToken = ApplicationBase.getInstance().getRegistrationToken();
                addRegistration(registrationToken);

                settingsManager.updateUser(user);
                settingsManager.setUserToken(user.getToken());
                settingsManager.setLoggedIn(true);
                settingsManager.setPassword(editTextPassword.getText().toString());

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPostExecute(ActionType actionType, boolean succsess, String message) {
            setLoading(false);
            if (succsess) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

}
