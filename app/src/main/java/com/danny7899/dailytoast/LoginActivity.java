package com.danny7899.dailytoast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.danny7899.dailytoast.R;
import com.danny7899.dailytoast.AppConfig;
import com.danny7899.dailytoast.AppController;
import com.danny7899.dailytoast.SQLiteHandler;
import com.danny7899.dailytoast.SessionManager;

public class LoginActivity extends ActionBarActivity {

    private Toolbar toolbar;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private Button btnGuest;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextInputLayout emailWrapper;
    private TextInputLayout passwordWrapper;

    private String PreviousActivity;

    public static DataWrapper dataWrapper;
    public static CategoryWrapper categoryWrapper;
    public static ArrayList<DataObject> dataSet;
    public static ArrayList<CategoryData> categorySet;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        emailWrapper = (TextInputLayout) findViewById(R.id.emailWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);

        emailWrapper.setHint("Email");
        passwordWrapper.setHint("Password");

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        btnGuest = (Button) findViewById(R.id.btnGuest);

        // Progress dialog
        pDialog = new ProgressDialog(this,R.style.AppTheme_Dialog);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        //Check if previous activity is Splash or Main
        Boolean previousState = getIntent().getBooleanExtra("FROM_SPLASH", false);
        Log.i("PreviousActivity is Splash is ", previousState.toString());
        if (previousState == true) {
            Log.i("On Login, PreviousActivity is", " splash");
            btnGuest.setVisibility(View.VISIBLE);
            PreviousActivity = "splash";
        } else {
            Log.i("On Login, PreviousActivity is", " not splash");
            btnGuest.setVisibility(View.INVISIBLE);
            PreviousActivity = "main";
        }

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                hideKeyboard();

                String email = emailWrapper.getEditText().getText().toString().trim();
                String password = passwordWrapper.getEditText().getText().toString().trim();


                emailWrapper.setErrorEnabled(false);
                passwordWrapper.setErrorEnabled(false);

                if (!validateEmail(email)) {
                    emailWrapper.setError("Not a valid email address!");
                }
                if (!validatePassword(password)) {
                    passwordWrapper.setError("Not a valid password!");
                }
                if (validateEmail(email) && validatePassword(password)) {
                    emailWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                    checkLogin(email, password);
                }

            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                /*Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);*/
                Uri uri = Uri.parse("http://dailytoast.dx.am/login/register.php"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        // Link to Register Screen
        btnGuest.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dataWrapper = (DataWrapper) getIntent().getSerializableExtra("dataSet1");
                categoryWrapper = (CategoryWrapper) getIntent().getSerializableExtra("categorySet");
                dataSet = dataWrapper.getDataObjects();
                categorySet = categoryWrapper.getCategoryData();

                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                i.putExtra("dataSet1", new DataWrapper(dataSet));
                i.putExtra("categorySet", new CategoryWrapper(categorySet));
                startActivity(i);
                finish();
            }
        });

    }

    public boolean validatePassword(String password) {
        return password.length() > 4;
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            //overridePendingTransition(R.anim.disappear, R.anim.appear);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    long lastPress;

    @Override
    public void onBackPressed() {
        if (PreviousActivity == "splash") {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPress > 3000) {
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_LONG).show();
                lastPress = currentTime;
            } else {
                super.onBackPressed();
            }
        } else {
            NavUtils.navigateUpFromSameTask(this);
        }
    }

    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String username = user.getString("username");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, username, email, uid, created_at);

                        if (PreviousActivity == "splash") {
                            dataWrapper = (DataWrapper) getIntent().getSerializableExtra("dataSet1");
                            categoryWrapper = (CategoryWrapper) getIntent().getSerializableExtra("categorySet");
                            dataSet = dataWrapper.getDataObjects();
                            categorySet = categoryWrapper.getCategoryData();

                            Intent i = new Intent(getApplicationContext(),
                                    MainActivity.class);
                            i.putExtra("dataSet1", new DataWrapper(dataSet));
                            i.putExtra("categorySet", new CategoryWrapper(categorySet));
                            startActivity(i);
                            finish();
                        } else {
                            Intent intent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        emailWrapper.setError("Email incorrect!");
                        passwordWrapper.setError("Password incorrect!");
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };
        AppController controller = new AppController();
        // Adding request to request queue
        controller.getInstance();
        controller.addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}