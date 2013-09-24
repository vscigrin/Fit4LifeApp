package ru.fit4life.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ivanuch on 07.09.13.
 */
public class NutrientsDBAdapter extends ParentDatabaseAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PROTEINS = "proteins";
    public static final String KEY_FATS = "fats";
    public static final String KEY_CARBOHYDRATES = "carbohydrates";
    public static final String KEY_CALORIES = "calories";
    private static final String TABLE_NAME = "GlycemicIndex";

    private static final String TAG = "NutrientsDBAdapter";

    public Cursor fetchAll() {
        String query = "SELECT * FROM nutrients ORDER BY name";
        return getCursorBySqlString(query);
    }

    public Cursor fetchByFilter(String s) {
        String query = "SELECT * FROM nutrients WHERE name LIKE '%" + s + "%' ORDER BY name";
        return getCursorBySqlString(query);
    }

    public boolean foodAlreadyExists(String foodName) {

        String query = "SELECT name FROM nutrients WHERE name = '" + foodName + "' ORDER BY name ASC LIMIT 1";

        if (getCursorBySqlString(query).getCount() > 0)
            return true;
        else
            return false;
    }


    public void insertNewRow(String name, float proteins, float fats, float carbs, float calories) {
        //Bind values with columns
        ContentValues data = new ContentValues();
        data.put(KEY_NAME, name);
        data.put(KEY_PROTEINS, proteins);
        data.put(KEY_FATS, fats);
        data.put(KEY_CARBOHYDRATES, carbs);
        data.put(KEY_CALORIES, calories);

        getDb().insert(TABLE_NAME, null, data);
    }

    public void updateRowById(int id, String name, float proteins, float fats, float carbs, float calories) {
        //Bind values with columns
        ContentValues data = new ContentValues();
        data.put(KEY_NAME, name);
        data.put(KEY_PROTEINS, proteins);
        data.put(KEY_FATS, fats);
        data.put(KEY_CARBOHYDRATES, carbs);
        data.put(KEY_CALORIES, calories);

        String whereClause = KEY_ROWID + " = " + id;

        getDb().update(TABLE_NAME, data, whereClause, null);
    }

    public void deleteRowById(int id) {

        String whereClause = KEY_ROWID + " = " + id;

        getDb().delete(TABLE_NAME, whereClause, null);
    }

}
