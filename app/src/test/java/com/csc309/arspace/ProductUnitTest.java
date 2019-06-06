package com.csc309.arspace;

import android.util.Log;
import com.csc309.arspace.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import org.junit.Test;

import static android.support.constraint.motion.MotionScene.TAG;
import static org.junit.Assert.assertTrue;

public class ProductUnitTest {
    Product newProduct = new Product("sampleProductID",
            "Magic Bullet Blender, Silver",
            "Kitchen Appliance",
            5.337090298211931,
            16.916736700318364,
            1.8325901746647544,
            "imageurl.com",
            50.99,
            "This is a blender.");

    @Test
    public void testAddProduct() {
        Login login = new Login();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DocumentReference documentReference;

        firebaseAuth.signInWithEmailAndPassword("ayoo97@gmail.com", "Password1")
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Success!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error!", e));
        newProduct.addProduct("MyFirstProduct");
        documentReference = db.document(firebaseAuth.getCurrentUser().getUid()
                + "/" + "MyFirstProduct");
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    Product expectedProduct = documentSnapshot.toObject(Product.class);
                    assertTrue(newProduct.equals(expectedProduct));
                });
    }
}
