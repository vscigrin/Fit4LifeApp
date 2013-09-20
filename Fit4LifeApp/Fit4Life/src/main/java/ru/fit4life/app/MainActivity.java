package ru.fit4life.app;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import org.json.JSONObject;
import java.net.URL;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private DatabaseHelper databaseHelper;

    public static SQLiteDatabase myDb;
    public static AsyncTask<URL, Void, JSONObject> mTask = null;
    private static boolean undergroundActivityIsActive = false;
    private static boolean appIsUpToDate = false;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setMainContext(this.getApplicationContext());

        if(!ApplicationState.isForegroud()) {
            Log.i(TAG, "APP STARTED FOR THE FIRST TIME OR RESUMED FROM BACGROUND");

            // Initialize DB
            databaseHelper = new DatabaseHelper(this);
            myDb = databaseHelper.createOrOpen();
            Log.i(TAG, "DB INITIALISED");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!ApplicationState.isForegroud()) {
            Log.i(TAG, "IS NOW FOREGROUND");
            MainActivity.setAppIsUpToDate(false);
            MainActivity.runAppSync(MainActivity.getMainContext());
        }

        ApplicationState.setBackground();
    }

    @Override
    public void onPause() {
        super.onPause();

        if(!ApplicationState.isForegroud()) {
            Log.i(TAG, "IS NOW BACKGROUND");
        }
    }


    public void showSubMenu(View view) {

        Intent intent = null;

        /* based on the button clicked load the appropriate activity */
        switch (view.getId()) {
            /* show exercises */
            case R.id.main_btn_exercises: {
                intent = new Intent(this, BodyPartsActivity.class);
            } break;

            /* show tables */
            case R.id.main_btn_tables: {
                intent = new Intent(this, TablesActivity.class);
            } break;

            /* show video */
            case R.id.main_btn_video: {
                intent = new Intent(this, VideoActivity.class);
            } break;

            /* show underground */
            case R.id.main_btn_underground: {
                intent = new Intent(this, UndergroundActivity.class);
            } break;

            /* show diary */
            case R.id.main_btn_diary: {
                intent = new Intent(this, DiaryActivity.class);
            } break;

            /* show audio */
            case R.id.main_btn_audio: {
                intent = new Intent(this, AudioActivity.class);
            } break;

            /* show planner */
            case R.id.main_btn_planner: {
                intent = new Intent(this, PlannerActivity.class);
            } break;

            /* show measurements */
            case R.id.main_btn_measurements: {
                intent = new Intent(this, MeasurementsActivity.class);
            } break;

            /* show communication */
            case R.id.main_btn_communication: {
                intent = new Intent(this, CommunicationActivity.class);
            } break;
        }

        if (intent != null) {
            ApplicationState.setForeground();
            startActivity(intent);
            overridePendingTransition(R.anim.animation_in_left, R.anim.animation_out_left);
        }
    }




    public static void runAppSync(Context context) {
        // we start a new asynchronous task to update our database with latest changes
        if (!getAppIsUpToDate()) {
            if (mTask==null) {
                Log.i(TAG, "App is not up to date and needs to be synchronized!");
                mTask = new JSONParser(context);
                mTask.execute();
            }
        }
    }

    public static Context getMainContext() {
        return MainActivity.mContext;
    }

    public static void setMainContext(Context context) {
        MainActivity.mContext = context;
    }

    public static boolean getAppIsUpToDate() {
        return MainActivity.appIsUpToDate;
    }

    public static void setAppIsUpToDate(boolean status) {
        MainActivity.appIsUpToDate = status;
    }

    public static boolean getUndergroundActivityStatus() {
        return MainActivity.undergroundActivityIsActive;
    }

    public static void setUndergroundActivityStatus(boolean status) {
        MainActivity.undergroundActivityIsActive = status;
    }
}
