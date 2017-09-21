package com.danny7899.dailytoast;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by danny7899 on 1/3/16.
 */
public class CreateActivity extends ActionBarActivity {

    private Toolbar toolbar;

    private Spinner catSpinner;
    private SpinnerAdapter spinnerAdapter;
    private EditText etTitle;
    private EditText etDesc;
    private EditText etCont;
    private Button btnUpload;

    private String formTitle;
    private String formCatID;
    private String formDesc;
    private String formCont;

    public static CategoryWrapper categoryWrapper;
    private ArrayList<CategoryData> categorySet;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    private static String LOG_TAG = "CreateActivity";

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        spinner = (ProgressBar) findViewById(R.id.progressSpinner);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Progress dialog
        pDialog = new ProgressDialog(this,R.style.AppTheme_Dialog);
        pDialog.setCancelable(false);

        etTitle = (EditText) findViewById(R.id.etTitle);
        catSpinner = (Spinner) findViewById(R.id.categorySpinner);
        etDesc = (EditText) findViewById(R.id.etDesc);
        etCont = (EditText) findViewById(R.id.etCont);
        btnUpload = (Button) findViewById(R.id.btnPost);

        //Setting up toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getSerializableExtra("categorySet") != null) {
            //Obtaining dataSet for Category
            Log.i(LOG_TAG, "About to obtain ArrayList from Intent");
            categoryWrapper = (CategoryWrapper) getIntent().getSerializableExtra("categorySet");
            categorySet = categoryWrapper.getCategoryData();
        } else {
            Log.i(LOG_TAG, "ArrayList Empty!");
        }

        //Converting ArrayList Category Titles to String array
        Integer arraySize = categorySet.size();
        final String[] catNameArray = new String[arraySize];
        final String[] catIDArray = new String[arraySize];
        Log.i(LOG_TAG, "String[] is set to have " + arraySize.toString() + " children");
        for (int i = 0; i < arraySize; i++) {
            catNameArray[i] = categorySet.get(i).getmCatName();
            catIDArray[i] = categorySet.get(i).getmCatID();
        }

        Spinner spinner = (Spinner) findViewById(R.id.categorySpinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, catNameArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        //spinner.setAdapter(spinnerArrayAdapter);
        spinner.setPrompt("Select a Category");

        spinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        spinnerArrayAdapter,
                        R.layout.contact_spinner_row_nothing_selected,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0)  {formCatID = "0";} else {
                    Integer pos = new Integer(position - 1);
                    formCatID = catIDArray[pos];
                }
                //Toast.makeText(getApplicationContext(), "Selected with ID " + formCatID, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //Toast.makeText(getApplicationContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();
            }

        });

        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                formTitle = etTitle.getText().toString().trim();
                formDesc = etDesc.getText().toString().trim();
                formCont = etCont.getText().toString();
                if (formTitle.matches("") || formDesc.matches("") || formCont.matches("")) {
                    Toast.makeText(getApplicationContext(), "Please enter all the required information", Toast.LENGTH_SHORT).show();
                } else {
                    if (formCatID == "0") {
                        Toast.makeText(getApplicationContext(), "Please choose a Category", Toast.LENGTH_SHORT).show();
                    } else {

                        //EXECUTE UPLOADING TASK
                        DataUploadCheck();

                    }
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
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

    public void setToolBarTitle(String title) {
        toolbar.setTitle(title);
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void DataUploadCheck() {

        //Check for internet connection
        if (isConnectedToInternet()) {
            // Internet Connection is Present
            // make HTTP requests
            new UploadData().execute();
        } else {
            // Internet connection is not present
        }

    }

    private class UploadData extends AsyncTask<Void, Void, Void> {

        // flag for Internet connection status
        Boolean isInternetPresent = false;

        // Connection detector class
        ConnectionDetector cd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Before making HTTP calls
            pDialog.setMessage("Uploading Toast...");
            showDialog();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            //Will call HTTP here
            //or any other pre-execution tasks
            PostToast();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Intent i = new Intent(CreateActivity.this, MainActivity.class);
            i.putExtra("REFRESH", true);
            Log.i("SplashScreen", "Post uploaded, starting MainActivity, will reload data");
            hideDialog();
            startActivity(i);
            finish();
        }
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public void PostToast() {
        JSONParser jParser = new JSONParser();
        String url_all_products = "http://www.dailytoast.dx.am/android_connect/add_post.php";

        JSONArray products = null;

        HashMap<String, String> user = db.getUserDetails();

        ArrayList<NameValuePair> params;
        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("submit", "Submit"));
        params.add(new BasicNameValuePair("title", formTitle));
        params.add(new BasicNameValuePair("catID", formCatID));
        params.add(new BasicNameValuePair("desc", formDesc));
        params.add(new BasicNameValuePair("cont", formCont));
        params.add(new BasicNameValuePair("author", user.get("uid")));
        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

        // Check your log cat for JSON response
        Log.d("Response: ", json.toString());

        try {
            // Checking for SUCCESS TAG
            boolean success = json.getBoolean("error");

            if (success == false) {
                // products found
                // Getting Array of Product
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

