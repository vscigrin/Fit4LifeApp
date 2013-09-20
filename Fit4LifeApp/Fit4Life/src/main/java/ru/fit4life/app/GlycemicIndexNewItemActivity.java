package ru.fit4life.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GlycemicIndexNewItemActivity extends Activity {

    private static final String TAG = "GlycemicIndexNewItemActivity";

    private EditText newName;
    private EditText newValue;
    private TextWatcher textWatcher;
    private Button buttonAccept;
    private GlycemicIndexDBAdapter glycemicIndexDBAdapter;
    private Toast warningToast;
    private TextView toastText;
    private boolean atEdit;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glycemic_index_new_item);

        glycemicIndexDBAdapter = new GlycemicIndexDBAdapter(this);
        newName = (EditText) findViewById(R.id.glycemic_index_new_name);
        newValue = (EditText) findViewById(R.id.glycemic_index_new_value);
        buttonAccept = (Button) findViewById(R.id.glycemic_index_new_button_accept);

        warningToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        toastText = (TextView) warningToast.getView().findViewById(android.R.id.message);
        toastText.setTextColor(Color.RED);

        intent = getIntent();
        if (intent.getAction().equals(getString(R.string.atEdit)))
            atEdit = true;
        else atEdit = false;

        if (atEdit)
            fillData();


    }

    private void fillData() {
        newName.setText(intent.getStringExtra(GlycemicIndexDBAdapter.KEY_NAME));
        newValue.setText(String.valueOf(intent.getFloatExtra(GlycemicIndexDBAdapter.KEY_VALUE, 0)));
    }

    private boolean checkCorrectFill() {
        String name = newName.getText().toString().trim();

        if (atEdit) {
            if (name.length() == 0) {
                toastText.setText(R.string.tables_enter_product_name);
                warningToast.show();
                return false;
            }

        } else {
            if (name.length() > 0) {
                if (glycemicIndexDBAdapter.foodAlreadyExists(name)) {
                    toastText.setText(R.string.tables_product_already_exists);
                    warningToast.show();
                    return false;
                }
            } else {
                toastText.setText(R.string.tables_enter_product_name);
                warningToast.show();
                return false;
            }
        }
        if (newValue.getText().toString().isEmpty()) {
            toastText.setText(R.string.tables_glycemic_index_value_is_empty);
            warningToast.show();
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //glycemicIndexDBAdapter.closeDatabaseHelper();
        Log.i(TAG, String.format("View destroyed and database closed"));
    }


    public void navigateBack(View view) {
        navigateBack();
    }

    private void navigateBack() {
        finish();
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }

    public void navigateHome(View view) {

        Intent intent = new Intent(GlycemicIndexNewItemActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }


    public void accept(View view) {

        if (checkCorrectFill()) {

            if (atEdit) {
                Toast.makeText(this, "Product changed", Toast.LENGTH_SHORT).show();
                glycemicIndexDBAdapter.updateFoodById(intent.getIntExtra(GlycemicIndexDBAdapter.KEY_ROWID, 0), newName.getText().toString().trim(), Float.parseFloat(newValue.getText().toString()) );

                navigateBack();
            }
            else {
            Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
            //insert
            glycemicIndexDBAdapter.insertNewFood(newName.getText().toString().trim(), Float.parseFloat(newValue.getText().toString()));

            //clear forms
            newName.getEditableText().clear();
            newValue.getEditableText().clear();
            }
        }
    }

    public void cancel(View view) {
        Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        navigateBack();

    }

}
