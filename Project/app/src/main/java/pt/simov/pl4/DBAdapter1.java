package pt.simov.pl4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DBAdapter1 {
    private DBHelper dbHelper;

    private static final String TABLE = "ITEMS";
    private static final String ID = "ID";
    private static final String ITEM_NAME = "ITEM_NAME";

    public DBAdapter1(Context context) {
        dbHelper = new DBHelper(context, TABLE, ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ITEM_NAME );
    }

    //All CRUD Operations (Create, Read, Update and Delete)
    public boolean insertItem( String itemName) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues initialValues = new ContentValues();
            initialValues.put(ITEM_NAME,itemName);
            db.insert(TABLE, null, initialValues);
            db.close();
        } catch (SQLException sqlerror) {
            Log.v("Insert into table error", sqlerror.getMessage());
            return false;
        }
        return true;
    }
    public Item getItem(int id) {
        Item item = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String s = "SELECT * FROM " + TABLE + " WHERE " + ID+ "=" + id;
        Cursor cursor = db.rawQuery(s, null);
        if(cursor != null) {
            cursor.moveToFirst();
            item = new Item(cursor.getInt(0), cursor.getString(1));
            cursor.close();
        }
        db.close();
        return item;
    }
    public ArrayList<Item> getItems() {
        String query = "SELECT * FROM " + TABLE;
        ArrayList<Item> wishlist = new ArrayList<Item>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                wishlist.add(new Item(cursor.getInt(0), cursor.getString(1)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return wishlist;
    }
    public ArrayList<Item> getItems(String nameLike) {
        String query = "SELECT * FROM " + TABLE + " where " + ITEM_NAME + " like '%" + nameLike + "%' ";
        ArrayList<Item> wishlist = new ArrayList<Item>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                wishlist.add(new Item(cursor.getInt(0), cursor.getString(1)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return wishlist;
    }

    //delete Operation made by Konstantin
    public boolean removeItem(int id){
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
    public boolean updateItem(int id, String item){
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues initialValues = new ContentValues();
            initialValues.put(ID,id);
            initialValues.put(ITEM_NAME,item);
            db.update(TABLE,initialValues,"ID=" + id,null);
            db.close();
        } catch (SQLException sqlerror) {
            Log.v("Update in table error", sqlerror.getMessage());
            return false;
        }
        return true;
    }

}
