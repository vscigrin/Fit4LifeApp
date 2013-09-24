package ru.fit4life.app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BodyPartsDBAdapter extends ParentDatabaseAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_ICON = "icon";
    public static final String KEY_NAME = "name";

    private static final String TAG = "BodyPartsDBAdapter";


    public Cursor fetchAllBodyParts() {

        String query = "SELECT * FROM BodyParts";

        return getCursorBySqlString(query);
    }
}
