package ru.fit4life.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CancellationSignal;
import android.util.Log;


public abstract class ParentDatabaseAdapter {

    private SQLiteDatabase mDb;
    protected ContentValues data;

    protected ParentDatabaseAdapter() {
        mDb = MainActivity.myDb;
        data = new ContentValues();
    }

    protected Cursor getCursorBySqlString(String query) {

        Cursor cursor = mDb.rawQuery(query, null);
        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    //select * from table
    protected Cursor getCursorByQuery(String table) {

        return getCursorByQuery(false, table, null, null, null, null, null, null, null);
    }

    //select * from table where column = ?
    protected Cursor getCursorByQuery(String table, String selection, String[] selectionArgs) {

        return getCursorByQuery(false, table, null, selection, selectionArgs, null, null, null, null);
    }

    //select * from table where column = ? order by ?
    protected Cursor getCursorByQuery(String table, String selection, String[] selectionArgs, String orderBy) {

        return getCursorByQuery(false, table, null, selection, selectionArgs, null, null, orderBy, null);
    }

    protected Cursor getCursorByQuery(boolean distinct, String table, String[] columns,
                                      String selection, String[] selectionArgs,
                                      String groupBy, String having, String orderBy,
                                      String limit) {
        Cursor cursor = mDb.query(distinct, table, columns, selection, selectionArgs, groupBy,
                having, orderBy, limit);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }


    protected boolean itemExists (String table,
                                  String selection, String[] selectionArgs) {
        Cursor cursor = mDb.query(table, null, selection, selectionArgs, null,
                null, null, "1");

        if (cursor.moveToFirst())
            return true;
        else return false;
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
