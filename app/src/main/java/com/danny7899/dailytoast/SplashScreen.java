package com.danny7899.dailytoast;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashScreen extends Activity {

    ArrayList<DataObject> dataSet;
    ArrayList<CategoryData> categorySet;

    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        //Show Splash Screen while fetching data
        DataFetchCheck();

    }

    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        // flag for Internet connection status
        Boolean isInternetPresent = false;

        // Connection detector class
        ConnectionDetector cd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Before making HTTP calls
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            //Will call HTTP here
            //or any other pre-execution tasks

            dataSet = getDataSet();
            categorySet = getCategoryData();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (session.isLoggedIn()) {
                // User is already logged in. Take him to main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                i.putExtra("dataSet1", new DataWrapper(dataSet));
                i.putExtra("categorySet", new CategoryWrapper(categorySet));
                Log.i("SplashScreen", "Both ArrayList Placed, starting MainActivity, already logged in");
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                i.putExtra("FROM_SPLASH", true);
                i.putExtra("dataSet1", new DataWrapper(dataSet));
                i.putExtra("categorySet", new CategoryWrapper(categorySet));
                Log.i("SplashScreen", "Both ArrayList Placed, starting LoginActivity, not logged in");
                startActivity(i);
                finish();
            }
        }
    }

    private void DataFetchCheck() {

        //Check for internet connection
        if (isConnectedToInternet()) {
            // Internet Connection is Present
            // make HTTP requests
            new PrefetchData().execute();
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            showAlertDialog(SplashScreen.this, "Connection Error",
                    "Error occurred while connecting to the server. Please check your internet connection and try again.");
        }

    }

    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    public ArrayList<DataObject> getDataSet() {
        ArrayList<DataObject> dataObject = new ArrayList<DataObject>();

        JSONParser jParser = new JSONParser();

        String url_all_products = "http://www.dailytoast.dx.am/android_connect/get_all_products.php";

        JSONArray products = null;

        ArrayList<NameValuePair> params;
        params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

        // Check your log cat for JSON response
        Log.d("All Products: ", json.toString());

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt("success");

            if (success == 1) {
                // products found
                // Getting Array of Products
                products = json.getJSONArray("products");

                // looping through All Products
                for (int i = 0; i < products.length(); i++) {
                    JSONObject c = products.getJSONObject(i);

                    Integer postID = c.getInt("id");
                    String title = c.getString("name");
                    String postDesc = c.getString("description");
                    String author = c.getString("created_by");
                    String category = c.getString("category");
                    String time = c.getString("created_at");
                    String commentsDesc;
                    Integer commentsInt = c.getInt("comment");
                    String viewsDesc;
                    Integer viewsInt = c.getInt("views");
                    //Integer ratingInt = c.getInt("rating");

                    if (viewsInt == 1){
                        viewsDesc =" view";
                    } else {
                        viewsDesc = " views";
                    }

                    if (commentsInt == 1){
                        commentsDesc =" comment";
                    } else {
                        commentsDesc = " comments";
                    }

                    DataObject obj = new DataObject(postID, title, author, time, postDesc, category, viewsInt, viewsDesc, commentsInt, commentsDesc, 0);
                    dataObject.add(i, obj);

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataObject;
    }

    public ArrayList<CategoryData> getCategoryData() {

        ArrayList<CategoryData> categoryData = new ArrayList<CategoryData>();

        JSONParser jParser = new JSONParser();

        String url_all_products = "http://www.dailytoast.dx.am/android_connect/get_all_categories.php";

        JSONArray categories = null;

        ArrayList<NameValuePair> params;
        params = new ArrayList<NameValuePair>();

        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

        // Check your log cat for JSON response
        Log.d("All Categories: ", json.toString());

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt("success");

            if (success == 1) {
                // Categories found
                // Getting Array of Categories
                categories = json.getJSONArray("categories");

                // looping through All Categories
                for (int i = 0; i < categories.length(); i++) {
                    JSONObject c = categories.getJSONObject(i);

                    Integer catID = c.getInt("id");
                    String catName = c.getString("name");
                    Integer catCount = c.getInt("count");

                    CategoryData obj = new CategoryData(catID, catName, catCount);
                    categoryData.add(i, obj);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return categoryData;
    }

    public void showAlertDialog(Context context, String title, String message) {
        /**
        * Function to display simple Alert Dialog
        * @param context - application context
        * @param title - alert dialog title
        * @param message - alert message
        * @param status - success/failure (used to set icon)
        **/

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DataFetchCheck();
            }
        });

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                          @Override
                                          public void onShow(DialogInterface arg0) {
                                              alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                                          }
                                      });

                // Showing Alert Message
                alertDialog.show();
    }

}

















/**
 * Created by danny7899 on 12/17/15.
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;

import com.danny7899.dailytoast.MainActivity;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {



            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Log.i("GETDATA", "Begins");
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                //i.putExtra("data", new DataWrapper(getDataSet()));
                i.putParcelableArrayListExtra("data", getDataSet());
                startActivity(i);

                // close this activity
                //finish();
            }
        }, SPLASH_TIME_OUT);
    }

    public ArrayList<DataObject> getDataSet(){

        enableStrictMode();

        ArrayList<DataObject> results = new ArrayList<DataObject>();

        JSONParser jParser = new JSONParser();

        String url_all_products = "http://www.dailytoast.dx.am/android_connect/get_all_products.php";

        JSONArray products = null;

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

        // Check your log cat for JSON response
        Log.d("All Products: ", json.toString());

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt("success");

            if (success == 1) {
                // products found
                // Getting Array of Products
                products = json.getJSONArray("products");

                // looping through All Products
                for (int i = 0; i < products.length(); i++) {
                    JSONObject c = products.getJSONObject(i);

                    String title = c.getString("name");
                    String postDesc = c.getString("description");
                    String author = c.getString("created_by");
                    //String category = c.getString("category");
                    String time = c.getString("created_at");
                    String viewsCheck = c.getString("views");
                    String comment = c.getString("comment");

                    if (viewsCheck == "1"){
                        viewsCheck = viewsCheck + " view";
                    } else {
                        viewsCheck = viewsCheck + " views";
                    }

                    DataObject obj = new DataObject(title, postDesc, viewsCheck, comment, time, "Category N/A", author);
                    results.add(i, obj);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

}**/

