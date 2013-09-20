package ru.fit4life.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "f4l.db";
    private static final int DATABASE_VERSION = 1;

    Context myContext;
    private File dbFile;

    // DatabaseHelper class constructor
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
        //createOrOpen();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.w(TAG, "DB created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "DB updated");
    }

    public boolean dbExists() {
        dbFile = myContext.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    public SQLiteDatabase createOrOpen() {

        if (dbExists()){
            Log.i(TAG, "Db already exists");
        }
        else {
            Log.i(TAG, "Creating empty db");

            this.getReadableDatabase();
            this.close();

            Log.i(TAG, "Empty db created and opened for reading");
            Log.i(TAG, "Empty db closed");

            try {
                Log.i(TAG, "Copying db files");
                copyDB();
            } catch (IOException e) {
                Log.i(TAG,"Can't copy DataBase: " + e.toString());
            }
        }

        return this.getWritableDatabase();
    }

    public void copyDB () throws IOException {

        InputStream input = myContext.getAssets().open(DATABASE_NAME);
        OutputStream output = new FileOutputStream(dbFile);

        byte [] buffer = new byte[1024];
        int length;
        Log.i(TAG, "Input file" + input.toString());
        Log.i(TAG, "Output file" + output.toString());
        while((length = input.read(buffer)) > 0)
            output.write(buffer,0,length);

        output.flush();
        output.close();
        input.close();
        Log.i(TAG, "Copy complete. File size: " + dbFile.getUsableSpace() );
    }
}
