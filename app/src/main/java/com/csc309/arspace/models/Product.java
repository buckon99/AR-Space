package com.csc309.arspace.models;

import android.graphics.Bitmap;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.support.constraint.motion.MotionScene.TAG;

public class Product {

    private Bitmap img;
    private String title;
    private String type;
    private double width;
    private double height;
    private double length;
    private String id;
    private String imgURL;
    private double price;
    private String info;
    private String productUrl;

    public Product(String id, String title, String type,
                   double width, double height, double length,
                   String imgURL, double price, String info, String productUrl) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.width = width;
        this.height = height;
        this.length = length;
        this.imgURL = imgURL;
        this.price = price;
        this.info = info;
        this.productUrl = productUrl;
    }

    public String getProductUrl() {
        return this.productUrl;
    }

    public void addBitmap(Bitmap img) {
        this.img = img;
    }

    public Bitmap getBitmap() {
        return img;
    }

    public String getId() {
        return id;
    }

    public String getImgURL() {
        return imgURL;
    }

    public double getHeight()
    {
        return height;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getInfo() {
        return info;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public boolean equals(Product other) {
        return this.title.equals(other.title)
                && this.type.equals(other.type)
                && this.height == other.height
                && this.width == other.width
                && this.length == other.length;
    }

    // adds product to user's database collection of products
    public void addProduct(String userSavedProductName) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        String currentUid = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.document(currentUid + "/" + userSavedProductName);

        Map<String, Object> product = new HashMap<>();
        product.put("id", this.id);
        product.put("title", this.title);
        product.put("type", this.type);
        product.put("width", this.width);
        product.put("height", this.height);
        product.put("length", this.length);
        product.put("imgURL", this.imgURL);
        product.put("price", this.price);
        product.put("info", this.info);
        product.put("productURL", this.productUrl);

        documentReference.set(product)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Success!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error!", e));
    }

    public static void loadProducts() {
        ArrayList<Product> products = new ArrayList<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String currentUid = null;
        if (currentUser != null) {
            currentUid = currentUser.getUid();
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        assert currentUid != null;
        db.collection(currentUid)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Map<String, Object> prodFields = document.getData();
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Product product = new Product(Objects.requireNonNull(prodFields.get("id")).toString(),
                                    Objects.requireNonNull(prodFields.get("title")).toString(),
                                    Objects.requireNonNull(prodFields.get("type")).toString(),
                                    (Double)prodFields.get("width"),
                                    (Double)prodFields.get("height"),
                                    (Double)prodFields.get("length"),
                                    Objects.requireNonNull(prodFields.get("imgURL")).toString(),
                                    (Double)prodFields.get("price"),
                                    Objects.requireNonNull(prodFields.get("info")).toString(),
                                    Objects.requireNonNull(prodFields.get("productURL")).toString());
                            products.add(product);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}
