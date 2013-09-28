package ru.fit4life.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorTreeAdapter;

public class FoodsWeightActivity extends MyActivity {

    private FoodsWeightDBAdapter foodsWeightDBAdapter;
    private MyExpandableListAdapter dataAdapter;
    private ExpandableListView expandableListView;
    private String selectedParentRowId;
    private String selectedParentName;
    private String selectedChildRowId;
    private String selectedChildName;
    private String selectedChildGlass;
    private String selectedChildSpoon;
    private String selectedChildPcs;
    private String[] menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods_weight);

        setTag(this.getClass().getSimpleName());
        foodsWeightDBAdapter = new FoodsWeightDBAdapter();
        menuItems = getResources().getStringArray(R.array.tables_context_menu);
        toolbarsManager = new ToolbarsManager(this);
        toolbarsManager.setNameActivity(this.getTitle().toString());

        //Set invisible and layout space free
        toolbarsManager.setFunctionalToolbarVisible(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();

        displayListView();
    }

    private void displayListView() {

        Cursor parentCursor = foodsWeightDBAdapter.fetchAllParents();
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

        expandableListView = (ExpandableListView) findViewById(R.id.foodsWeightGroupsList);

        this.dataAdapter = new MyExpandableListAdapter(
                parentCursor,
                this,
                R.layout.foods_weight_exp_list_row,
                R.layout.foods_weight_list_row,
                parentColumns,
                parentTo,
                childColumns,
                childTo
        );

        expandableListView.setAdapter(this.dataAdapter);
        //Нужно будет как-то сделать норм изменение элементов таблицы
        //registerForContextMenu(expandableListView);
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
            Cursor childCursor = foodsWeightDBAdapter.fetchChildesByParentId(groupCursor.getInt(groupCursor.getColumnIndex(FoodsWeightDBAdapter.KEY_ROWID)));
            //startManagingCursor(childCursor);
            return childCursor;
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int type =
                ExpandableListView.getPackedPositionType(info.packedPosition);

        int group =
                ExpandableListView.getPackedPositionGroup(info.packedPosition);

        int child =
                ExpandableListView.getPackedPositionChild(info.packedPosition);

        Cursor parentCursor = ((MyExpandableListAdapter) expandableListView.getExpandableListAdapter()).getCursor();
        parentCursor.moveToPosition(group);

        selectedParentRowId = parentCursor.getString(parentCursor.getColumnIndexOrThrow(FoodsWeightDBAdapter.KEY_ROWID));
        selectedParentName = parentCursor.getString(parentCursor.getColumnIndexOrThrow(FoodsWeightDBAdapter.KEY_NAME));


        // Create a context menu for child items
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            Cursor childCursor = ((MyExpandableListAdapter) expandableListView.getExpandableListAdapter()).getChildrenCursor(parentCursor);
            childCursor.moveToPosition(child);

            selectedChildRowId = parentCursor.getString(childCursor.getColumnIndexOrThrow(FoodsWeightDBAdapter.KEY_ROWID));
            selectedChildName = childCursor.getString(childCursor.getColumnIndexOrThrow(FoodsWeightDBAdapter.KEY_NAME));
            selectedChildGlass = childCursor.getString(childCursor.getColumnIndexOrThrow(FoodsWeightDBAdapter.KEY_GLASS));
            selectedChildSpoon = childCursor.getString(childCursor.getColumnIndexOrThrow(FoodsWeightDBAdapter.KEY_SPOON));
            selectedChildPcs = childCursor.getString(childCursor.getColumnIndexOrThrow(FoodsWeightDBAdapter.KEY_PCS));

            menu.setHeaderTitle("<<" + selectedParentName + ">>" + Character.toString((char) 10) + selectedChildName);

            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }

        if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {

            selectedChildRowId = "-1";
            Log.i(getTag(), "group = " + group + " child = " + child);
            menu.setHeaderTitle(selectedParentName);

            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int menuItemIndex = item.getItemId();

        String menuItemName = menuItems[menuItemIndex];
        Intent intent = new Intent(this, FoodsWeightNewItemActivity.class);

        //parent selected
        if (selectedChildRowId.equals("-1")) {
            if (menuItemName.equals("Delete")) {
                showAlert();
                return true;
            } else {
                intent.putExtra(FoodsWeightDBAdapter.KEY_ROWID, selectedParentRowId)
                        .putExtra(FoodsWeightDBAdapter.KEY_NAME, selectedParentName);

                if (menuItemName.equals("Add"))
                    intent.setAction("AddParent");
                else if (menuItemName.equals("Edit"))
                    intent.setAction("EditParent");
            }
        }
        //child selected
        else {
            if (menuItemName.equals("Delete")) {
                showAlert();
                return true;
            } else {
                intent.putExtra(FoodsWeightDBAdapter.KEY_ROWID, selectedChildRowId)
                        .putExtra(FoodsWeightDBAdapter.KEY_NAME, selectedChildName)
                        .putExtra(FoodsWeightDBAdapter.KEY_GLASS, selectedChildGlass)
                        .putExtra(FoodsWeightDBAdapter.KEY_SPOON, selectedChildSpoon)
                        .putExtra(FoodsWeightDBAdapter.KEY_PCS, selectedChildPcs)
                        .putExtra(FoodsWeightDBAdapter.KEY_GROUPID, selectedParentRowId);

                if (menuItemName.equals("Add"))
                    intent.setAction("AddChild");
                else if (menuItemName.equals("Edit"))
                    intent.setAction("EditChild");
            }
        }
        startActivity(intent);
        overridePendingTransition(R.anim.animation_in_left, R.anim.animation_out_left);
        return true;
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true)
                .setTitle("Really delete???")
                .setInverseBackgroundForced(true)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (selectedChildRowId.equals("-1"))
                                    foodsWeightDBAdapter.deleteParentRowById(selectedParentRowId);
                                else
                                    foodsWeightDBAdapter.deleteChildRowById(selectedChildRowId);
                                displayListView();
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void addNew(View view) {
        this.addNew();
    }

    private void addNew() {
        Intent intent = new Intent(this, GlycemicIndexNewItemActivity.class);
        intent.setAction(getString(R.string.atNew));
    }
}
