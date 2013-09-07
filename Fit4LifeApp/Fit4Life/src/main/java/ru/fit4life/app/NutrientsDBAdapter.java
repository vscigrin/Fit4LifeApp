package ru.fit4life.app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ivanuch on 07.09.13.
 */
public class NutrientsDBAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_ICON = "icon";
    public static final String KEY_NAME = "name";
    public static final String KEY_PROTEINS = "proteins";
    public static final String KEY_FATS = "fats";
    public static final String KEY_CARBOHYDRATES = "carbohydrates";
    public static final String KEY_CALORIES = "kcal";

    private static final String TAG = "ExercisesDBAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    public NutrientsDBAdapter(Context ctx) {
        mDbHelper = new DatabaseHelper(ctx);
        mDb = mDbHelper.getWritableDatabase();
    }

    public void closeDatabaseHelper() {
        mDbHelper.close();
    }

    private Cursor getCursorBySqlString(String query) {

        Cursor c = mDb.rawQuery(query, null);
        if (c != null)
            c.moveToFirst();

        return c;
    }

    public Cursor fetchAll() {

        String query = "SELECT * FROM nutrients ORDER BY name";
        return getCursorBySqlString(query);
    }
}
