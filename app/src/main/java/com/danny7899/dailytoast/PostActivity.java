package com.danny7899.dailytoast;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PostActivity extends ActionBarActivity {

    private Toolbar toolbar;

    private String postID;
    private String postTitle;
    private String postAuthor;
    private String postTime;
    private String postDesc;
    private String postCategory;
    private String postViewsInt;
    private String postViewsDesc;
    private String postCommentsInt;
    private String postCommentsDesc;
    private String postCont;

    public ArrayList<DataDetails> dataDetails = new ArrayList<DataDetails>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "PostActivity";

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        spinner = (ProgressBar)findViewById(R.id.progressSpinner);

        mLayoutManager = new LinearLayoutManager(PostActivity.this);

        mRecyclerView = (RecyclerView) findViewById(R.id.postCardView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PostCardAdapter(dataDetails);
        mRecyclerView.setAdapter(null);

        Intent i = getIntent();
        postID = i.getStringExtra("postID");
        postTitle = i.getStringExtra("postTitle");
        postAuthor= i.getStringExtra("postAuthor");
        postTime = i.getStringExtra("postTime");
        postDesc = i.getStringExtra("postDesc");
        postCategory = i.getStringExtra("postCategory");
        postViewsInt = i.getStringExtra("postViewsInt");
        postViewsDesc = i.getStringExtra("postViewsDesc");
        postCommentsInt = i.getStringExtra("postCommentsInt");
        postCommentsDesc = i.getStringExtra("postCommentsDesc");

        //Setting up toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setToolBarTitle(postTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DataFetchCheck();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post, menu);
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


    private void DataFetchCheck() {

        //Check for internet connection
        if (isConnectedToInternet()) {
            // Internet Connection is Present
            // make HTTP requests
            new GetPostDetails().execute();
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            showAlertDialog(PostActivity.this, "Connection Error",
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

    private class GetPostDetails extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            spinner.setVisibility(View.VISIBLE);
        }

        protected Void doInBackground(Void... arg0) {
            //Will call HTTP here
            //or any other pre-execution tasks
            getPostCont();

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            spinner.setVisibility(View.GONE);

            DataDetails obj = new DataDetails(postID, postTitle, postAuthor, postTime, postDesc, postCont, postCategory, postViewsInt, postViewsDesc, postCommentsInt, postCommentsDesc);
            dataDetails.add(obj);

            mAdapter = new PostCardAdapter(dataDetails);
            mRecyclerView.setAdapter(mAdapter);


        }
    }

    public void getPostCont() {



        JSONParser jParser = new JSONParser();

        String url_all_products = "http://www.dailytoast.dx.am/android_connect/get_product_details.php";

        JSONArray products = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("pid", postID));

        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

        // Check your log cat for JSON response
        Log.d("Fetched Content: ", json.toString());

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt("success");

            if (success == 1) {
                // products found
                // Getting Array of Products
                products = json.getJSONArray("products");
                JSONObject c = products.getJSONObject(0);

                postCont = c.getString("content");

                Log.d("Content: ", postCont);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setToolBarTitle(String title){toolbar.setTitle(title);}
}
