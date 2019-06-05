package com.csc309.arspace;
import com.csc309.arspace.models.Product;
import com.google.gson.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class Search
{
    public static ArrayList<Product> searchProduct(String[] keywords)
    {
        ArrayList<Product> results = new ArrayList<>();

        if(keywords.length == 0)
        {
            return null;
        }

        // construct search url
        String baseURL = "https://search.unbxd.io/ac97f4afb1f7404167b9611f771ea548/prod-ikea-com800881532940772/search?&q=";
        String urlTail = "&rows=40&view=grid&start=0&format=json&variants=true&variants.count=10&variants.fields=v_imageUrl%2Cv_isNewLowerPrice%2Cv_goodToKnow%2Cv_parent_unbxd%2Cv_productMeasurements%2Cv_normalPrice%2Cv_externalImageNormal%2Cv_title%2Cv_brand%2Cv_short_description%2Cv_uniqueId%2Cv_productUrl%2Cv_sku%2Cv_price%2Cv_isNew%2Cv_averageRating%2Cv_ratingCount%2Cv_isBreathtakingItem%2Cv_isBuyable&stats=price&fields=imageUrl,isNewLowerPrice,goodToKnow,parent_unbxd,productMeasurements,normalPrice,externalImageNormal,title,brand,short_description,uniqueId,productUrl,sku,price,isNew,averageRating,ratingCount,isBreathtakingItem,isBuyable&facet.multiselect=true&indent=off&device-type=Mobile&unbxd-url=https%3A%2F%2Fwww.ikea.com%2Fms%2Fen_US%2Fusearch%2F%3F";
        String searchURL = baseURL + keywords[0];
        for(int i = 1; i < keywords.length; i++)
        {
            searchURL = searchURL + "%20" + keywords[i];
        }
        searchURL += urlTail;
        //System.out.println(searchURL);

        // load json object from url
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(getJSON(searchURL)).getAsJsonObject();
        if(obj == null)
        {
            return null;
        }

        // extract info from json object
        JsonArray productsJArray = obj.getAsJsonObject("response").getAsJsonArray("products");
        for(int i = 0; i < productsJArray.size(); i++)
        {
            JsonObject ele = productsJArray.get(i).getAsJsonObject();

            if(ele.has("productMeasurements"))
            {
                String productURL = ele.get("productUrl").getAsString();
                double price = ele.get("price").getAsFloat();
                String imgURL = ele.getAsJsonArray("imageUrl").get(0).getAsString();
                String title = ele.get("title").getAsString();
                String type = ele.get("short_description").getAsString();

                String measurements = ele.getAsJsonPrimitive("productMeasurements").getAsString();
                //System.out.println(productURL);
                ArrayList<Double> dimensions = parseMeasurements(measurements);
                if(dimensions != null)
                {
                    results.add(new Product("0", title, type, dimensions.get(0),
                            dimensions.get(1), dimensions.get(2), imgURL, price));
                }
            }

        }
        return results;
    }
    
    private static ArrayList<Double> parseMeasurements(String measurements)
    {
        ArrayList<Double> dimensions = new ArrayList<>();
        ArrayList<Double> extra = new ArrayList<>();
        String[] elements = measurements.split("\"");
        if(elements.length < 3)
        {
            return null;
        }
        boolean lengthFound = false;
        boolean widthFound = false;
        boolean heightFound = false;
        double width = 0;
        double height = 0;
        double length = 0;
        for(int i = 0; i < elements.length - 1; i++)
        {
            //System.out.println("---" + elements[i]);
            String[] subElements;
            if(elements[i].contains("</dEn>"))
            {
                //System.out.println("split: " + elements[i].split("</dEn>")[1]);
                elements[i] = elements[i].split("</dEn>")[1];
                subElements = elements[i].split(" ");
            }
            else
            {
                subElements = elements[i].split(" ");
            }

            if(elements[i].contains("Depth") || elements[i].contains("depth"))
            {
                extra.add(getDouble(subElements));
            }
            else if(elements[i].contains("Thickness") || elements[i].contains("thickness"))
            {
                extra.add(getDouble(subElements));
            }
            else if(elements[i].contains("Height") || elements[i].contains("height"))
            {
                heightFound = true;
                height = getDouble(subElements);
            }
            else if(elements[i].contains("Width") || elements[i].contains("width"))
            {
                widthFound = true;
                width = getDouble(subElements);
            }
            else if(elements[i].contains("Length") || elements[i].contains("length"))
            {
                lengthFound = true;
                length = getDouble(subElements);
            }
        }

        int count = 0;
        if(widthFound)
        {
            dimensions.add(width);
        }
        else
        {
            if(count < extra.size())
            {
                dimensions.add(extra.get(count++));
            }
            else
            {
                return null;
            }
        }
        if(heightFound)
        {
            dimensions.add(height);
        }

        else
        {
            if(count < extra.size())
            {
                dimensions.add(extra.get(count++));
            }
            else
            {
                return null;
            }
        }
        if(lengthFound) {
            dimensions.add(length);
        }
        else
        {
            if(count < extra.size())
            {
                dimensions.add(extra.get(count));
            }
            else
            {
                return null;
            }

        }
        if(dimensions.contains(0.0))
        {
            return null;
        }
        return dimensions;
    }

    private static double getDouble(String[] subElements)
    {
        int offset = 0;
        double decimal = 0;
        if(subElements[subElements.length - 1].contains("/"))
        {
            offset = 1;
            String[] mixedFraction = subElements[subElements.length - 1].split("/");
            decimal = Double.parseDouble(mixedFraction[0]) / Double.parseDouble(mixedFraction[1]);
        }
        try {
            return Double.parseDouble(subElements[subElements.length - 1 - offset]) + decimal;
        }
        catch(java.lang.NumberFormatException e)
        {
            return 0.0;
        }
    }

    private static String getJSON(String url) {
        HttpsURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();

            con.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }
}
