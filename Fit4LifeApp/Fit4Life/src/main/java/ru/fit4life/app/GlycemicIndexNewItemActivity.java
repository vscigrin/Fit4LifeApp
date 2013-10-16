package ru.fit4life.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GlycemicIndexNewItemActivity extends MyActivity {

    private EditText newName;
    private EditText newValue;
    private GlycemicIndexDBAdapter glycemicIndexDBAdapter;
    private Toast warningToast;
    private TextView toastText;
    private boolean atEdit;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glycemic_index_new_item);

        setTag(this.getClass().getSimpleName());

        glycemicIndexDBAdapter = new GlycemicIndexDBAdapter();
        newName = (EditText) findViewById(R.id.glycemic_index_new_name);
        newValue = (EditText) findViewById(R.id.glycemic_index_new_value);

        warningToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        toastText = (TextView) warningToast.getView().findViewById(android.R.id.message);
        toastText.setTextColor(Color.RED);

        intent = getIntent();
        if (intent.getAction().equals(Intent.ACTION_INSERT))
            atEdit = true; //It's updating existing item, not inserting
        else atEdit = false; //It's inserting new row

        //set activity name
        toolbarsManager = new ToolbarsManager(this);
        toolbarsManager.setNameActivity(this.getTitle().toString());
    }

    @Override
    public void onResume() {
        super.onResume();
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

    public void accept(View view) {

        if (checkCorrectFill()) {

            if (atEdit) {
                Toast.makeText(this, "Product changed", Toast.LENGTH_SHORT).show();
                glycemicIndexDBAdapter.updateRowById(intent.getIntExtra(GlycemicIndexDBAdapter.KEY_ROWID, 0), newName.getText().toString().trim(), Float.parseFloat(newValue.getText().toString()));

                navigateBack();
            }
            else {
            Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
            //insert
            glycemicIndexDBAdapter.insertNewRow(newName.getText().toString().trim(), Float.parseFloat(newValue.getText().toString()));

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
