package com.csc309.arspace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.csc309.arspace.models.Product;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


//A NOTE:
/*
   Android.jar methods are not mocked when running unit tests, so we can' test the actual parsing of images as that is Android - based
   This program instead makes a connection to each image and makes sure it is valid
 */
public class LoopTests {

    private static ArrayList<Product> fakeData(int n) {
        ArrayList<Product> products = new ArrayList<Product>();
        for(int i = 0; i < n; i++) {
            //https://picsum.photos/200 returns a random photo each time
            products.add(new Product(i + "", "", "", 0,
                    0, 0, "https://picsum.photos/200", 0, "", ""));
        }
        return products;
    }

    @Test
    public void testNoProductRetrieveLoop() {
        ArrayList<Product> products = fakeData(0);
        int count = 0;
        try {
            for (int i = 0; i < products.size(); i++) {
                Product prod = products.get(i);
                URL url = new URL(prod.getImgURL());
                InputStream s = url.openConnection().getInputStream();
                if(s != null) {
                    count++;
                }
            }
        }catch (Exception e){

        }
        assertEquals(0, count);
    }
    @Test
    public void test1ProductRetrieveLoop() {
        ArrayList<Product> products = fakeData(1);
        int count = 0;
        try {
            for (int i = 0; i < products.size(); i++) {
                Product prod = products.get(i);
                URL url = new URL(prod.getImgURL());
                InputStream s = url.openConnection().getInputStream();
                if(s != null) {
                    count++;
                }
            }
        }catch (Exception e){

        }
        assertEquals(0, count);
    }
    @Test
    public void test10ProductsRetrieveLoop() {
        ArrayList<Product> products = fakeData(10);
        int count = 0;
        try {
            for (int i = 0; i < products.size(); i++) {
                Product prod = products.get(i);
                URL url = new URL(prod.getImgURL());
                InputStream s = url.openConnection().getInputStream();
                if(s != null) {
                    count++;
                }
            }
        }catch (Exception e){

        }
        assertEquals(10, count);
    }
    @Test
    public void test100ProductsRetrieveLoop() {
        ArrayList<Product> products = fakeData(100);
        int count = 0;
        try {
            for (int i = 0; i < products.size(); i++) {
                Product prod = products.get(i);
                URL url = new URL(prod.getImgURL());
                InputStream s = url.openConnection().getInputStream();
                if(s != null) {
                    count++;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        assertEquals(100, count);
    }

}
