package hu.ait.android.noah.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import hu.ait.android.noah.shoppinglist.data.ShoppingItem;


public class CreateShoppingItemActivity extends Activity {

    public static final String KEY_ITEM_EDIT = "KEY_ITEM_EDIT";
    public static final String KEY_EDIT_ID = "KEY_EDIT_ID";
    public static final String KEY_ITEM = "KEY_ITEM";



    private Spinner spinnerItemType;
    private EditText etItemName;
    private EditText etItemDesc;
    private EditText etItemPrice;
    private CheckBox cbAlreadyPurchased;
    private ShoppingItem itemToEdit = null;
    private Context context;
    private int itemToEditId = 0;
    private boolean inEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shopping_item);

        context = this;

        //Modify the size of the dialog box
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        this.getWindow().setAttributes(params);


        spinnerItemType = (Spinner) findViewById(R.id.spinnerItemType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.item_types_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItemType.setAdapter(adapter);

        etItemName = (EditText) findViewById(R.id.etItemName);
        etItemDesc = (EditText) findViewById(R.id.etItemDesc);
        etItemPrice = (EditText) findViewById(R.id.etItemPrice);
        cbAlreadyPurchased = (CheckBox) findViewById(R.id.cbAlreadyPurchased);

        if (getIntent().getExtras() != null &&
                getIntent().getExtras().containsKey(KEY_ITEM_EDIT)) {
            inEditMode = true;

            itemToEdit = (ShoppingItem) getIntent().getSerializableExtra(KEY_ITEM_EDIT);
            itemToEditId = getIntent().getIntExtra(KEY_EDIT_ID, -1);

            spinnerItemType.setSelection(itemToEdit.getItemType().getValue());
            etItemName.setText(itemToEdit.getItemName());
            etItemDesc.setText(itemToEdit.getItemDesc());
            etItemPrice.setText(String.valueOf(itemToEdit.getItemPrice()));
            cbAlreadyPurchased.setChecked(itemToEdit.isPurchased());
        }


        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etItemName.getText().toString().equals("") ||
                        etItemDesc.getText().toString().equals("") ||
                        etItemPrice.getText().toString().equals("")) {
                    Toast.makeText(context, "Please fill all fields",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (inEditMode) {
                    updateItem();
                } else {
                    saveItem();
                }
            }
        });
    }

    private void updateItem() {
        itemToEdit.setItemName(etItemName.getText().toString());
        itemToEdit.setItemDesc(etItemDesc.getText().toString());
        itemToEdit.setItemType(ShoppingItem.ItemType.fromInt(
                spinnerItemType.getSelectedItemPosition()));
        itemToEdit.setItemPrice(Integer.parseInt(etItemPrice.getText().toString()));
        itemToEdit.setPurchased(cbAlreadyPurchased.isChecked());

        Intent intentResult = new Intent();
        intentResult.putExtra(KEY_ITEM,itemToEdit);
        intentResult.putExtra(KEY_EDIT_ID,itemToEditId);
        setResult(RESULT_OK,intentResult);
        finish();
    }

    private void saveItem() {
        Intent intentResult = new Intent();
        intentResult.putExtra(KEY_ITEM,
                new ShoppingItem(ShoppingItem.ItemType.fromInt(
                        spinnerItemType.getSelectedItemPosition()),
                        etItemName.getText().toString(),
                        etItemDesc.getText().toString(),
                        Integer.parseInt(etItemPrice.getText().toString()),
                        cbAlreadyPurchased.isChecked()));
        setResult(RESULT_OK,intentResult);
        finish();
    }
}
