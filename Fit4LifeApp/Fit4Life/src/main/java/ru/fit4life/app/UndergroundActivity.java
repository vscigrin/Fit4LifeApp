package ru.fit4life.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UndergroundActivity extends Activity {

    private static final String TAG = "UndergroundActivity";
    private static final String PATH_ON_SD_CARD = "/Fit4Life/";
    public static ProgressDialog loadingDialog = null;
    private ArticlesDBAdapter articlesDatabaseHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_underground);

        MainActivity.setUndergroundActivityStatus(true);

        // gets the global defined async task from main activity and checks its status, if it is running then presents a message
        if(MainActivity.mTask != null) {
            if (MainActivity.mTask.getStatus() == AsyncTask.Status.RUNNING) {
                loadingDialog = new ProgressDialog(UndergroundActivity.this);
                loadingDialog.setTitle(this.getResources().getString(R.string.loading_dialog_database_is_syncing_title));
                loadingDialog.setMessage(this.getResources().getString(R.string.loading_dialog_database_is_syncing_message));
                loadingDialog.show();
            }
        }
        else {
            articlesDatabaseHelper = new ArticlesDBAdapter();
            displayArticlesListView();
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "activity destroyed");
        MainActivity.setUndergroundActivityStatus(false);
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
        Intent intent = new Intent(UndergroundActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }

    private void displayArticlesListView() {

        Cursor cursor = articlesDatabaseHelper.fetchAllArticles();

        // The desired columns to be bound
        String[] columns = {
                ArticlesDBAdapter.KEY_TITLE,
                ArticlesDBAdapter.KEY_IMAGE
        };

        // the XML defined views which the data will be bound to
        int[] to = {
                R.id.textViewArticlesTitle,
                R.id.imageViewArticlesIcon
        };

        ListView listView = (ListView) findViewById(R.id.articlesList);

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.underground_list_row,
                cursor,
                columns,
                to,
                0);

        dataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue (View view, Cursor cursor, int columnIndex){

                if (view.getId() == R.id.imageViewArticlesIcon) {

                    Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + PATH_ON_SD_CARD + cursor.getString(columnIndex));
                    ImageView IV=(ImageView) view;
                    IV.setImageBitmap(bmp);

                    return true;
                }
                else if(view.getId() == R.id.textViewArticlesTitle) {

                    String title = cursor.getString(columnIndex).substring(0,1).toUpperCase() + cursor.getString(columnIndex).substring(1).toLowerCase();
                    TextView TV=(TextView) view;
                    TV.setText(title);
                    return true;
                }
                else {
                    return false;
                }
            }
        });

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor itemCursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String title = itemCursor.getString(itemCursor.getColumnIndexOrThrow("title"));
                String content = itemCursor.getString(itemCursor.getColumnIndexOrThrow("content"));

                Intent intent = new Intent(getApplicationContext(), UndergroundItemActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("content", content);

                ApplicationState.setForeground();
                startActivity(intent);
                overridePendingTransition(R.anim.animation_in_left, R.anim.animation_out_left);
            }
        });
    }
}
