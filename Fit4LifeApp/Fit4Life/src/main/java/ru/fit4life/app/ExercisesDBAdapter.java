package ru.fit4life.app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ExercisesDBAdapter extends ParentDatabaseAdapter {

    public static final String TABLE_NAME = "Exercises";

    public static final String KEY_ROWID = "_id";
    public static final String KEY_ICON = "icon";
    public static final String KEY_NAME = "name";
    public static final String KEY_GROUP_ID = "ref_body_part";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TECHNIQUE = "technique";
    public static final String KEY_WORKING_MUSCLES = "working_muscles";
    public static final String KEY_EXERCISE_TYPE = "exercise_type";
    public static final String KEY_EXERCISE_URL = "exercise_url";

    private static final String TAG = "ExercisesDBAdapter";

    public Cursor fetchExercisesForGroupId(String groupId) {
        //String query = "SELECT * FROM Exercises WHERE ref_body_part = "+groupId;
        return getCursorByQuery(TABLE_NAME, KEY_GROUP_ID + " = ?", new String[]{groupId});
    }

}
