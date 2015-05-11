package com.grupp3.projekt_it;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class PlantSearchActivity extends BaseActivity {
    //initialing variables
    String TAG = "com.grupp3.projekt_it_sok";
    String searchQuery;
    Drawable searchIcon;
    Drawable closeSearchIcon;
    ListView listView;
    MenuItem searchItem;
    boolean SearchOpened;
    EditText SearchEditText;
    InputMethodManager inputManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        // set layout
        setContentView(R.layout.activity_plant_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // fetch resources
        searchIcon = getResources().getDrawable(R.drawable.ic_action_search);
        closeSearchIcon = getResources().getDrawable(R.drawable.ic_action_remove);
        listView = (ListView) findViewById(R.id.listView2);

        // needs savedInstance to work, if it was open when saved it will now be restored
        if (SearchOpened) {
            Log.i(TAG, "SearchOpened == True");
            openSearchBar(searchQuery);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Override
    protected void openActivity(int position) {
        Log.i(TAG, "openActivity(int position)");

        /**
         * All activities with navigation drawer must contain this override function in order
         * to not be able to launch another instance of itself from navigation bar.
         * Remove correct startActivity(..) call to do so.
         */

        //mDrawerList.setItemChecked(position, true);
        //setTitle(listArray[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        BaseActivity.position = position; //Setting currently selected position in this field so that it will be available in our child activities.

        switch (position) {
            case 0:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, MyGardenListActivity.class));
                break;
            case 2:
                break;
            case 3:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, Login.class));
                break;
            case 5:
                startActivity(new Intent(this, Preferences.class));
                break;
            default:
                break;
        }
    }


    // method for searching stuff
    public void search(){
        Log.i(TAG, "search()");

        closeKeyBoard();

        // get user search input
        Context context = getApplicationContext();

        // check network status, needs permissions see manifest
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                FragmentManager fragmentManager = getFragmentManager();
                //create new async task to download plants from database
                //from name query in sqlite database
                Activity activity = this;
                String urlPlant = SearchEditText.getText().toString();
                urlPlant = inputFilter(urlPlant);
                //Log.i(TAG, urlPlant);
                new DownloadPlant(context, listView, fragmentManager, activity, getLayoutInflater())
                        .execute("http://xn--trdgrdsappen-hcbq.nu/api" + "?name=" + urlPlant);
            } catch (Exception e) {
                e.printStackTrace();
                //Log.i(TAG, "Connected but failed anyway");
            }
        }
    }

    public void getSearchSuggestions() {
        Log.i(TAG, "getSearchSuggestions");
        // get user search input
        Context context = getApplicationContext();

        // check network status, needs permissions see manifest
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                FragmentManager fragmentManager = getFragmentManager();
                //create new async task to download plants from database
                //from name query in sqlite database
                Activity activity = this;
                String urlPlant = SearchEditText.getText().toString();
                urlPlant = inputFilter(urlPlant);
                //Log.i(TAG, urlPlant);
                new DownloadPlant(context, listView, fragmentManager, activity, getLayoutInflater())
                        .execute("http://xn--trdgrdsappen-hcbq.nu/api" + "?name=" + urlPlant);
            } catch (Exception e) {
                e.printStackTrace();
                //Log.i(TAG, "Connected but failed anyway");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu");
        //inflate menu
        getMenuInflater().inflate(R.menu.menu_plant_search, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.i(TAG, "onPrepareOptionsMenu");
        //code to change actionbar on search icon click
        searchItem = menu.findItem(R.id.search);
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        //closeKeyBoard();
        return true;
        //return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        Log.i(TAG, "inBackPressed");
        Intent intent = getIntent(); //get intent that started this activity
        setResult(RESULT_OK, intent); // set result
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected(MenuItem item)");
        int id = item.getItemId();
        if (id == R.id.search) {
            Log.i(TAG, "id == R.id.search");
            if (SearchOpened) {
                Log.i(TAG, "iSearchOpened");
                //close search bar
                closeKeyBoard();
                closeSearchBar();


            } else {
                Log.i(TAG, "id == R.id.search");
                //open search bar
                openSearchBar(searchQuery);
                openKeyBoard();
            }
            return true;
        }


        else if (id == R.id.action_settings) {
            Log.i(TAG, "id == R.id.action_settings");
            return true;
        }

        else if (id== android.R.id.home){
            Log.i(TAG, "id== android.R.id.home");
            onBackPressed();

        }

        return super.onOptionsItemSelected(item);
    }

    private void openSearchBar(String queryText) {
        Log.i(TAG, "openSearchBar(String queryText)");
        View view = this.getCurrentFocus();

        // Set custom view on action bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.search);

        // Search edit text field setup.
        SearchEditText = (EditText) actionBar.getCustomView().findViewById(R.id.editTextSearch);


        SearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.i(TAG, "actionId == EditorInfo.IME_ACTION_SEARCH ");

                    search();
                    return true;
                }
                return false;
            }
        });
        Log.i(TAG, "Search field text: " + SearchEditText.getText());
        //Auto search

        SearchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getSearchSuggestions();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        //SearchEditText.addTextChangedListener(new SearchWatcher());
        SearchEditText.setText(queryText);
        SearchEditText.requestFocus();

        // Change search icon accordingly.
        searchItem.setIcon(closeSearchIcon);
        SearchOpened = true;

    }

    private void closeSearchBar() {
        Log.i(TAG, "closeSearchBar");
        // Remove custom view.
        getSupportActionBar().setDisplayShowCustomEnabled(false);
        // Remove search results
        listView.setVisibility(View.GONE);
        // Change search icon accordingly.
        searchItem.setIcon(searchIcon);

        SearchOpened = false;
    }

    private String inputFilter(String input) {
        Log.i(TAG, "inputFilter");
        input = input.replaceAll("å", "a");
        input = input.replaceAll("ä", "a");
        input = input.replaceAll("ö", "o");
        return input.replaceAll("[^a-zA-Z ]", "");
    }

    private void closeKeyBoard(){
        Log.i(TAG, "closeKeyBoard");
        View view = this.getCurrentFocus();
        //InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void toggleKeyBoard(){
        Log.i(TAG, "toggleKeyBoard");
        View view = this.getCurrentFocus();
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        //inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        inputManager.toggleSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }


    private void openKeyBoard() {
        Log.i(TAG, "openKeyBoard");
        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }
}
