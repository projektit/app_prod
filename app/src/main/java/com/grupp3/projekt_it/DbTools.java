package com.grupp3.projekt_it;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbTools extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "plants";

    // Plants table name
    private static final String TABLE_PLANTS = "plants";

    // Plants Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";

    public DbTools (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLANTS_TABLE = "CREATE TABLE " + TABLE_PLANTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_TYPE + " TEXT" + ")";
        db.execSQL(CREATE_PLANTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTS);

        // Create tables again
        onCreate(db);
    }

        /////////////////////////////////////////////////////////////
        // Methods for Create, Read, Update and Delete in database.//
        /////////////////////////////////////////////////////////////
        // Adding new plant
        public void addPlant(Plant_test plant) {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_NAME, plant.getName()); // Plant name
            values.put(KEY_TYPE, plant.getType()); // Plant type

            // Inserting Row
            db.insert(TABLE_PLANTS, null, values);
            db.close(); // Closing database connection
        }

        // Getting single plant
        // Returns single row by id
        public Plant_test getPlant(int id) {

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_PLANTS, new String[] { KEY_ID,
                            KEY_NAME, KEY_TYPE }, KEY_ID + "=?",
                    new String[] { String.valueOf(id) }, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();

            Plant_test plant = new Plant_test(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2));
            // return plant
            return plant;
        }

        // Getting all plants
        public List<Plant_test> getAllPlants() {

            List<Plant_test> plantList = new ArrayList<Plant_test>();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_PLANTS;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Plant_test plant = new Plant_test();
                    plant.setID(Integer.parseInt(cursor.getString(0)));
                    plant.setName(cursor.getString(1));
                    plant.setType(cursor.getString(2));
                    // Adding plant to list
                    plantList.add(plant);
                } while (cursor.moveToNext());
            }

            // return plant list
            return plantList;
        }

        // Getting plant count
        public int getPlantCount() {

            String countQuery = "SELECT  * FROM " + TABLE_PLANTS;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            cursor.close();

            // return count
            return cursor.getCount();
        }

        // Updating single plant
        public int updatePlant(Plant_test plant) {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_NAME, plant.getName());
            values.put(KEY_TYPE, plant.getType());

            // updating row
            return db.update(TABLE_PLANTS, values, KEY_ID + " = ?",
                    new String[] { String.valueOf(plant.getID()) });
        }

        // Deleting single plant
        public void deletePlant(Plant_test plant) {

            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_PLANTS, KEY_ID + " = ?",
                    new String[] { String.valueOf(plant.getID()) });
            db.close();
        }
}

class Plant_test{

    // private variables
    int _id;
    String _name;
    String _type;

    // Empty constructor
    public Plant_test(){

    }
    // constructor
    public Plant_test(int id, String name, String _type){
        this._id = id;
        this._name = name;
        this._type = _type;
    }

    // constructor
    public Plant_test(String name, String _type){
        this._name = name;
        this._type = _type;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    // getting phone number
    public String getType(){
        return this._type;
    }

    // setting phone number
    public void setType(String phone_number){
        this._type = phone_number;
    }
        }