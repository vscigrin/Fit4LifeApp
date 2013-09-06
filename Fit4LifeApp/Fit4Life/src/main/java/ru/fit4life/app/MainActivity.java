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
            case R.id.button00: {
                intent = new Intent(this, BodyPartsActivity.class);
            } break;

            /* show tables */
            case R.id.button01: {
                intent = new Intent(this, TablesActivity.class);
            } break;

            /* show video */
            case R.id.button02: {
                intent = new Intent(this, VideoActivity.class);
            } break;

            /* show underground */
            case R.id.button10: {
                intent = new Intent(this, UndergroundActivity.class);
            } break;

            /* show diary */
            case R.id.button11: {
                intent = new Intent(this, DiaryActivity.class);
            } break;

            /* show audio */
            case R.id.button12: {
                intent = new Intent(this, AudioActivity.class);
            } break;

            /* show planner */
            case R.id.button20: {
                intent = new Intent(this, PlannerActivity.class);
            } break;

            /* show measurements */
            case R.id.button21: {
                intent = new Intent(this, MeasurementsActivity.class);
            } break;

            /* show communication */
            case R.id.button22: {
                intent = new Intent(this, CommunicationActivity.class);
            } break;
        }

        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.animation_in_left, R.anim.animation_out_left);
        }
    }
}
