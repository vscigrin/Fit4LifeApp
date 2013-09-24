package ru.fit4life.app;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class DiaryProgramsActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_programs);

        setTag(this.getClass().getSimpleName());
        //set activity name
        toolbarsManager = new ToolbarsManager(this);
        toolbarsManager.setNameActivity(this.getTitle().toString());

    }

    public void showSubItem(View view) {
        Intent intent = null;

        switch (view.getId()) {
            /* show create new program activity */
            case R.id.diaryProgramsNewProgram: {
                intent = new Intent(this, DiaryNewProgramActivity.class);
            }
            break;
            /* show saved programs */
            case R.id.diaryProgramsSavedPrograms: {
                //intent = new Intent(this, GlycemicIndexActivity.class);
            }
            break;
        }
        if (intent != null) {
            ApplicationState.setForeground();
            startActivity(intent);
            overridePendingTransition(R.anim.animation_in_left, R.anim.animation_out_left);
        }
    }

}
