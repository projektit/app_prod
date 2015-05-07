package com.grupp3.projekt_it;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

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
    public ArrayList<Garden> loadAllGardens(Context context){
        String [] files = context.fileList();
        ArrayList <String> desiredFiles = new ArrayList<>();
        for(int i = 0; i < files.length; i ++){
            if (files[i].endsWith(".grdn")){
                desiredFiles.add(files[i].substring(0, files[i].length()-5));
            }
        }
        ArrayList <Garden> allGardens = new ArrayList<>();
        for(String desiredFile: desiredFiles){
            allGardens.add(loadGarden(desiredFile, context));
        }
        return allGardens;
    }
    public void deleteGarden(String gardenName, Context context){
        Garden garden = loadGarden(gardenName, context);
        String tableName = garden.getTableName();
        SQLPlantHelper sqlPlantHelper = new SQLPlantHelper(context);
        sqlPlantHelper.deleteTable(tableName);
        context.deleteFile(gardenName + ".grdn");
    }
    public int getTableNumber(Context context){
    String [] files = context.fileList();
    List <String> files2 = Arrays.asList(files);
    if(files2.contains("number.tbl")){
        String file = "";
        FileInputStream fileInputStream;
        try{
            fileInputStream = context.openFileInput("number.tbl");
            byte[] input = new byte[fileInputStream.available()];
            while(fileInputStream.read(input) != -1){
                file += new String(input);
            }
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setTableNumber(context, Integer.parseInt(file));
        return Integer.parseInt(file);
    }else{
        setTableNumber(context, 1);
        return 1;
    }
}
    public void setTableNumber(Context context, int version){
        String version2 = Integer.toString(version);
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("number.tbl", Context.MODE_PRIVATE);
            fileOutputStream.write(version2.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getNotificationNumber(Context context){
        String [] files = context.fileList();
        List <String> files2 = Arrays.asList(files);
        if(files2.contains("number.notif")){
            String file = "";
            FileInputStream fileInputStream;
            try{
                fileInputStream = context.openFileInput("number.notif");
                byte[] input = new byte[fileInputStream.available()];
                while(fileInputStream.read(input) != -1){
                    file += new String(input);
                }
                fileInputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setTableNumber(context, Integer.parseInt(file));
            return Integer.parseInt(file);
        }else{
            setTableNumber(context, 1);
            return 1;
        }
    }
    public void setNotificationNumber(Context context, int version){
        String version2 = Integer.toString(version);
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("number.notif", Context.MODE_PRIVATE);
            fileOutputStream.write(version2.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveUserNotification(UserNotification userNotification, Context context){
        Gson gson = new Gson();

        String json = gson.toJson(userNotification);
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(userNotification.getId() + ".not", Context.MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public UserNotification loadUserNotification(int id, Context context){
        String json = "";
        FileInputStream fileInputStream;
        try{
            fileInputStream = context.openFileInput(Integer.toString(id) + ".not");
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

        UserNotification userNotification = gson.fromJson(json, UserNotification.class);
        return userNotification;
    }
    public ArrayList<UserNotification> loadAllUserNotifications(Context context){
        String [] files = context.fileList();
        ArrayList <String> desiredFiles = new ArrayList<>();
        for(int i = 0; i < files.length; i ++){
            if (files[i].endsWith(".not")){
                desiredFiles.add(files[i].substring(0, files[i].length()-4));
            }
        }
        ArrayList <UserNotification> allUserNotifications = new ArrayList<>();
        for(String desiredFile: desiredFiles){
            allUserNotifications.add(loadUserNotification(Integer.parseInt(desiredFile), context));
        }
        return allUserNotifications;
    }
    public void deleteUserNotification(int id, Context context){
        UserNotification userNotification = loadUserNotification(id, context);
        context.deleteFile(id + ".not");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Gson gson = new Gson();
        String json = gson.toJson(userNotification);

        Intent intent = new Intent(context, UserNotificationService.class);
        intent.putExtra("jsonUserNotification", json);
        PendingIntent pendingIntent = PendingIntent.getService(context, userNotification.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
    }
}

