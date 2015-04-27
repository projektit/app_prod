package com.grupp3.projekt_it;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2015-04-23.
 */
public class SQLPlantHelper extends SQLiteOpenHelper {
    String TAG = "com.grupp3.projekt_it";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "plants.db";
    //private static final String TABLE_FLOWERS = "flowers";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "_name";
    private static final String COLUMN_SWENAME = "_swename";
    private static final String COLUMN_LATIN_NAME = "_latin_name";
    private static final String COLUMN_TYPE = "_type";
    private static final String COLUMN_SOIL = "_soil";
    private static final String COLUMN_ZONE_MIN = "_zone_min";
    private static final String COLUMN_ZONE_MAX = "_zone_max";
    private static final String COLUMN_WATER = "_water";
    private static final String COLUMN_MISC = "_misc";
    private static final String COLUMN_SUN = "_sun";

    private static final String COLUMN_USER_INFO = "_user_info";
    private static final String COLUMN_USER_SOIL = "_user_soil";
    private static final String COLUMN_USER_ZONE = "_user_zone";
    public boolean firstTime = true;

    String newTableName;

    public SQLPlantHelper(Context context) {
        //super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "constructor");
    }

    public void createNewTable(String tableName){
        newTableName = tableName;
        SQLiteDatabase db = getWritableDatabase();
        String query = "CREATE TABLE " + newTableName + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SWENAME + " TEXT, " +
                COLUMN_LATIN_NAME + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_SOIL + " TEXT, " +
                COLUMN_ZONE_MIN + " TEXT, " +
                COLUMN_ZONE_MAX + " TEXT, " +
                COLUMN_WATER + " INTEGER, " +
                COLUMN_MISC + " TEXT, " +
                COLUMN_SUN + " INTEGER, " +
                COLUMN_USER_INFO + " TEXT, " +
                COLUMN_USER_SOIL + " TEXT, " +
                COLUMN_USER_ZONE + " TEXT " +
                ");";

        Log.i(TAG, "onCreate: " + newTableName + " " + Integer.toString(db.getVersion()));
        db.execSQL(query);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade: " + newTableName + " " + Integer.toString(db.getVersion()));
        db.execSQL("DROP TABLE IF EXISTS " + newTableName);
        onCreate(db);
    }
    public void addPlant(Plant_DB plant, String tableName){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, plant.get_name());
        values.put(COLUMN_SWENAME, plant.get_swe_name());
        values.put(COLUMN_LATIN_NAME, plant.get_latin_name());
        values.put(COLUMN_TYPE, plant.get_type());
        values.put(COLUMN_SOIL, plant.get_soil());
        values.put(COLUMN_ZONE_MIN, plant.get_zone_min());
        values.put(COLUMN_ZONE_MAX, plant.get_zone_max());
        values.put(COLUMN_WATER, plant.get_water());
        values.put(COLUMN_MISC, plant.get_misc());
        values.put(COLUMN_SUN, plant.get_sun());
        values.put(COLUMN_USER_INFO, plant.get_user_info());
        values.put(COLUMN_USER_SOIL, plant.get_user_soil());
        values.put(COLUMN_USER_ZONE, plant.get_user_zone());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(tableName, null, values);
        db.close();
    }
    public Plant_DB getPlant(int id, String tableName) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(tableName, new String[] { COLUMN_ID,
                        COLUMN_NAME, COLUMN_SWENAME, COLUMN_LATIN_NAME, COLUMN_TYPE, COLUMN_SOIL, COLUMN_ZONE_MIN,
                        COLUMN_ZONE_MAX, COLUMN_WATER, COLUMN_MISC, COLUMN_SUN, COLUMN_USER_INFO, COLUMN_USER_SOIL,
                        COLUMN_USER_ZONE}, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Plant_DB plant = new Plant_DB(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                Integer.parseInt(cursor.getString(8)),
                cursor.getString(9),
                Integer.parseInt(cursor.getString(10)),
                cursor.getString(11),
                cursor.getString(12),
                cursor.getString(13)
        );
        // return plant
        db.close();
        return plant;
    }
    public void deleteFlower(String flowerName, String tableName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + tableName + " WHERE " + COLUMN_SWENAME + "+\"" + flowerName + "\";");
    }

    public ArrayList<Plant_DB> getAllPlants(String tableName) {

        ArrayList<Plant_DB> plantList = new ArrayList<Plant_DB>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tableName;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Plant_DB plant = new Plant_DB();
                plant.set_id(Integer.parseInt(cursor.getString(0)));
                plant.set_name((cursor.getString(1)));
                plant.set_swe_name((cursor.getString(2)));
                plant.set_latin_name((cursor.getString(3)));
                plant.set_type((cursor.getString(4)));
                plant.set_soil((cursor.getString(5)));
                plant.set_zone_min((cursor.getString(6)));
                plant.set_zone_max((cursor.getString(7)));
                plant.set_water(Integer.parseInt(cursor.getString(8)));
                plant.set_misc((cursor.getString(9)));
                plant.set_sun(Integer.parseInt(cursor.getString(10)));
                plant.set_user_info((cursor.getString(11)));
                plant.set_user_soil((cursor.getString(12)));
                plant.set_user_zone((cursor.getString(13)));
                Log.i(TAG, "hallåå " + plant.get_swe_name());
                // Adding plant to list
                plantList.add(plant);
            } while (cursor.moveToNext());
        }
        // return plant list
        db.close();
        return plantList;
    }


}
class Plant_DB{
    int _id;
    String _name;
    String _swe_name;
    String _latin_name;
    String _type;
    String _soil;
    String _zone_min;
    String _zone_max;
    int _water;
    String _misc;
    int _sun;
    String _user_info;
    String _user_soil;
    String _user_zone;

    Plant_DB(Plant plant, String _user_info, String _user_soil, String _user_zone) {
        this._sun = plant.getSun();
        this._id = plant.getId();
        this._name = plant.getName();
        this._swe_name = plant.getSwe_name();
        this._latin_name = plant.getLatin_name();
        this._type = plant.getType();
        this._soil = plant.getSoil();
        this._zone_min = plant.getZone_min();
        this._zone_max = plant.getZone_max();
        this._water = plant.getWater();
        this._misc = plant.getMisc();
        this._user_info = _user_info;
        this._user_soil = _user_soil;
        this._user_zone = _user_zone;
    }

    Plant_DB(int _id, String _name, String _swe_name, String _latin_name, String _type, String _soil,
             String _zone_min, String _zone_max, int _water, String _misc, int _sun, String _user_info, String _user_soil,
             String _user_zone) {
        this._id = _id;
        this._name = _name;
        this._swe_name = _swe_name;
        this._latin_name = _latin_name;
        this._type = _type;
        this._soil = _soil;
        this._zone_min = _zone_min;
        this._zone_max = _zone_max;
        this._water = _water;
        this._misc = _misc;
        this._sun = _sun;
        this._user_info = _user_info;
        this._user_soil = _user_soil;
        this._user_zone = _user_zone;
    }

    Plant_DB() {
        this._sun = 1;
        this._id = 1;
        this._name = "hej";
        this._swe_name = "hej";
        this._latin_name = "hej";
        this._type = "hej";
        this._soil = "hej";
        this._zone_min = "hej";
        this._zone_max = "hej";
        this._water = 1;
        this._misc = "hej";
        this._user_info = "hej";
        this._user_soil = "hej";
        this._user_zone = "hej";
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_swe_name(String _swe_name) {
        this._swe_name = _swe_name;
    }

    public void set_latin_name(String _latin_name) {
        this._latin_name = _latin_name;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public void set_soil(String _soil) {
        this._soil = _soil;
    }

    public void set_zone_min(String _zone_min) {
        this._zone_min = _zone_min;
    }

    public void set_zone_max(String _zone_max) {
        this._zone_max = _zone_max;
    }

    public void set_water(int _water) {
        this._water = _water;
    }

    public void set_misc(String _misc) {
        this._misc = _misc;
    }

    public void set_sun(int _sun) {
        this._sun = _sun;
    }

    public void set_user_info(String _user_info) {
        this._user_info = _user_info;
    }

    public void set_user_soil(String _user_soil) {
        this._user_soil = _user_soil;
    }

    public void set_user_zone(String _user_zone) {
        this._user_zone = _user_zone;
    }

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public String get_swe_name() {
        return _swe_name;
    }

    public String get_latin_name() {
        return _latin_name;
    }

    public String get_type() {
        return _type;
    }

    public String get_soil() {
        return _soil;
    }

    public String get_zone_min() {
        return _zone_min;
    }

    public String get_zone_max() {
        return _zone_max;
    }

    public int get_water() {
        return _water;
    }

    public String get_misc() {
        return _misc;
    }

    public int get_sun() {
        return _sun;
    }

    public String get_user_info() {
        return _user_info;
    }

    public String get_user_soil() {
        return _user_soil;
    }

    public String get_user_zone() {
        return _user_zone;
    }
}
