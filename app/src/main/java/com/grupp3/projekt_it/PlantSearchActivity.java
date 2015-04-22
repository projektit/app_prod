package com.grupp3.projekt_it;

import android.app.Notification;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;


public class PlantSearchActivity extends ActionBarActivity {
    String TAG = "com.grupp3.projekt_it";
    String searchQuery;
    Drawable searchIcon;
    Drawable closeSearchIcon;
    ListView listView;
    MenuItem searchItem;
    boolean SearchOpened;
    EditText SearchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_search);

        if (savedInstanceState == null) {
            //code if no previous search exists
            Log.i(TAG, "so far");
            SearchOpened = false;
            searchQuery = "";
        } else {
            //searchQuery = savedInstance.searchQuery
            //restore previous results
        }
        searchIcon = getResources().getDrawable(R.drawable.ic_action_search);
        closeSearchIcon = getResources().getDrawable(R.drawable.ic_action_remove);
        listView = (ListView) findViewById(R.id.listView1);

        if (SearchOpened) {
            openSearchBar(searchQuery);
        }

    }
    public void search(){
        String plant = SearchEditText.getText().toString();
        Context context = getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        TextView textView = (TextView) findViewById(R.id.textView);
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                new DownloadPlant(context, textView).execute("http://46.101.8.10/" + "?name=maskros");
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "Connected but failed anyway");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_plant_search, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchItem = menu.findItem(R.id.search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search) {
            if (SearchOpened) {
                closeSearchBar();
            } else {
                openSearchBar(searchQuery);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSearchBar(String queryText) {

        // Set custom view on action bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.search);

        // Search edit text field setup.
        SearchEditText = (EditText) actionBar.getCustomView().findViewById(R.id.editTextSearch);
        //
        SearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
        //SearchEditText.addTextChangedListener(new SearchWatcher());
        SearchEditText.setText(queryText);
        SearchEditText.requestFocus();

        // Change search icon accordingly.
        searchItem.setIcon(closeSearchIcon);
        SearchOpened = true;
    }

    private void closeSearchBar() {
        // Remove custom view.
        getSupportActionBar().setDisplayShowCustomEnabled(false);
        // Change search icon accordingly.
        searchItem.setIcon(searchIcon);
        SearchOpened = false;
    }
}
