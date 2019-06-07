package com.csc309.arspace.dummy;

import com.csc309.arspace.models.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ProductsContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Product> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Product> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        //addItem(new Product("1", "Sample Product (1x1x1)", "example", 0.3048, 0.3048, 0.3048, null, 0, ""));
        //addItem(new Product("2", "Sample Product (1x2x1)", "example", 0.3048, 0.3048 * 2, 0.3048, null, 0, ""));
        //addItem(new Product("3", "Sample Product (1x2x2)", "example", 0.3048, 0.3048 * 2, 0.3048 * 2, null, 0, ""));
    }
    public static void addItem(Product item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getTitle(), item);
    }

    public static void removeAll() {
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    public static void addAll(ArrayList<Product> items) {
        for(Product prod: items) {
            addItem(prod);
        }

    }


}
