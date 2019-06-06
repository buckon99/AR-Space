package com.csc309.arspace.models;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
        return this.title.equals(other.title);
                /*&& this.type.equals(other.type)
                && this.height == other.height
                && this.width == other.width
                && this.length == other.length
                && this.imgURL.equals(other.imgURL);*/
    }

    // adds product to user's database collection of products
    public void addProduct(String userSavedProductName) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String currentUid = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.document(currentUid + "/" + userSavedProductName);

        Map<String, Object> product = new HashMap<>();
        product.put("id", this.id);
        product.put("title", this.title);
        product.put("width", this.width);
        product.put("height", this.height);
        product.put("length", this.length);
        product.put("imgURL", this.imgURL);

        documentReference.set(product)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Success!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error!", e));
    }

    public void loadProduct(String userSavedProductName) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String currentUid = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.document(currentUid + "/" + userSavedProductName);

        documentReference.get()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Success!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error!", e));
    }
}
