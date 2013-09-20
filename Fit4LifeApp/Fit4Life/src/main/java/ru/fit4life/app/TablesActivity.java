package ru.fit4life.app;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TablesActivity extends Activity {

    private static final String TAG = "TablesActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);

        fillNavigationName();
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

    public void showTable(View view) {
        Intent intent = null;

        switch (view.getId()) {
            /* show tableNutrients table */
            case R.id.tableNutrients: {
                Log.i(TAG, "R.id.tableNutrients selected");
                intent = new Intent(this, NutrientsActivity.class);
            }
            break;
            /* show glycemic Indexes table */
            case R.id.tableGlycemicIndex: {
                Log.i(TAG, "R.id.tableGlycIndex selected");
                intent = new Intent(this, GlycemicIndexActivity.class);
            }
            break;
            /* show weighting table */
            case R.id.tableWeighting: {
                Log.i(TAG, "R.id.tableWeighting selected");
            }
            break;
            /* show Sport Nutrition table */
            case R.id.tableSportNutrition: {
                Log.i(TAG, "R.id.tableSportNutrition selected");
            }
            break;
        }
        if (intent != null) {
            ApplicationState.setForeground();
            startActivity(intent);
            overridePendingTransition(R.anim.animation_in_left, R.anim.animation_out_left);
        }
    }

    @Override
    public void finish() {
        super.finish();

        ApplicationState.setForeground();
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }

    public void navigateBack(View view) {
        finish();
    }

    public void navigateHome(View view) {

        ApplicationState.setForeground();
        Intent intent = new Intent(TablesActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }
    //Show text in top
    private void fillNavigationName() {
        TextView name = (TextView) findViewById(R.id.navigation_toolbar_name);
        name.setText(R.string.title_activity_tables);
    }

}
