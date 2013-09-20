package ru.fit4life.app;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

public class FoodsWeightAZFragment extends Fragment {
    private static final String TAG = "FoodsWeightActivity";

    private FoodsWeightDBAdapter foodsWeightDatabaseHelper;
    private SimpleCursorAdapter dataAdapter;
    private View view;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        Log.i(TAG, "show Android fragment");
        view = inflater.inflate(R.layout.fragment_foods_weight_az, container, false);

        // Initialize database adapter for the exercises table
        foodsWeightDatabaseHelper = new FoodsWeightDBAdapter(view.getContext());

        displayListView();


        return view;
    }

    private void displayListView() {
        Log.i(TAG, "display list view inicialized");
        Cursor cursor = foodsWeightDatabaseHelper.fetchAllAZ ();

        if (cursor.getCount() > 0) {
            String[] columns = {
                    FoodsWeightDBAdapter.KEY_NAME,
                    FoodsWeightDBAdapter.KEY_GLASS,
                    FoodsWeightDBAdapter.KEY_SPOON,
                    FoodsWeightDBAdapter.KEY_PCS
            };

            int[] to = {
                    R.id.textViewFoodsWeightName,
                    R.id.textViewFoodsWeightGlass,
                    R.id.textViewFoodsWeightSpoon,
                    R.id.textViewFoodsWeightPcs
            };

            ListView listView = (ListView) view.findViewById(R.id.foodsWeightList);
            Log.i(TAG, String.format("ListView = " + listView.getId()));


            dataAdapter = new SimpleCursorAdapter(
                    getActivity().getApplicationContext(),
                    R.layout.foods_weight_list_row,
                    cursor,
                    columns,
                    to,
                    0);

            Log.i(TAG, String.format("dataAdapter row count = " + dataAdapter.getCount()));

            /*dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence charSequence) {

                    if (charSequence == null)
                        return glycemicIndexDatabaseHelper.fetchAll();

                    return glycemicIndexDatabaseHelper.fetchByFilter(charSequence.toString());

                }
            }
            );
            */

            listView.setAdapter(dataAdapter);
        }

    }

}
