package com.example.recetadecomida;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {
    private static final String dataBaseName = "recipes.db";
    private static final int dataBaseVersion = 1;

    public DataBase (Context context) {
        super(context, dataBaseName, null, dataBaseVersion);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableMeal = "create table meal(" +
                "idMeal integer primary key autoincrement," +
                "email text," +
                "title text," +
                "area text," +
                "category text," +
                "instructions text," +
                "ingredients text," +
                "imageUrl text)";
        db.execSQL(createTableMeal);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists meal");
        onCreate(db);
    }
}
