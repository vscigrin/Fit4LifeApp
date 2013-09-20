package ru.fit4life.app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Ivanuch on 08.09.13.
 */
public class GlycemicIndexDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_VALUE = "gi_value";

    private static final String TAG = "GlycemicIndexDBAdapter";
    private SQLiteDatabase mDb;



    public GlycemicIndexDBAdapter(Context ctx) {
        mDb = MainActivity.myDb;
    }

    private Cursor getCursorBySqlString(String query) {

        Cursor c = mDb.rawQuery(query, null);
        if (c != null)
            c.moveToFirst();

        return c;
    }

    public Cursor fetchAll() {

        String query = "SELECT * FROM GlycemicIndex ORDER BY gi_value DESC";
        return getCursorBySqlString(query);
    }

    public Cursor fetchByFilter(String s) {
        String query = "SELECT * FROM GlycemicIndex WHERE name LIKE '%" + s + "%' ORDER BY gi_value DESC";
        return getCursorBySqlString(query);
    }
}
