package ru.fit4life.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class UndergroundItemActivity extends Activity {

    private static final String TAG = "UndergroundItemActivity";
    private TextView textViewArticleTitle;
    private TextView textViewArticleContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_underground_item);

        Intent myIntent = getIntent();
        String title = myIntent.getStringExtra("title");
        String content = myIntent.getStringExtra("content");

        textViewArticleTitle = (TextView) findViewById(R.id.textViewUndergroundItemTitle);
        textViewArticleTitle.setText(title);

        textViewArticleContent = (TextView) findViewById(R.id.articlesTextViewHtmlContent);
        textViewArticleContent.setText(Html.fromHtml(content));
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

        ApplicationState.setForeground();
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }

    public void navigateBack(View view) {
        finish();
    }

    public void navigateHome(View view) {

        ApplicationState.setForeground();
        Intent intent = new Intent(UndergroundItemActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }
}
