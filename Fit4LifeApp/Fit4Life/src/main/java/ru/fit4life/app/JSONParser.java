package ru.fit4life.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JSONParser extends AsyncTask<URL, Void, JSONObject> {

    private static final String TAG = "JSONParser";
    public static final String PREFS_NAME = "F4LPrefsFile";

    private static final String INITIAL_SYNC_DATE = "1983-10-12";
    private static final String WEB_SERVICE_URL = "http://www.fit4life.ru/web_service/fit4life_web_service.php";
    //private static final String WEB_SERVICE_URL = "http://192.168.2.9/fit4life/fit4life_web_service.php";
    private static final String WEB_SERVICE_URL_PREFIX_ARTICLES = "?lastSyncDate=";

    private static final String PATH_ON_SD_CARD = "/Fit4Life/";

    private static final String TAG_POSTS = "posts";
    private static final String TAG_POST = "post";
    private static final String TAG_POST_ID = "ID";
    private static final String TAG_POST_TITLE = "post_title";
    private static final String TAG_POST_CONTENT = "post_content";
    private static final String TAG_POST_IMAGE = "post_image";
    private static final String TAG_POST_DATE = "post_modified";

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    static JSONArray postsArray;

    public String lastSyncDate;

    private ArticlesDBAdapter articlesDatabaseHelper;

    private Context mContext;

    public JSONParser (Context context){
        mContext = context;
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {

        articlesDatabaseHelper = new ArticlesDBAdapter();

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            lastSyncDate = getLastSyncDate();

            HttpGet httpGet = new HttpGet(WEB_SERVICE_URL + WEB_SERVICE_URL_PREFIX_ARTICLES + lastSyncDate);
            HttpResponse httpResponse = httpClient.execute(httpGet);

            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        try {
            jObj = new JSONObject(json);
            postsArray = jObj.getJSONArray(TAG_POSTS);

            Log.i(TAG, "total number of received records for Articles = "+postsArray.length());

            for (int i = 0; i < postsArray.length(); i++) {
                JSONObject posts = postsArray.getJSONObject(i);
                JSONObject post = posts.getJSONObject(TAG_POST);
                int post_id = post.getInt(TAG_POST_ID);
                String post_title = post.getString(TAG_POST_TITLE);
                String post_content = post.getString(TAG_POST_CONTENT);
                String post_url = post.getString(TAG_POST_IMAGE);
                String post_image = Integer.toString(post_id) + "." + post_url.substring(post_url.length() - 3);
                String post_date = post.getString(TAG_POST_DATE);

                articlesDatabaseHelper.insertArticle(post_id, post_title, post_content, post_url, post_image, post_date);

                saveImage(post_url, post_image);
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jObj;
    }

    protected void onPostExecute(JSONObject json) {

        super.onPostExecute(json);

        Log.i(TAG, "onPostExecute");
        Cursor cursor = articlesDatabaseHelper.fetchAllArticles();
        Log.i(TAG, "total number of records in table Articles = "+cursor.getCount());

        saveLastSyncDate();
        MainActivity.setAppIsUpToDate(true);

        Log.e(TAG, "!! POST EXECUTE !!");
        Log.e(TAG, "MainActivity.undergroundActivityIsActive = "+MainActivity.getUndergroundActivityStatus());
        if (MainActivity.getUndergroundActivityStatus()) {
            if (UndergroundActivity.loadingDialog != null) {
                Log.e(TAG, "!! DISSMISSED !!");
                UndergroundActivity.loadingDialog.dismiss();
            }
        }

        MainActivity.mTask = null;
    }

    protected String getLastSyncDate() {

        // Restore preferences
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        String lsd = settings.getString("lastSyncDate", "");

        if (lsd.length() > 0) {
            Log.i(TAG, "Retrieved LastSyncDate: " + lsd);
        }
        else {
            lsd = INITIAL_SYNC_DATE;
            Log.i(TAG, "LastSyncDate was not set, using default: " + lsd);
        }

        return lsd;
    }

    protected void saveLastSyncDate() {

        Date currentDateTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        lastSyncDate = sdf.format(currentDateTime);

        //Modification of the Preferences
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("lastSyncDate", lastSyncDate);

        // Commit the edits!
        editor.commit();

        Log.i(TAG, "New LastSyncDate saved: " + lastSyncDate);
    }

    public void saveImage(String imageUrl, String imageName) {

        try {

            if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {

                File direct = new File(Environment.getExternalStorageDirectory() + PATH_ON_SD_CARD);

                // checks if directory exists and if not creates one
                if(!direct.exists()) {
                    if(direct.mkdir()) {
                        Log.i(TAG, "Directory created!");
                    }
                }

                File outputFile = new File(direct, imageName);

                //checks if this image already exists and if not downloads
                if(!outputFile.exists()) {

                    String[] separatedUrl = imageUrl.split("/");
                    String tmpUrl = separatedUrl[separatedUrl.length - 1];
                    tmpUrl = URLEncoder.encode(tmpUrl, "utf-8");
                    separatedUrl[separatedUrl.length - 1] = tmpUrl;
                    imageUrl = TextUtils.join("/", separatedUrl);

                    URL url = new URL(imageUrl);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream(outputFile);

                    byte data[] = new byte[1024];
                    int count;
                    while ((count = input.read(data)) != -1) {
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();

                    Log.i(TAG, "Saved image - "+imageName);
                }
                else {
                    Log.i(TAG, "No need to download, file already exists - "+imageName);
                }
            }
        } catch (IOException e) {
            Log.i(TAG,"Can't save image: " + e.toString());
        }
    }
}