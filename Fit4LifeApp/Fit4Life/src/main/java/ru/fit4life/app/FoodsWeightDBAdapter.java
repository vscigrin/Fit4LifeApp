package ru.fit4life.app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.inputmethod.InputMethodManager;

public class FoodsWeightDBAdapter extends ParentDatabaseAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_GLASS = "per_glass";
    public static final String KEY_SPOON = "per_tablespoon";
    public static final String KEY_PCS = "per_pcs";
    public static final String KEY_GROUPID = "groupid";

    private static final String TAG = "FoodsWeightDBAdapter";

    public Cursor fetchAllAZ() {

        String query = "SELECT * FROM FoodsWeight ORDER BY name ASC";
        return getCursorBySqlString(query);
    }

    public Cursor fetchByFilter(String s) {
        String query = "SELECT * FROM FoodsWeight WHERE name LIKE '%" + s + "%' ORDER BY name ASC COLLATE NOCASE;";
        return getCursorBySqlString(query);
    }

    public Cursor fetchAllGroups() {

        String query = "SELECT * FROM FoodsWeightGroups";
        return getCursorBySqlString(query);
    }

    public Cursor fetchInfoByGroupId(int id) {

        String query = "SELECT * FROM FoodsWeight WHERE GroupId = " + id + " ORDER BY name ASC";
        return getCursorBySqlString(query);
    }

}
