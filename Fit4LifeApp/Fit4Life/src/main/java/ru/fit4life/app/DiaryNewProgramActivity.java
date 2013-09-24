package ru.fit4life.app;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DiaryNewProgramActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_new_program);

        setTag(this.getClass().getSimpleName());
        //set activity name
        toolbarsManager = new ToolbarsManager(this);
        toolbarsManager.setNameActivity(this.getTitle().toString());

    }


}
