package com.csc309.arspace;

import org.junit.Test;

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
        assertTrue(true);
    }
}
