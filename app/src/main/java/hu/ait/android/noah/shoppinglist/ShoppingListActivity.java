package hu.ait.android.noah.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.melnykov.fab.FloatingActionButton;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirections;

import java.text.NumberFormat;
import java.util.List;

import hu.ait.android.noah.shoppinglist.adapter.ShoppingItemsAdapter;
import hu.ait.android.noah.shoppinglist.data.ShoppingItem;


public class ShoppingListActivity extends ActionBarActivity {

    private static final int REQUEST_NEW_ITEM_CODE = 100;
    private static final int REQUEST_EDIT_ITEM_CODE = 101;

    private SwipeActionAdapter mAdapter;
    private Context context;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        List<ShoppingItem> shoppingItems = ShoppingItem.listAll(ShoppingItem.class);

        mAdapter = new SwipeActionAdapter(new ShoppingItemsAdapter(this, shoppingItems));

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setEmptyView(findViewById(android.R.id.empty));

        listView.setAdapter(mAdapter);

        //setListAdapter(mAdapter);
        mAdapter.setListView(listView);

        mAdapter.addBackground(SwipeDirections.DIRECTION_FAR_LEFT, R.layout.row_bg_left);
        mAdapter.addBackground(SwipeDirections.DIRECTION_NORMAL_LEFT, R.layout.row_bg_left);
        mAdapter.addBackground(SwipeDirections.DIRECTION_FAR_RIGHT, R.layout.row_bg_right);
        mAdapter.addBackground(SwipeDirections.DIRECTION_NORMAL_RIGHT, R.layout.row_bg_right);

        //mAdapter.setFadeOut(true);
        mAdapter.setFixedBackgrounds(true);

        tvTotal = (TextView) findViewById(R.id.tvTotal);
        updateTotal();


        // Listen to swipes
        mAdapter.setSwipeActionListener(new SwipeActionAdapter.SwipeActionListener() {
            @Override
            public boolean hasActions(int position) {
                // All items can be swiped
                return true;
            }

            @Override
            public boolean shouldDismiss(int position, int direction) {
                // Only dismiss an item when swiping normal left
                return direction == SwipeDirections.DIRECTION_FAR_RIGHT;
//                return false;
            }


            @Override
            public void onSwipe(int[] positionList, int[] directionList) {
                for (int i = 0; i < positionList.length; i++) {
                    int direction = directionList[i];
                    int position = positionList[i];


                    ShoppingItem item = (ShoppingItem) mAdapter.getAdapter().getItem(position);

                    switch (direction) {
                        case SwipeDirections.DIRECTION_FAR_LEFT:

                            Intent intentEditItem = new Intent();
                            intentEditItem.setClass(context, CreateShoppingItemActivity.class);
                            intentEditItem.putExtra(CreateShoppingItemActivity.KEY_ITEM_EDIT,
                                    item);
                            intentEditItem.putExtra(CreateShoppingItemActivity.KEY_EDIT_ID,
                                    position);

                            ((Activity) context).startActivityForResult(intentEditItem,
                                    REQUEST_EDIT_ITEM_CODE);
                            break;
                        case SwipeDirections.DIRECTION_NORMAL_LEFT:
                            break;
                        case SwipeDirections.DIRECTION_FAR_RIGHT:

                            item.delete();

                            ((ShoppingItemsAdapter) mAdapter.getAdapter()).removeItem(position);
                            mAdapter.getAdapter().notifyDataSetChanged();
                            Toast.makeText(context, "Item deleted!", Toast.LENGTH_LONG).show();

                            updateTotal();

                            break;
                        case SwipeDirections.DIRECTION_NORMAL_RIGHT:
                            break;
                    }
                }
            }
        });


        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreatePlaceActivity();
            }
        });
        fab.attachToListView(listView);

        Animation showAnim = AnimationUtils.loadAnimation(this, R.anim.fade_anim);
        fab.startAnimation(showAnim);
    }

    private void showCreatePlaceActivity() {
        Intent intentCreateItem = new Intent();
        intentCreateItem.setClass(getApplicationContext(), CreateShoppingItemActivity.class);
        startActivityForResult(intentCreateItem, REQUEST_NEW_ITEM_CODE);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_clear:
                ShoppingItem.deleteAll(ShoppingItem.class);
                ((ShoppingItemsAdapter)mAdapter.getAdapter()).removeAll();
                mAdapter.getAdapter().notifyDataSetChanged();

                Toast.makeText(this, "All items deleted!", Toast.LENGTH_LONG).show();

                updateTotal();

                return true;
            case R.id.action_help:
                Toast.makeText(this,
                        "Swipe right to delete\n" +
                        "Swipe left to edit\n" +
                        "Tap circle to mark as purchased", Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_about:
                Toast.makeText(this, "Created by Noah Mulfinger", Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_sort:
                ((ShoppingItemsAdapter)mAdapter.getAdapter()).sort();
                mAdapter.getAdapter().notifyDataSetChanged();
                Toast.makeText(this, "Items sorted", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == REQUEST_NEW_ITEM_CODE) {

                    ShoppingItem item = (ShoppingItem) data.getSerializableExtra(
                            CreateShoppingItemActivity.KEY_ITEM);
                    // when I call the save() function
                    // it will generate the id for the place
                    item.save();

                    ((ShoppingItemsAdapter) mAdapter.getAdapter()).addItem(item);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Item added!", Toast.LENGTH_LONG).show();

                   updateTotal();

                } else if (requestCode == REQUEST_EDIT_ITEM_CODE) {
                    int index = data.getIntExtra(CreateShoppingItemActivity.KEY_EDIT_ID, -1);
                    if (index != -1) {
                        ShoppingItem item = (ShoppingItem) data.getSerializableExtra(
                                CreateShoppingItemActivity.KEY_ITEM);

                        item.setId(((ShoppingItem) mAdapter.getAdapter().getItem(index)).getId());

                        item.save();

                        ((ShoppingItemsAdapter) mAdapter.getAdapter()).updateItem(
                                index,
                                (ShoppingItem) data.getSerializableExtra(
                                        CreateShoppingItemActivity.KEY_ITEM));
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "Item updated!", Toast.LENGTH_LONG).show();

                        updateTotal();
                    }
                }
                break;
            case RESULT_CANCELED:
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void updateTotal() {
        tvTotal.setText("Total Cost: $" +
                NumberFormat.getInstance().format(
                        ((ShoppingItemsAdapter) mAdapter.getAdapter()).getTotalCost()));
    }
}