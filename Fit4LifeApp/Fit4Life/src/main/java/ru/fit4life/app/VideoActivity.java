package ru.fit4life.app;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;

public class VideoActivity extends Activity {

    private static final String TAG = "VideoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }

    public void navigateBack(View view) {
        finish();
    }

    public void navigateHome(View view) {

        ApplicationState.setForeground();
        Intent intent = new Intent(VideoActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }
}
