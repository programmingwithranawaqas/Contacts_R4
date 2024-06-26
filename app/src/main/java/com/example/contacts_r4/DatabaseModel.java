package com.example.contacts_r4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseModel {

    private final Context context;
    private  final String DATABASE_NAME = "ContactsDB";
    private  final String DATABASE_TABLE = "ContactsTable";
    private  final String KEY_ID = "_id";
    private  final String KEY_NAME = "contact_name";
    private  final String KEY_PHONE = "_phone";
    private final int DATABASE_VERSION = 2;

    MyDBHelper helper;
    SQLiteDatabase db;

    public DatabaseModel(Context context)
    {
        this.context = context;
    }

    public void open()
    {
        helper = new MyDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = helper.getWritableDatabase();
    }

    public void close()
    {
        db.close();
        helper.close();
    }

    public void addContact(String name, String phone) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_PHONE, phone);

        long count = db.insert(DATABASE_TABLE, null, cv);
        if(count>0)
        {
            Toast.makeText(context, name+" added.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "contact not added", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Contact> getAllContacts()
    {
        ArrayList<Contact> contacts = new ArrayList<>();

        String []columns = new String[]{KEY_ID,
                                        KEY_NAME,
                                        KEY_PHONE};

       Cursor cursor = db.query(DATABASE_TABLE, columns, null, null, null, null, null);

       int id_index = cursor.getColumnIndex(KEY_ID);
       int name_index = cursor.getColumnIndex(KEY_NAME);
       int phone_index = cursor.getColumnIndex(KEY_PHONE);
       for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
       {
           Contact c = new Contact();
           c.setId(cursor.getInt(id_index));
           c.setName(cursor.getString(name_index));
           c.setPhone(cursor.getString(phone_index));

           contacts.add(c);
       }

       cursor.close();

       return contacts;
    }

    public void removeContact(int id) {

       int count =  db.delete(DATABASE_TABLE, KEY_ID+"=?", new String[]{id+""});
        if(count>0)
        {
            Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "not deleted", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateContact(int id, String name, String phone) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_PHONE, phone);

        int count = db.update(DATABASE_TABLE, cv, KEY_ID+"=?", new String[]{id+""});
        if(count>0)
        {
            Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "not updated", Toast.LENGTH_SHORT).show();
        }
    }


    private class MyDBHelper extends SQLiteOpenHelper
    {

        public MyDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE "+DATABASE_TABLE+"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +KEY_NAME+" TEXT,"+KEY_PHONE+" TEXT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String query = "DROP TABLE IF EXISTS "+DATABASE_TABLE;
            db.execSQL(query);
            onCreate(db);
        }
    }
}
