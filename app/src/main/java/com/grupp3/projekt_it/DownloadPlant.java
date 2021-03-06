    package com.grupp3.projekt_it;

    import android.app.Activity;
    import android.app.FragmentManager;
    import android.content.Context;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.drawable.Drawable;
    import android.net.ConnectivityManager;
    import android.net.NetworkInfo;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.ListView;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.gson.Gson;
    import com.google.gson.stream.JsonReader;
    import com.squareup.picasso.Picasso;

    import org.json.JSONObject;

    import java.io.BufferedReader;
    import java.io.FileInputStream;
    import java.io.FileNotFoundException;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.io.StringReader;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.HashMap;

    /**
     * Created by Daniel on 2015-04-21.
     */
    public class DownloadPlant extends AsyncTask<String, Void, String> {
        String TAG = "com.grupp3.projekt_it";
        Context context;
        ListView listView;
        FragmentManager fragmentManager;
        Activity activity;
        ArrayList <Plant> allPlants;
        LayoutInflater layoutInflater;
        String urlSearch;

        public DownloadPlant(Context context, ListView listView, FragmentManager fragmentManager, Activity activity,
                             LayoutInflater layoutInflater){
            this.context = context;
            this.listView = listView;
            this.fragmentManager = fragmentManager;
            this.activity = activity;
            this.layoutInflater = layoutInflater;
        }
        @Override
        protected String doInBackground(String... urls){
            String rawData = "";
            try {
                // get url
                URL url = new URL(urls[0]);
                urlSearch = url.toString();
                // setup http connection
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                // get input
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // build string from input stream
                StringBuffer json = new StringBuffer(1024);
                String result = reader.readLine();
                reader.close();

                return result;
            }catch(Exception e){
                Log.i(TAG, "Exception in  plant client");
                return null;
            }
        }
        protected void onPostExecute(String result) {
            if(result == null){
              //display toast with info if input string
              //doesn't match any entry in the database
              //don't display if search field is empty
              if (!urlSearch.endsWith("=")) {
                  Toast.makeText(this.context, "Inget resultat hittades, försök med något annat!", Toast.LENGTH_SHORT).show();
              }
              //if previous search have been made and displayed entries
              //hide these when user write a name that doesn't exisit
              listView.setVisibility(View.GONE);
              return;
            }
            if(result.equals("")){
                Log.i(TAG, "No search match");
                return;
            }
            //Show listview again
            listView.setVisibility(View.VISIBLE);
            JsonReader reader = new JsonReader(new StringReader(result));
            reader.setLenient(true);
            Gson gson = new Gson();
            Log.i(TAG, result);
            final Plant [] plants = gson.fromJson(reader, Plant[].class);
            allPlants = new ArrayList<Plant>(Arrays.asList(plants));
            ArrayAdapter <Plant> adapter = new SearchListAdapter();
            listView.setAdapter(adapter);

            //Listen for normal click on items in list
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Plant plant = allPlants.get(position);
                    //create new Gson object
                    Gson gson = new Gson();
                    //create jsonPlant string of DB
                    String jsonPlant = gson.toJson(plant);
                    //create new intent for starting MyFlowerActivity.class
                    Intent intent = new Intent(context, MyFlowerWebActivity.class);
                    //send flower info to MyFlowerActivity
                    intent.putExtra("jsonPlant", jsonPlant);
                    //start Activity
                    activity.startActivity(intent);
                }
            });

            //Listener for long click on items in list
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Plant plant = allPlants.get(position);
                    Gson gson = new Gson();
                    String json = gson.toJson(plant);
                    Bundle bundle = new Bundle();
                    bundle.putString("jsonPlant", json);

                    PlantSearchFragment quickOptions = new PlantSearchFragment();
                    quickOptions.setArguments(bundle);
                    quickOptions.show(fragmentManager, "disIsTag3");
                    return true;
                }
            });
        }
        private class SearchListAdapter extends ArrayAdapter <Plant> {
            public SearchListAdapter() {
                super(context, R.layout.search_list_item, allPlants);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View gardenItemView = convertView;
                //check if given view is null, if so inflate a new one
                if (gardenItemView == null) {
                    gardenItemView = layoutInflater.inflate(R.layout.search_list_item, parent, false);
                }
                //find garden to work with
                final Plant plant = allPlants.get(position);

                //Fill image and textviews in list items
                ImageView plantImage = (ImageView) gardenItemView.findViewById(R.id.plantImage);

                TextView plantResultName = (TextView) gardenItemView.findViewById(R.id.plantResultName);
                plantResultName.setText(plant.getSwe_name());

                TextView plantResultLatinName = (TextView) gardenItemView.findViewById(R.id.plantResultLatinName);
                plantResultLatinName.setText(plant.getLatin_name());

                // If the add to garden button is pressed, display options to add plant to a garden
                Button addButton = (Button) gardenItemView.findViewById(R.id.add_btn);
                addButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Gson gson = new Gson();
                        String json = gson.toJson(plant);
                        Bundle bundle = new Bundle();
                        bundle.putString("jsonPlant", json);
                        ChooseGardenFragment chooseGardenFragment = new ChooseGardenFragment();
                        chooseGardenFragment.setArguments(bundle);
                        chooseGardenFragment.show(fragmentManager, "disIsTag4");
                    }
                });
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        try {
                            Log.i(TAG, "HERE: " + plant.getSwe_name() + "\n" + plant.getImg_url());
                            //new DownloadImage(imageView1).execute(plant.getImg_url());
                            /* Picasso is a 3rd party library that takes care of image loading in android applications
                     * With the library re-use of adapters is automatically detected and previous downloads are
                      * canceled. The with method is used for the global default instance with defaut values:
                       *LRU memory cache of 15% the available application RAM
                       * Disk cache of 2% storage space up to 50MB but no less than 5MB. (Note: this is only available on API 14+ or if you are using a standalone library that provides a disk cache on all API levels like OkHttp)
                       *  Three download threads for disk and network access.
                       load is where you specify the URL/file that the picture should be read from
                       into method specifies which imageView that should be used*/
                            Picasso.with(context).load(plant.getImg_url()).into(plantImage);
                            /*Picasso.with(context)
                            .load(plant.getImg_url())
                            .resize(50, 50)
                            .centerCrop()
                            .into(imageView1);*/

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i(TAG, "Connected but failed anyway");
                        }
                    }
                return gardenItemView;
            }

        }


    }
