package com.csc309.arspace;

import com.csc309.arspace.models.Product;
import java.util.ArrayList;

public class Search
{
    private ArrayList<Product> sampleDatabase;
    public Search()
    {
        String titles[] =  new String[]
            {"Magic Bullet Blender, Silver",
            "Ninja Professional 72oz Countertop Blender with 1000-Watt Base",
            "Breville BBL620 Fresh & Furious Blender, Silver",
            };

        String type = "Blender";

        double widths[] = new double[]{0, 8.19, 6.9};
        double heights[] = new double[]{0, 15.75, 17.2};
        double lengths[] = new double[]{0, 10.63, 8.05};

        sampleDatabase = new ArrayList<>();
        for(int i = 0; i < titles.length; i++)
        {
            sampleDatabase.add(new Product(titles[i], type,
                    widths[i], heights[i], lengths[i]));
        }
    }

    public Product getProduct(int i)
    {
        if(i < sampleDatabase.size())
        {return sampleDatabase.get(i);}
        return null;
    }
    public void addProduct(Product p)
    {
        sampleDatabase.add(p);
    }
    public ArrayList<Product> searchForAny(String[] keywords)
    {
        ArrayList<Product> results = new ArrayList<>();
        for(int i = 0; i < sampleDatabase.size(); i++)
        {
            boolean toAdd = false;
            Product p = getProduct(i);
            for(int j = 0; j < keywords.length; j++)
            {
                if((p.getType() + p.getTitle()).contains(keywords[j]))
                {
                    toAdd = true;
                    break;
                }
            }
            if(toAdd)
            {
                addProduct(p);
            }
        }
        return results;
    }

    public ArrayList<Product> searchForAll(String[] keywords)
    {
        ArrayList<Product> results = new ArrayList<>();
        for(int i = 0; i < sampleDatabase.size(); i++)
        {
            boolean toAdd = true;
            Product p = getProduct(i);
            for(int j = 0; j < keywords.length; j++)
            {
                if(!(p.getType() + p.getTitle()).contains(keywords[j]))
                {
                    toAdd = false;
                    break;
                }
            }
            if(toAdd)
            {
                addProduct(p);
            }
        }
        return results;
    }
}
