package ru.fit4life.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DiaryActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        setTag(this.getClass().getSimpleName());
        //set activity name
        toolbarsManager = new ToolbarsManager(this);
        toolbarsManager.setNameActivity(this.getTitle().toString());
    }

    public void showSubItem(View view) {
        Intent intent = null;

        switch (view.getId()) {
            /* show my diary activity */
            case R.id.diaryMyDiary: {
                //intent = new Intent(this, NutrientsActivity.class);
            }
            break;
            /* show programs activity */
            case R.id.diaryPrograms: {
                intent = new Intent(this, DiaryProgramsActivity.class);
            }
            break;
            /* show my progress activity */
            case R.id.diaryMyProgress: {
                //intent = new Intent(this, FoodsWeightActivity.class);
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
