package com.danny7899.dailytoast;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;;

import java.util.ArrayList;
import java.util.Collections;

import java.util.HashMap;
import java.util.Map;

import com.danny7899.dailytoast.AppConfig;
import com.danny7899.dailytoast.AppController;
import com.danny7899.dailytoast.SQLiteHandler;
import com.danny7899.dailytoast.SessionManager;


public class MainActivity extends ActionBarActivity {

    private static String LOG_TAG = "MainActivity";

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private FragmentTransaction tx;

    public static DataWrapper dataWrapper;
    public static CategoryWrapper categoryWrapper;
    public static ArrayList<DataObject> dataSet;
    public static ArrayList<CategoryData> categorySet;
    public static ArrayList<DataObject> dataSetRecent;
    public static ArrayList<DataObject> dataSetFeatured;
    public static ArrayList<DataObject> dataSetTrending;

    private ProgressDialog pDialog;
    public SessionManager session;
    private SQLiteHandler db;

    private SearchView mSearchView;
    private MenuItem searchMenuItem;
    private SearchView.OnQueryTextListener listener;

    private long lastPress;

    static class DataSecure {
        private static ArrayList<DataObject> storedData;

        public static void setData(ArrayList<DataObject> myData) {
            storedData = myData;
        }

        public static ArrayList<DataObject> getData() {
            return storedData;
        }
    }

    static class CategorySecure {
        private static ArrayList<CategoryData> storedData;

        public static void setData(ArrayList<CategoryData> myData) {
            storedData = myData;
        }

        public static ArrayList<CategoryData> getData() {
            return storedData;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "MainActivity Started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(MainActivity.this);
        db = new SQLiteHandler(MainActivity.this);

        // Progress dialog
        pDialog = new ProgressDialog(this,R.style.AppTheme_Dialog);
        pDialog.setCancelable(false);

        tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.frame, new HomeFragment());

        if (getIntent().getSerializableExtra("dataSet1") != null || getIntent().getSerializableExtra("categorySet") != null) {
            //Obtaining dataSets for Toasts and Category
            Log.i("MainActivity", "About to obtain ArrayList from Intent");
            dataWrapper = (DataWrapper) getIntent().getSerializableExtra("dataSet1");
            categoryWrapper = (CategoryWrapper) getIntent().getSerializableExtra("categorySet");
            dataSet = dataWrapper.getDataObjects();
            categorySet = categoryWrapper.getCategoryData();
            DataSecure.setData(dataSet);
            CategorySecure.setData(categorySet);
            tx.commit();
        } else {
            Boolean refresh;
            refresh = getIntent().getBooleanExtra("REFRESH", false);
            Log.i("MainActivity", "TODO REFRESH onCreate is " + refresh.toString());
            if (refresh == true) {
                Log.i("MainActivity", "About to refresh Toast ArrayList");
                DataFetchCheck();
                //Commit located on PostExecute
            } else {
                Log.i("MainActivity", "About to obtain ArrayList from Cache");
                dataSet = DataSecure.getData();
                categorySet = CategorySecure.getData();
                tx.commit();
            }
        }

        dataSetRecent = dataSet;
        dataSetFeatured = dataSet;
        dataSetTrending = dataSet;

        Log.i("OA", "Test VALUE on Creation of MainActivity");
        if (dataSet != null) {
            Log.i("OA", "FILLED");
        } else {
            Log.i("OA", "EMPTY");
        }

        //Setting up toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setToolBarTitle("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    //Replacing the main content with Section Fragments;
                    case R.id.home_section:
                        //Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_SHORT).show();
                        HomeFragment homeFragment = new HomeFragment();
                        android.support.v4.app.FragmentTransaction homeFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        homeFragmentTransaction.replace(R.id.frame, homeFragment);
                        homeFragmentTransaction.commit();
                        setToolBarTitle("Home");
                        return true;

                    case R.id.category_section:
                        //Toast.makeText(getApplicationContext(), "Category Selected", Toast.LENGTH_SHORT).show();
                        CategoryFragment categoryFragment = new CategoryFragment();
                        android.support.v4.app.FragmentTransaction categoryFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        categoryFragmentTransaction.replace(R.id.frame, categoryFragment);
                        categoryFragmentTransaction.commit();
                        setToolBarTitle("Category");
                        return true;

                    case R.id.account_section:
                        //Toast.makeText(getApplicationContext(), "Account Selected", Toast.LENGTH_SHORT).show();
                        AccountFragment accountFragment = new AccountFragment();
                        android.support.v4.app.FragmentTransaction accountFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        accountFragmentTransaction.replace(R.id.frame, accountFragment);
                        accountFragmentTransaction.commit();
                        setToolBarTitle("Account");
                        return true;

                    case R.id.webpage_section:
                        Uri uri = Uri.parse("http://www.dailytoast.dx.am"); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        tx.replace(R.id.frame, new HomeFragment());
                        return true;

                    case R.id.about_section:
                        //Toast.makeText(getApplicationContext(), "About Selected", Toast.LENGTH_SHORT).show();
                        AboutFragment aboutFragment = new AboutFragment();
                        android.support.v4.app.FragmentTransaction aboutFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        aboutFragmentTransaction.replace(R.id.frame, aboutFragment);
                        aboutFragmentTransaction.commit();
                        setToolBarTitle("About");
                        return true;

                    case R.id.preferences_section:
                        //Toast.makeText(getApplicationContext(), "Preferences Selected", Toast.LENGTH_SHORT).show();
                        PreferencesFragment preferencesFragment = new PreferencesFragment();
                        android.support.v4.app.FragmentTransaction preferencesFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        preferencesFragmentTransaction.replace(R.id.frame, preferencesFragment);
                        preferencesFragmentTransaction.commit();
                        setToolBarTitle("Preferences");
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle;
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we don't want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we don't want anything to happen so we leave this blank

                hideKeyboard();

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        navigationView.getMenu().getItem(0).setChecked(true);

        listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // newText is text entered by user to SearchView
                Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_LONG).show();
                return false;
            }
        };

    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPress > 3000) {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_LONG).show();
            lastPress = currentTime;
        } else {
            super.onBackPressed();
        }
    }

    public void setToolBarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(listener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.action_settings:
                return true;

            case R.id.action_search:
                return true;

            case R.id.action_refresh:
                refreshActivity();
                return true;

            case R.id.action_reset:
                Intent i = new Intent(getApplicationContext(), SplashScreen.class);
                startActivity(i);
                finish();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private class ReFetchData extends AsyncTask<Void, Void, Void> {

        // flag for Internet connection status
        Boolean isInternetPresent = false;

        // Connection detector class
        ConnectionDetector cd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Before making HTTP calls
            pDialog.setMessage("Reloading Toasts...");
            showDialog();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            //Will call HTTP here
            //or any other pre-execution tasks

            dataSet = getDataSet();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            DataSecure.setData(dataSet);
            CategorySecure.setData(categorySet);

            Intent reStart = new Intent(MainActivity.this, MainActivity.class);
            reStart.putExtra("RESTART", false);
            hideDialog();
            finish();
            startActivity(reStart);

        }
    }

    private void DataFetchCheck() {

        //Check for internet connection
        if (isConnectedToInternet()) {
            // Internet Connection is Present
            // make HTTP requests
            new ReFetchData().execute();
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            showAlertDialog(MainActivity.this, "Connection Error",
                    "Error occurred while connecting to the server. Please check your internet connection and try again.");
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

                    if (viewsInt == 1) {
                        viewsDesc = " view";
                    } else {
                        viewsDesc = " views";
                    }

                    if (commentsInt == 1) {
                        commentsDesc = " comment";
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void refreshActivity() {
        Intent restart = new Intent(MainActivity.this, MainActivity.class);
        restart.putExtra("REFRESH", true);
        finish();
        startActivity(restart);
    }

}