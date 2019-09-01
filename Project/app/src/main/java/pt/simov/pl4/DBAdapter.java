package pt.simov.pl4;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DBAdapter {
    private DBHelper dbHelper;

    private static final String TABLE = "PEOPLE";
    private static final String ID = "ID";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    public DBAdapter(Context context) {
        dbHelper = new DBHelper(context, TABLE, ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + FIRST_NAME + " TEXT," + LAST_NAME + " TEXT");
    }

    //All CRUD Operations (Create, Read, Update and Delete)
    public boolean insertPerson( String firstName, String lastName) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues initialValues = new ContentValues();
            initialValues.put(FIRST_NAME,firstName);
            initialValues.put(LAST_NAME, lastName);
            db.insert(TABLE, null, initialValues);
            db.close();
        } catch (SQLException sqlerror) {
            Log.v("Insert into table error", sqlerror.getMessage());
            return false;
        }
        return true;
    }
    public Person getPerson(int id) {
        Person person = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String s = "SELECT * FROM " + TABLE + " WHERE " + ID+ "=" + id;
        Cursor cursor = db.rawQuery(s, null);
        if(cursor != null) {
            cursor.moveToFirst();
            person = new Person(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
            cursor.close();
        }
        db.close();
        return person;
    }
    public ArrayList<Person> getPeople() {
        String query = "SELECT * FROM " + TABLE;
        ArrayList<Person> people = new ArrayList<Person>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                people.add(new Person(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return people;
    }
    public ArrayList<Person> getPeople(String nameLike) {
        String query = "SELECT * FROM " + TABLE + " where " + FIRST_NAME + " like '%" + nameLike + "%' " + " OR " + LAST_NAME + " like '%" + nameLike + "%' ";
        ArrayList<Person> people = new ArrayList<Person>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                people.add(new Person(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return people;
    }

    //delete Operation made by Konstantin
    public boolean removePerson(int id){
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //ContentValues initialValues = new ContentValues();
            //initialValues.put(ID,id);

            db.delete(TABLE, ID + "=" + id , null);
            db.close();
        } catch (SQLException sqlerror) {
            Log.v("Delete from table error", sqlerror.getMessage());
            return false;
        }
            return true;
    }

    //update Operation made by Konstantin
    public boolean updatePerson(int id, String name, String surName){
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues initialValues = new ContentValues();
            initialValues.put(ID,id);
            initialValues.put(FIRST_NAME,name);
            initialValues.put(LAST_NAME,surName);
            db.update(TABLE,initialValues,"ID=" + id,null);
            db.close();
        } catch (SQLException sqlerror) {
            Log.v("Update in table error", sqlerror.getMessage());
            return false;
        }
        return true;
    }

}
