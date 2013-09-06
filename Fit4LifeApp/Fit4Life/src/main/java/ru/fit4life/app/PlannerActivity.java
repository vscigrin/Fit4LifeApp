package ru.fit4life.app;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class PlannerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);
    }

    public void navigateBack(View view) {
        finish();
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }

    public void navigateHome(View view) {

        Intent intent = new Intent(PlannerActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }
}
