package ru.fit4life.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public abstract class MyActivity extends Activity{

    protected String tag;
    protected ToolbarsManager toolbarsManager;
    protected ParentDatabaseAdapter databaseAdapter;

    private Activity activity;

protected String getTag() {
    return this.tag;
}
    protected void setTag(String tag) {
        this.tag = tag;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
        @Override
    public void onResume() {
        super.onResume();

        if (!ApplicationState.isForegroud()) {
            Log.i(getTag(), "IS NOW FOREGROUND");
            MainActivity.setAppIsUpToDate(false);
            MainActivity.runAppSync(MainActivity.getMainContext());
        }
        ApplicationState.setBackground();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ApplicationState.setForeground();
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (!ApplicationState.isForegroud()) {
            Log.i(getTag(), "IS NOW BACKGROUND");
        }
    }
    public void navigateBack(View view) {
        navigateBack();
    }

    protected void navigateBack(){
        Log.i(getTag(), "navigate back");
        finish();
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }


    public void navigateHome(View view) {

        Log.i(getTag(), "navigate home");
        ApplicationState.setForeground();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }

}
