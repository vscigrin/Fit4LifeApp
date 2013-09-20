package ru.fit4life.app;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleCursorTreeAdapter;

public class FoodsWeightByGroupsFragment extends Fragment {
    private static final String TAG = "FoodsWeightActivity";

    private FoodsWeightDBAdapter foodsWeightDatabaseHelper;
    private View view;
    private MyExpandableListAdapter adapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        Log.i(TAG, "show Groups fragment");
        view = inflater.inflate(R.layout.fragment_foods_weight_bygroups, container, false);

        // Initialize database adapter for the exercises table
        foodsWeightDatabaseHelper = new FoodsWeightDBAdapter(view.getContext());

        displayListView();

        return view;
    }

    private void displayListView() {

        Log.i(TAG, "Expandable list view inicialized");

        Cursor parentCursor = foodsWeightDatabaseHelper.fetchAllGroups();
        String[] parentColumns = new String[]{
                FoodsWeightDBAdapter.KEY_NAME,
        };

        // the XML defined views which the data will be bound to
        int[] parentTo = new int[]{
                android.R.id.text1
        };
        String[] childColumns = new String[]{
                FoodsWeightDBAdapter.KEY_NAME,
                FoodsWeightDBAdapter.KEY_GLASS,
                FoodsWeightDBAdapter.KEY_SPOON,
                FoodsWeightDBAdapter.KEY_PCS
        };

        int[] childTo = new int[]{
                R.id.textViewFoodsWeightName,
                R.id.textViewFoodsWeightGlass,
                R.id.textViewFoodsWeightSpoon,
                R.id.textViewFoodsWeightPcs
        };
        ExpandableListView elv = (ExpandableListView) view.findViewById(R.id.foodsWeightGroupsList);
        Log.i(TAG, String.format("ExpListView = " + elv.getId()));

        this.adapter = new MyExpandableListAdapter(
                parentCursor,
                view.getContext(),
                R.layout.foods_weight_exp_list_row,
                // android.R.layout.simple_expandable_list_item_1,
                R.layout.foods_weight_list_row,
                parentColumns,
                parentTo,
                childColumns,
                childTo
        );

        Log.i(TAG, "dataAdapter group rows count = " + adapter.getGroupCount());
        elv.setAdapter(this.adapter);
    }

    private class MyExpandableListAdapter extends SimpleCursorTreeAdapter {

        public MyExpandableListAdapter(Cursor cursor, Context context, int groupLayout,
                                       int childLayout, String[] groupFrom, int[] groupTo, String[] childrenFrom,
                                       int[] childrenTo) {
            super(context, cursor, groupLayout, groupFrom, groupTo,
                    childLayout, childrenFrom, childrenTo);

        }

        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            Cursor childCursor = foodsWeightDatabaseHelper.fetchInfoByGroupId(groupCursor.getInt(groupCursor.getColumnIndex(FoodsWeightDBAdapter.KEY_ROWID)));
            //startManagingCursor(childCursor);
            return childCursor;
        }
    }

}
