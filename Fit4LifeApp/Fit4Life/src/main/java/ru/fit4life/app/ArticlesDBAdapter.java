package ru.fit4life.app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ArticlesDBAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_URL = "url";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_DATE = "date";

    private static final String TAG = "ArticlesDBAdapter";
    private SQLiteDatabase mDb;

    public ArticlesDBAdapter(Context ctx) {
        mDb = MainActivity.myDb;
    }

    private Cursor getCursorBySqlString(String query) {

        Cursor c = mDb.rawQuery(query, null);
        if (c != null)
            c.moveToFirst();

        return c;
    }

    public Cursor fetchAllArticles() {

        String query = "SELECT * FROM Articles ORDER BY date DESC";

        return getCursorBySqlString(query);
    }

    public Cursor fetchArticleById(String id) {

        String query = "SELECT * FROM Articles where _id="+id;

        return getCursorBySqlString(query);
    }

    public void insertArticle(int id, String title, String content, String url, String image, String date) {

        try {
            String sql = "INSERT or REPLACE INTO Articles (_id, title, content, url, image, date) VALUES("+id+", '"+title+"', '"+content+"', '"+url+"', '"+image+"', '"+date+"')" ;
            mDb.execSQL(sql);
        }
        catch (Exception e) {
            Log.e(TAG, "ERROR " + e.toString());
        }
    }
}
