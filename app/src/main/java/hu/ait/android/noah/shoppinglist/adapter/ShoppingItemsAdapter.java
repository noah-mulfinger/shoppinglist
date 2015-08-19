package hu.ait.android.noah.shoppinglist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

import hu.ait.android.noah.shoppinglist.data.ShoppingItem;


import hu.ait.android.noah.shoppinglist.R;

public class ShoppingItemsAdapter extends BaseAdapter {

    private final List<ShoppingItem> shoppingItems;
    private final Context context;
    private final int bgUncheckedColor;
    private final int bgCheckedColor;


    public ShoppingItemsAdapter(Context context, List<ShoppingItem> shoppingItems) {
        this.context = context;
        this.shoppingItems = shoppingItems;

        bgUncheckedColor = context.getResources().getColor(R.color.item_background);
        bgCheckedColor = context.getResources().getColor(R.color.item_background_checked);
    }

    @Override
    public int getCount() {
        return shoppingItems.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(ShoppingItem item) {
        shoppingItems.add(item);
    }

    public void updateItem(int position, ShoppingItem item) {
        shoppingItems.remove(position);
        shoppingItems.add(position, item);
    }

    public int getTotalCost() {
        int total = 0;
        for (ShoppingItem item : shoppingItems) {
            total += item.getItemPrice();
        }
        return total;
    }


    public void sort() {
        //Sorts by type first, alphabetically second
        Collections.sort(shoppingItems);
    }

    public void removeAll() {
        shoppingItems.removeAll(shoppingItems);
    }

    public void removeItem(int position) {
        shoppingItems.remove(position);
    }


    static class ViewHolder {
        ImageView ivIcon;
        TextView tvItem;
        TextView tvPrice;
        CheckBox cbPurchased;
        TextView tvDesc;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.row_place, null);
            ViewHolder holder = new ViewHolder();
            holder.ivIcon = (ImageView) v.findViewById(R.id.ivIcon);
            holder.tvItem = (TextView) v.findViewById(R.id.tvItem);
            holder.tvPrice = (TextView) v.findViewById(R.id.tvPrice);
            holder.cbPurchased = (CheckBox) v.findViewById(R.id.cbPurchased);
            holder.tvDesc = (TextView) v.findViewById(R.id.tvDesc);
            v.setTag(holder);
        }

        final ShoppingItem item = shoppingItems.get(position);
        if (item != null) {
            final ViewHolder holder = (ViewHolder) v.getTag();
            holder.ivIcon.setImageResource(item.getItemType().getIconId());
            holder.tvItem.setText(item.getItemName());
            holder.tvPrice.setText("$"+
                    NumberFormat.getInstance().format(item.getItemPrice()));
            holder.cbPurchased.setChecked(item.isPurchased());
            holder.tvDesc.setText(item.getItemDesc());

            v.setBackgroundColor(item.isPurchased() ? bgCheckedColor : bgUncheckedColor);

            holder.cbPurchased.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    item.setPurchased(!item.isPurchased());
                    item.save();
                    notifyDataSetChanged();
                }
            });
        }

        return v;
    }
}
