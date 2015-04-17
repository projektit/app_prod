package com.grupp3.projekt_it;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Marcus on 2015-04-17.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.grupp3.projekt_it/databases/";

    private static String DB_NAME = "flowersDB";

    private SQLiteDatabase myDB;

    private final Context myContext;

    public DBHelper(Context context) {

        super(context,DB_NAME,null,1);
        this.myContext = context;
    }



    public void createDataBase(){
        try {
            boolean dbExist = checkDataBase();

            if(dbExist){
                //do nothing - database already exist
            }else{
                //By calling this method and empty database will be created into the default system path
                //of your application so we are gonna be able to overwrite that database with our database.
                this.getReadableDatabase();


                copyDataBase();

            }
        }
        catch (Exception e) {

        }
    }

    /*private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try{

            String myPath = DB_NAME + DB_PATH;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){
            //dB doesn't exist yet
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true:false;
    }*/

    private boolean checkDataBase() {
        File dbFile = myContext.getDatabasePath(DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
    }

    private void copyDatabase() throws IOException {

        InputStream myInput = myContext.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);

        }

        myOutput.flush();
        myOutput.close();
        myInput.close();


    }



    public void openDatabase() throws SQLiteException{

        String myPath = DB_PATH + DB_NAME;
        myDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);



    }

    public synchronized void close(){

        if(myDB != null)
        {myDB.close();}
        super.close();
        
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
