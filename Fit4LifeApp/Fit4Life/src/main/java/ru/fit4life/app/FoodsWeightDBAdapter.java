package ru.fit4life.app;

import android.content.ContentValues;
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
    public static final String KEY_GROUPID = "groupId";

    private static final String CHILD_TABLE_NAME = "FoodsWeight";
    private static final String PARENT_TABLE_NAME = "FoodsWeightGroups";


    private static final String TAG = "FoodsWeightDBAdapter";

    public boolean parentRowAlreadyExists(String name) {
        return itemExists(PARENT_TABLE_NAME, KEY_NAME + " = ?", new String[]{name});
    }


    public boolean childRowAlreadyExists(String name, String groupId) {
        return itemExists(CHILD_TABLE_NAME, KEY_NAME + " = ? AND " + KEY_GROUPID + " = ?", new String[]{name, groupId});
    }

    public Cursor fetchAllParents() {

        return getCursorByQuery(PARENT_TABLE_NAME);
    }

    public Cursor fetchChildesByParentId(int id) {

        return getCursorByQuery(CHILD_TABLE_NAME, KEY_GROUPID + " =  ?",
                new String[]{String.valueOf(id)}, KEY_NAME);

    }

    public void insertNewParentRow(String name) {
        //Bind values with columns
        data.clear();
        data.put(KEY_NAME, name);

        getDb().insert(PARENT_TABLE_NAME, null, data);
    }

    public void insertNewChildRow(String name, String glass, String spoon, String pcs, int groupId) {
        //Bind values with columns
        data.clear();
        data.put(KEY_NAME, name);
        data.put(KEY_GLASS, glass);
        data.put(KEY_SPOON, spoon);
        data.put(KEY_PCS, pcs);
        data.put(KEY_GROUPID, groupId);

        getDb().insert(CHILD_TABLE_NAME, null, data);
    }

    public void updateParentRowById(String id, String name) {
        data.clear();
        data.put(KEY_NAME, name);

        getDb().update(PARENT_TABLE_NAME, data, KEY_ROWID + " = ?", new String[]{id});
    }

    public void updateChildRowById(String id, String name, String glass, String spoon, String pcs, String groupId) {
        data.clear();
        data.put(KEY_NAME, name);
        data.put(KEY_GLASS, glass);
        data.put(KEY_SPOON, spoon);
        data.put(KEY_PCS, pcs);
        data.put(KEY_GROUPID, groupId);

        getDb().update(CHILD_TABLE_NAME, data, KEY_ROWID + " = ?", new String[]{id});
    }

    public void deleteParentRowById(String id) {
        getDb().delete(CHILD_TABLE_NAME, KEY_GROUPID + " = ?", new String[]{id}); //delete from child table
        getDb().delete(PARENT_TABLE_NAME, KEY_ROWID + " = ?", new String[]{id}); //and delete from group table
    }

    public void deleteChildRowById(String id) {
        getDb().delete(CHILD_TABLE_NAME, KEY_ROWID + " = ?", new String[]{id}); //delete from child table
    }
}
