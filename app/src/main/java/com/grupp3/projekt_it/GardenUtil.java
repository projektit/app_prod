package com.grupp3.projekt_it;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Daniel on 2015-04-22.
 */
public class GardenUtil {
    public void saveGarden(Garden garden, Context context){
        Gson gson = new Gson();

        String json = gson.toJson(garden);
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(garden.getName() + ".grdn", Context.MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Garden loadGarden(String gardenName, Context context){
        String json = "";
        FileInputStream fileInputStream;
        try{
            fileInputStream = context.openFileInput(gardenName + ".grdn");
            byte[] input = new byte[fileInputStream.available()];
            while(fileInputStream.read(input) != -1){
                json += new String(input);
            }
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //convert json to java object
        Gson gson = new Gson();

        Garden garden = gson.fromJson(json, Garden.class);
        return garden;
    }
    public void deleteGarden(String gardenName, Context context){
        context.deleteFile(gardenName + ".grdn");
    }
}

