package com.example.sanje.i_weather;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="contactdata.db";
    private static final String TABLE_NAME="contact_table";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_USERNAME="username";
    private static final String COLUMN_EMAIL="email";
    private static final String COLUMN_PASSWORD="password";

    public static final String CREATE_TAB_QUERY="CREATE TABLE contact_table(id integer PRIMARY KEY AUTOINCREMENT ,username text ,email text ,password text )";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);//creates
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(CREATE_TAB_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String QUERY="DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(QUERY);
        onCreate(db);
    }
    //self defined method
    public void insertContact(Contacts c){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(COLUMN_USERNAME,c.getName());
        contentValues.put(COLUMN_EMAIL,c.getEmail());
        contentValues.put(COLUMN_PASSWORD,c.getPassword());
        long result=db.insert(TABLE_NAME,null,contentValues);

    }
    //self defined method
    public String searchPassword(String uname){
        String a,b;
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT username,password FROM "+TABLE_NAME;
        Cursor cursor=db.rawQuery(query,null);
        b="not found";
        if(cursor.moveToFirst()){
            do{
                a=cursor.getString(0);


                if(a.equals(uname)){
                    b=cursor.getString(1);
                    break;
                }
            }while (cursor.moveToNext());
        }
        return b;
    }
}
