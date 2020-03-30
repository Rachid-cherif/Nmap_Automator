package com.example.mrcherif.nmap_auto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="mylist.db";
    public static final String TABLE_NAME = "mylist_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "MSG";
    public static final String COL3 = "CMD";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createtable = " CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MSG TEXT, "+ " CMD TEXT)";
        sqLiteDatabase.execSQL(createtable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
sqLiteDatabase.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
    }
    public  boolean addData(String msg, String cmd){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COL2,msg);
        contentValues.put(COL3,cmd);

        Long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        }else return true;
    }

    public Cursor getListContent(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data_db = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data_db;
    }
    public Integer deletedata(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_NAME, COL1 + "=" + id, null);
    }
        protected void deleteT(){
            SQLiteDatabase db= this.getWritableDatabase();
           db.execSQL("drop table if exists "+ TABLE_NAME+ ";");
            onCreate(db);


    }
    public String getListContent(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COL3 + " FROM " + TABLE_NAME + " WHERE "+ COL1 + " = "+id, null);

        return stringcursor(data);
    }
    public boolean getList(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COL3 + " FROM " + TABLE_NAME + " WHERE "+ COL1 + " = "+id, null);


        if(stringcursor(data)==null){
            return false;

        }else return true;
    }

    private String stringcursor(Cursor c) {
        if (c.getCount() == 0)
            return null;
        c.moveToFirst();
        Log.d("test", "le count est " + c.getCount());
        return c.getString(0);

    }
}
