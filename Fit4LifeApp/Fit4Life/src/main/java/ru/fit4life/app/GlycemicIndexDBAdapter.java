package ru.fit4life.app;

import android.database.Cursor;

public class GlycemicIndexDBAdapter extends ParentDatabaseAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_VALUE = "gi_value";
    private static final String TABLE_NAME = "GlycemicIndex";

    private static final String TAG = "GlycemicIndexDBAdapter";

    public Cursor fetchAll() {
        //"SELECT * FROM GlycemicIndex ORDER BY gi_value DESC";
        return getCursorByQuery(TABLE_NAME, null, null, KEY_VALUE + " DESC");
    }

    public Cursor fetchByFilter(String s) {
        return getCursorByQuery(TABLE_NAME, KEY_NAME + " LIKE '%?%'", new String[] {s}, KEY_VALUE + " DESC");
    }

    public boolean foodAlreadyExists(String foodName) {
        return itemExists(TABLE_NAME, KEY_NAME + " = ?", new String[]{foodName});
    }


    public void insertNewRow(String name, float value) {
        //Bind values with columns
        data.clear();
        data.put(KEY_NAME, name.toLowerCase());
        data.put(KEY_VALUE, value);

        getDb().insert(TABLE_NAME, null, data);
    }

    public void updateRowById(int id, String name, float value) {
        data.clear();
        data.put(KEY_NAME, name.toLowerCase());
        data.put(KEY_VALUE, value);

        getDb().update(TABLE_NAME, data, KEY_ROWID + " = ?", new String[] {String.valueOf(id)});
    }

    public void deleteRowById(int id) {
        getDb().delete(TABLE_NAME, KEY_ROWID + " = ?", new String[] {String.valueOf(id)});
    }

}
