package ru.fit4life.app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ExercisesDBAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_ICON = "icon";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TECHNIQUE = "technique";
    public static final String KEY_WORKING_MUSCLES = "working_muscles";
    public static final String KEY_EXERCISE_TYPE = "exercise_type";
    public static final String KEY_EXERCISE_URL = "exercise_url";

    private static final String TAG = "ExercisesDBAdapter";
    private SQLiteDatabase mDb;

    public ExercisesDBAdapter(Context ctx) {

        mDb = MainActivity.myDb;
    }

    private Cursor getCursorBySqlString(String query) {

        Cursor c = mDb.rawQuery(query, null);
        if (c != null)
            c.moveToFirst();

        return c;
    }

    public Cursor fetchExercisesForGroupId(String groupId) {

        String query = "SELECT * FROM Exercises WHERE ref_body_part = "+groupId;

        return getCursorBySqlString(query);
    }
}
