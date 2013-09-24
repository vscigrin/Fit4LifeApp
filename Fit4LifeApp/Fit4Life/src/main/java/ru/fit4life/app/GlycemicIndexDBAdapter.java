package ru.fit4life.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GlycemicIndexDBAdapter extends ParentDatabaseAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_VALUE = "gi_value";
    private static final String TABLE_NAME = "GlycemicIndex";

    private static final String TAG = "GlycemicIndexDBAdapter";

    public Cursor fetchAll() {

        String query = "SELECT * FROM GlycemicIndex ORDER BY gi_value DESC";
        return getCursorBySqlString(query);
    }

    public Cursor fetchByFilter(String s) {
        String query = "SELECT * FROM GlycemicIndex WHERE lower(name) LIKE '%" + s.toLowerCase() + "%' ORDER BY gi_value DESC;";
        return getCursorBySqlString(query);
    }

    public boolean foodAlreadyExists(String foodName) {

        String query = "SELECT name FROM GlycemicIndex WHERE lower(name) = '" + foodName.toLowerCase() + "'  LIMIT 1;";

        if (getCursorBySqlString(query).getCount() > 0)
            return true;
        else
            return false;
    }


    public void insertNewRow(String name, float value) {
        //Bind values with columns
        ContentValues data = new ContentValues();
        data.put(KEY_NAME, name);
        data.put(KEY_VALUE, value);

        getDb().insert(TABLE_NAME, null, data);
    }

    public void updateRowById(int id, String name, float value) {

        ContentValues data = new ContentValues();
        data.put(KEY_NAME, name);
        data.put(KEY_VALUE, value);

        String whereClause = KEY_ROWID + " = " + id;

        getDb().update(TABLE_NAME, data, whereClause, null);

    }

    public void deleteRowById(int id) {

        String whereClause = KEY_ROWID + " = " + id;

        getDb().delete(TABLE_NAME, whereClause, null);
    }

}
