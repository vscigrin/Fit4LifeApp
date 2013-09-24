package ru.fit4life.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public abstract class ParentDatabaseAdapter {

    private SQLiteDatabase mDb;

    protected ParentDatabaseAdapter() {
        mDb = MainActivity.myDb;
    }

    protected Cursor getCursorBySqlString(String query) {

        Cursor cursor = mDb.rawQuery(query, null);
        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    protected SQLiteDatabase getDb() {
        return mDb;
    }
/*
    protected void close() {


        Log.i("qwerty", "Check cursor state");
        if (!cursor.isClosed()) {
            Log.i("qwerty", "Trying to close");
            cursor.close();
        }
        Log.i("qwerty", "Cursor closed!");

    }
*/
    //  public abstract Cursor fetchAll();

}
