package com.grupp3.projekt_it;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daniel on 2015-04-16.
 */
public class sqlFlowerHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "flowers.db";
    private static final String TABLE_FLOWERS = "flowers";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_SWENAME = "_swename";
    private static final String COLUMN_LANAME = "_laname";
    private static final String COLUMN_LIGHT = "_light";
    private static final String COLUMN_TYPE = "_type";
    private static final String COLUMN_ZONE = "_zone";
    private static final String COLUMN_MISC = "_misc";

    public sqlFlowerHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_FLOWERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
                COLUMN_SWENAME + " TEXT " +
                COLUMN_LANAME + " TEXT " +
                COLUMN_LIGHT + " TEXT "+
                COLUMN_TYPE + " TEXT " +
                COLUMN_ZONE + " TEXT " +
                COLUMN_MISC + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLOWERS);
        onCreate(db);
    }
    public void addFlower(Flower flower){
        ContentValues values = new ContentValues();
        values.put(COLUMN_SWENAME, flower.getSweName());
        values.put(COLUMN_LANAME, flower.getLaName());
        values.put(COLUMN_LIGHT, flower.getLight());
        values.put(COLUMN_TYPE, flower.getType());
        values.put(COLUMN_ZONE, flower.getZone());
        values.put(COLUMN_MISC, flower.get_misc());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_FLOWERS, null, values);
        db.close();
    }
    public void deleteFlower(String flowerName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_FLOWERS + " WHERE " + COLUMN_SWENAME + "+\"" + flowerName + "\";");
    }
}

class Flower{
    int id;
    String _swename;
    String _laname;
    String _light;
    String _type;
    String _zone;
    String _misc;

    public String getSweName() {
        return _swename;
    }

    public String getLaName() {
        return _laname;
    }

    public String getLight() {
        return _light;
    }

    public String getType() {
        return _type;
    }

    public String getZone() {
        return _zone;
    }

    public void setSweName(String sweName) {
        this._swename = sweName;
    }

    public void setLaName(String laName) {
        this._laname = laName;
    }

    public void setLight(String light) {
        this._light = light;
    }

    public void setType(String type) {
        this._type = type;
    }

    public void setZone(String zone) {
        this._zone = zone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get_misc() {
        return _misc;
    }

    public void set_misc(String _misc) {
        this._misc = _misc;
    }

    Flower(int id, String _swename, String _laname, String _light, String _type, String _zone, String _misc) {
        this.id = id;
        this._swename = _swename;
        this._laname = _laname;
        this._light = _light;
        this._type = _type;
        this._zone = _zone;
        this._misc = _misc;
    }
}

