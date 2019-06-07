package com.csc309.arspace;

import com.csc309.arspace.models.Product;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class SearchSingleUnitTest {
    @Test
    public void searchSingle()
    {
        String[] keywords = new String[] {"sofa"};
        Search.searchProduct(keywords);
        assertTrue(true);
    }

    @Test
    public void searchMassSingle()
    {
        String[] keywords = new String[] {"sofa"};
        Search.searchProduct(keywords);
        keywords = new String[] {"lamp"};
        Search.searchProduct(keywords);
        keywords = new String[] {"desk"};
        Search.searchProduct(keywords);
        keywords = new String[] {"bed"};
        Search.searchProduct(keywords);
        keywords = new String[] {"blender"};
        Search.searchProduct(keywords);
        keywords = new String[] {"table"};
        Search.searchProduct(keywords);
        keywords = new String[] {"bedframe"};
        Search.searchProduct(keywords);
        keywords = new String[] {"nightstand"};
        Search.searchProduct(keywords);
        keywords = new String[] {"clock"};
        Search.searchProduct(keywords);
        keywords = new String[] {"chair"};
        Search.searchProduct(keywords);
        assertTrue(true);
    }

    @Test
    public void searchInvalid()
    {
        String[] keywords = new String[] {"digital"};
        ArrayList<Product> actual = Search.searchProduct(keywords);
        assertTrue(actual != null && actual.isEmpty());
    }

    @Test
    public void searchEmpty()
    {
        String[] keywords = new String[] {"asdnoajdoiajdojw"};
        ArrayList<Product> actual = Search.searchProduct(keywords);
        assertTrue(actual != null && actual.isEmpty());
    }

}
