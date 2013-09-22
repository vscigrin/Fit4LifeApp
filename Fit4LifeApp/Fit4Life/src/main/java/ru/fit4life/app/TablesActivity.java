package ru.fit4life.app;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TablesActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);

        setTag(this.getClass().getSimpleName());
//        fillNavigationName();
    }

    public void showTable(View view) {
        Intent intent = null;

        switch (view.getId()) {
            /* show tableNutrients table */
            case R.id.tableNutrients: {
                Log.i(getTag(), "R.id.tableNutrients selected");
                intent = new Intent(this, NutrientsActivity.class);
            }
            break;
            /* show glycemic Indexes table */
            case R.id.tableGlycemicIndex: {
                Log.i(getTag(), "R.id.tableGlycIndex selected");
                intent = new Intent(this, GlycemicIndexActivity.class);
            }
            break;
            /* show weighting table */
            case R.id.tableWeighting: {
                Log.i(getTag(), "R.id.tableWeighting selected");
                intent = new Intent(this, FoodsWeightActivity.class);
            }
            break;
            /* show Sport Nutrition table */
            case R.id.tableSportNutrition: {
                Log.i(getTag(), "R.id.tableSportNutrition selected");
            }
            break;
        }
        if (intent != null) {
            ApplicationState.setForeground();
            startActivity(intent);
            overridePendingTransition(R.anim.animation_in_left, R.anim.animation_out_left);
        }
    }

    //Show text in top
/*    private void fillNavigationName() {
        TextView name = (TextView) findViewById(R.id.navigation_toolbar_name);
        name.setText(R.string.title_activity_tables);
    }
*/
}
