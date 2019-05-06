package com.csc309.arspace;

import org.junit.Test;

import java.util.ArrayList;
import com.csc309.arspace.models.Product;

import static org.junit.Assert.assertEquals;
public class SearchUnitTest {
    @Test
    public void searchAll_isCorrect()
    {
        Search testSearcher = new Search();
        String[] keywords = new String[] {"Magic", "Blender"};
        ArrayList<Product> expected =  new ArrayList<>();
        expected.add(new Product("Magic Bullet Blender, Silver", "Blender",
                0, 0, 0));
        assertEquals(expected, testSearcher.searchForAll(keywords));
    }

    @Test
    public void searchAny_isCorrect()
    {
        Search testSearcher = new Search();
        String[] keywords = new String[] {"Magic", "Elephant"};
        ArrayList<Product> expected =  new ArrayList<>();
        expected.add(new Product("Magic Bullet Blender, Silver", "Blender",
                0, 0, 0));
        assertEquals(expected, testSearcher.searchForAny(keywords));
    }
}