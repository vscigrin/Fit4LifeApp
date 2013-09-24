package ru.fit4life.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NutrientsNewItemActivity extends MyActivity {

    private EditText newName;
    private EditText newProteins;
    private EditText newFats;
    private EditText newCarbs;
    private EditText newCalories;
    private NutrientsDBAdapter nutrientsDBAdapter;
    private Toast warningToast;
    private TextView toastText;
    private boolean atEdit;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrients_new_item);

        setTag(this.getClass().getSimpleName());
        //set activity name
        toolbarsManager = new ToolbarsManager(this);
        toolbarsManager.setNameActivity(this.getTitle().toString());

        nutrientsDBAdapter = new NutrientsDBAdapter();
        newName = (EditText) findViewById(R.id.nutrients_new_name);
        newProteins = (EditText) findViewById(R.id.nutrients_new_proteins);
        newFats = (EditText) findViewById(R.id.nutrients_new_fats);
        newCarbs = (EditText) findViewById(R.id.nutrients_new_carbs);
        newCalories = (EditText) findViewById(R.id.nutrients_new_calories);


        warningToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        toastText = (TextView) warningToast.getView().findViewById(android.R.id.message);
        toastText.setTextColor(Color.RED);

        intent = getIntent();
        if (intent.getAction().equals(getString(R.string.atEdit)))
            atEdit = true; //It's updating existing item, not inserting
        else atEdit = false; //It's inserting new row

    }

    @Override
    public void onResume() {
        super.onResume();
        if (atEdit)
            fillData();
    }

    private void fillData() {
        newName.setText(intent.getStringExtra(NutrientsDBAdapter.KEY_NAME));
        newProteins.setText(String.valueOf(intent.getFloatExtra(NutrientsDBAdapter.KEY_PROTEINS, 0)));
        newFats.setText(String.valueOf(intent.getFloatExtra(NutrientsDBAdapter.KEY_FATS, 0)));
        newCarbs.setText(String.valueOf(intent.getFloatExtra(NutrientsDBAdapter.KEY_CARBOHYDRATES, 0)));
        newCalories.setText(String.valueOf(intent.getFloatExtra(NutrientsDBAdapter.KEY_CALORIES, 0)));
    }

    private boolean checkCorrectFill() {
        String name = newName.getText().toString().trim();
        float proteins = 0;
        float fats = 0;
        float carbs = 0;
        float calories = 0;

        if (atEdit) {
            if (name.length() == 0) {
                toastText.setText(R.string.tables_enter_product_name);
                warningToast.show();
                return false;
            }

        } else {
            if (name.length() > 0) {
                if (nutrientsDBAdapter.foodAlreadyExists(name)) {
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
        if (newProteins.getText().toString().isEmpty()) {
            toastText.setText(R.string.tables_nutrients_proteins_is_empty);
            warningToast.show();
            return false;
        } else proteins = Float.parseFloat(newProteins.getText().toString());

        if (newFats.getText().toString().isEmpty()) {
            toastText.setText(R.string.tables_nutrients_fats_is_empty);
            warningToast.show();
            return false;
        } else fats = Float.parseFloat(newFats.getText().toString());

        if (newCarbs.getText().toString().isEmpty()) {
            toastText.setText(R.string.tables_nutrients_carbs_is_empty);
            warningToast.show();
            return false;
        } else carbs = Float.parseFloat(newCarbs.getText().toString());

        if (newCalories.getText().toString().isEmpty()) {
            toastText.setText(R.string.tables_nutrients_calories_is_empty);
            warningToast.show();
            return false;
        } else calories = Float.parseFloat(newCalories.getText().toString());


        if (proteins + fats + carbs > 100) {
            toastText.setText(R.string.tables_nutrients_values_to_match);
            warningToast.show();
            return false;
        }

        return true;
    }

    public void accept(View view) {

        if (checkCorrectFill()) {

            if (atEdit) {
                Toast.makeText(this, "Product changed", Toast.LENGTH_SHORT).show();
                nutrientsDBAdapter.updateRowById(intent.getIntExtra(GlycemicIndexDBAdapter.KEY_ROWID, 0),
                        newName.getText().toString().trim(),
                        Float.parseFloat(newProteins.getText().toString()),
                        Float.parseFloat(newFats.getText().toString()),
                        Float.parseFloat(newCarbs.getText().toString()),
                        Float.parseFloat(newCalories.getText().toString())
                );

                navigateBack();
            } else {
                Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
                //insert
                nutrientsDBAdapter.insertNewRow(
                        newName.getText().toString().trim(),
                        Float.parseFloat(newProteins.getText().toString()),
                        Float.parseFloat(newFats.getText().toString()),
                        Float.parseFloat(newCarbs.getText().toString()),
                        Float.parseFloat(newCalories.getText().toString())
                );

                //clear forms
                newName.getEditableText().clear();
                newProteins.getEditableText().clear();
                newFats.getEditableText().clear();
                newCarbs.getEditableText().clear();
                newCalories.getEditableText().clear();
            }
        }
    }

    public void cancel(View view) {
        Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        navigateBack();

    }


}
