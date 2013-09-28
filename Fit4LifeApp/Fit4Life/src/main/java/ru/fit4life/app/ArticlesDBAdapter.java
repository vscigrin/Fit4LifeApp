package ru.fit4life.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ArticlesDBAdapter extends ParentDatabaseAdapter {

    public static final String TABLE_NAME = "Articles";

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_URL = "url";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_DATE = "date";


    private static final String TAG = "ArticlesDBAdapter";

    public Cursor fetchAllArticles() {
        //SELECT * FROM Articles ORDER BY date DESC
        return getCursorByQuery(TABLE_NAME, null, null, KEY_DATE + " DESC");
    }

    public Cursor fetchArticleById(String id) {

        //SELECT * FROM Articles where _id= id
        return getCursorByQuery(TABLE_NAME, KEY_ROWID + " = ?", new String[] {id});
    }

    public void insertArticle(int id, String title, String content, String url, String image, String date) {

        try {
            //Bind values with columns
            data.clear();
            data.put(KEY_ROWID, id);
            data.put(KEY_TITLE, title);
            data.put(KEY_CONTENT, content);
            data.put(KEY_URL, url);
            data.put(KEY_IMAGE, image);
            data.put(KEY_DATE, date);

            getDb().insert(TABLE_NAME, null, data);
        } catch (Exception e) {
            Log.e(TAG, "Insert ERROR!! " + e.toString());
        }
    }
}
