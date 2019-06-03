package com.csc309.arspace;

import com.csc309.arspace.models.Product;
import java.util.ArrayList;
import java.util.Random;

public class Search
{
    private ArrayList<Product> sampleDatabase;
    public Search()
    {
        String[] titles =
            {
                    "Magic Bullet Blender, Silver",
                    "Ninja Professional 72oz Countertop Blender with 1000-Watt Base",
                    "Breville BBL620 Fresh & Furious Blender, Silver",
                    "Audio-Technica ATH-M40x Professional Studio"
            };

        String[] types =
                {
                        "Motors",
                        "Fashion",
                        "Collectibles & Art",
                        "Home & Garden",
                        "Sporting Goods",
                        "Toys",
                        "Business & Industrial",
                        "Music"
                };
        Random random = new Random();

        sampleDatabase = new ArrayList<>();
        for(String title : titles)
        {
            sampleDatabase.add(new Product("0", title,
                    types[random.nextInt(types.length)],
                    random.nextDouble() * 20.0,
                    random.nextDouble() * 20.0,
                    random.nextDouble() * 20.0, ""));
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
        for(Product p : sampleDatabase)
        {
            boolean toAdd = false;
            for(String keyword : keywords)
            {
                if((p.getType() + p.getTitle()).contains(keyword))
                {
                    toAdd = true;
                    break;
                }
            }
            if(toAdd)
            {
                results.add(p);
            }
        }
        return results;
    }

    public ArrayList<Product> searchForAll(String[] keywords)
    {
        ArrayList<Product> results = new ArrayList<>();
        for(Product p : sampleDatabase)
        {
            boolean toAdd = true;
            for(String keyword : keywords)
            {
                if(!(p.getType() + p.getTitle()).contains(keyword))
                {
                    toAdd = false;
                    break;
                }
            }
            if(toAdd)
            {
                results.add(p);
            }
        }
        return results;
    }
}
