package ru.fit4life.app;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class MainActivity extends Activity {

    public static String PACKAGE_NAME;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize DB
        databaseHelper = new DatabaseHelper(this);

        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();
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
            startActivity(intent);
            overridePendingTransition(R.anim.animation_in_left, R.anim.animation_out_left);
        }
    }
}
