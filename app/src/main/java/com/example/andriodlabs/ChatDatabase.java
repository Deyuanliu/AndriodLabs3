package com.example.andriodlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Chats.db";
    public static final String TABLE_NAME = "ChatMessage";
    public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "MESSAGE";
    public static final String KEY_TYPE = "MSGTYPE";
    private static int VERSION_NUM = 3;
    private static final String ACTIVITY_NAME = "ChatDatabase";
    // create table sql
    // ChatMessage
    // _id integer
    // MESSAGE text
    // TYPE INTEGER
    public static final String CREATE_TABLE_MESSAGE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_MESSAGE + " text, "
                    + KEY_TYPE + " INTEGER);";

    public static final String DROP_TABLE_MESSAGE =
            "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public ChatDatabase(Context ctx){
        super(ctx, TABLE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_MESSAGE);
        Log.i(ACTIVITY_NAME, "Calling onCreate "+CREATE_TABLE_MESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        db.execSQL(DROP_TABLE_MESSAGE);
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer +" "+ DROP_TABLE_MESSAGE);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer){
        db.execSQL(DROP_TABLE_MESSAGE);
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Calling onDowngrade, oldVersion=" + oldVer + " newVersion=" + newVer +" "+ DROP_TABLE_MESSAGE);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        Log.i(ACTIVITY_NAME, "Calling onOpen ");

    }
}


