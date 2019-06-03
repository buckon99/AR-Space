package com.csc309.arspace;

import com.csc309.arspace.models.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Search
{
    private ArrayList<Product> sampleDatabase;
    public Search()
    {

        //below is deprecated
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
                    random.nextDouble() * 20.0, "", 0));
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
    public ArrayList<Product> searchProduct(String[] keywords)
    {
        if(keywords.length == 0)
        {
            return null;
        }
        String baseURL = "https://www.ikea.com/ms/en_US/usearch/?query=";
        String searchURL = baseURL + keywords[0];
        Document doc;
        for(int i = 1; i < keywords.length; i++)
        {
            searchURL += "%20" + keywords[i];
        }
        try
        {
            doc = Jsoup.connect(searchURL).get();
            //Element link = doc.select("a").first();
            //System.out.println(searchURL);
            //https://search.unbxd.io/ac97f4afb1f7404167b9611f771ea548/prod-ikea-com800881532940772/search?&q=table&rows=40&view=grid&start=0&format=json&variants=true&variants.count=10&variants.fields=v_imageUrl%2Cv_isNewLowerPrice%2Cv_goodToKnow%2Cv_parent_unbxd%2Cv_productMeasurements%2Cv_normalPrice%2Cv_externalImageNormal%2Cv_title%2Cv_brand%2Cv_short_description%2Cv_uniqueId%2Cv_productUrl%2Cv_sku%2Cv_price%2Cv_isNew%2Cv_averageRating%2Cv_ratingCount%2Cv_isBreathtakingItem%2Cv_isBuyable&stats=price&fields=imageUrl,isNewLowerPrice,goodToKnow,parent_unbxd,productMeasurements,normalPrice,externalImageNormal,title,brand,short_description,uniqueId,productUrl,sku,price,isNew,averageRating,ratingCount,isBreathtakingItem,isBuyable&facet.multiselect=true&indent=off&device-type=Mobile&unbxd-url=https%3A%2F%2Fwww.ikea.com%2Fms%2Fen_US%2Fusearch%2F%3Fquery%3DMagic%2520Blender&unbxd-referrer=&user-type=repeat&api-key=ac97f4afb1f7404167b9611f771ea548&uid=uid-1559583884458-33622
            System.out.println(doc.body().getElementById("main"));

        }
        catch(IOException e)
        {
            System.err.println("Error: searchURL Construction Failed.");
        }

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
}
