package hu.ait.android.noah.shoppinglist.data;

import android.support.annotation.NonNull;

import com.orm.SugarRecord;
import java.io.Serializable;
import hu.ait.android.noah.shoppinglist.R;

public class ShoppingItem extends SugarRecord<ShoppingItem>
        implements Serializable, Comparable<ShoppingItem> {



    public enum ItemType {
        FOOD(0, R.drawable.food),
        ELECTRONIC(1, R.drawable.electronic),
        BOOK(2, R.drawable.book),
        CLOTHING(3, R.drawable.clothing);

        private final int value;
        private final int iconId;

        private ItemType(int value, int iconId) {
            this.value = value;
            this.iconId = iconId;
        }

        public static ItemType fromInt(int value) {
            for (ItemType i : ItemType.values()) {
                if (i.value == value) {
                    return i;
                }
            }
            return FOOD;
        }

        public int getIconId() {
            return iconId;
        }

        public int getValue() {
            return value;
        }

    }


    private ItemType itemType;
    private String itemName;
    private String itemDesc;
    private int itemPrice;
    private boolean isPurchased;


    public ShoppingItem() {

    }


    public ShoppingItem(ItemType itemType, String itemName,
                        String itemDesc, int itemPrice, boolean isPurchased) {
        this.itemType = itemType;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemPrice = itemPrice;
        this.isPurchased = isPurchased;
    }

    @Override
    public int compareTo(@NonNull ShoppingItem another) {
        int x = this.getItemType().getValue();
        int y = another.getItemType().getValue();

        //Sort alphabetically if type is the same
        if (x == y) {
            return this.getItemName().compareTo(another.getItemName());
        }
        return x - y;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean isPurchased) {
        this.isPurchased = isPurchased;
    }


}
