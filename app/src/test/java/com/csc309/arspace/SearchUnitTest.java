package com.csc309.arspace;

import org.junit.Test;

import java.util.ArrayList;
import com.csc309.arspace.models.Product;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SearchUnitTest {
    @Test
    public void searchAll_isCorrect()
    {
        Search testSearcher = new Search();
        String[] keywords = new String[] {"Magic", "Blender"};
        ArrayList<Product> expected =  new ArrayList<>();
        expected.add(new Product("Magic Bullet Blender, Silver", "",
                "", 0, 0, 0, ""));
        assertTrue(expected.get(0).equals(testSearcher.searchForAny(keywords).get(0)));
    }

    @Test
    public void searchAny_isCorrect()
    {
        Search testSearcher = new Search();
        String[] keywords = new String[] {"Magic", "Elephant"};
        ArrayList<Product> expected =  new ArrayList<>();
        expected.add(new Product("Magic Bullet Blender, Silver", "",
                "", 0, 0, 0, ""));
        assertTrue(expected.get(0).equals(testSearcher.searchForAny(keywords).get(0)));
    }
}