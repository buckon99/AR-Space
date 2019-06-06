package com.csc309.arspace;

import com.csc309.arspace.models.Product;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class SearchUnitTest {
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
    public void searchMassMultiple()
    {
        String[] keywords = new String[] {"silver",  "storage"};
        Search.searchProduct(keywords);
        keywords = new String[] {"new", "couch"};
        Search.searchProduct(keywords);
        keywords = new String[] {"magic", "blender"};
        Search.searchProduct(keywords);
        keywords = new String[] {"small", "shelf"};
        Search.searchProduct(keywords);
        keywords = new String[] {"large", "carpet"};
        Search.searchProduct(keywords);
        keywords = new String[] {"coffee", "table"};
        Search.searchProduct(keywords);
        assertTrue(true);
    }

    @Test
    public void searchInvalid1()
    {
        String[] keywords = new String[] {"flottebo",  "book"};
        ArrayList<Product> actual = Search.searchProduct(keywords);
        assertTrue(actual != null && actual.isEmpty());
    }

    @Test
    public void searchInvalid2()
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

    @Test
    public void searchEmptyMultiple()
    {
        String[] keywords = new String[] {"asdnoajdoiajdojw", "wowowowowow"};
        ArrayList<Product> actual = Search.searchProduct(keywords);
        assertTrue(actual != null && actual.isEmpty());
    }
}
