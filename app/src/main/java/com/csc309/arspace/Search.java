package com.csc309.arspace;
import com.csc309.arspace.models.Product;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
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
        String baseURL = "https://search.unbxd.io/ac97f4afb1f7404167b9611f771ea548/prod-ikea-com800881532940772/search?&q=";
        String urlTail = "&rows=40&view=grid&start=0&format=json&variants=true&variants.count=10&variants.fields=v_imageUrl%2Cv_isNewLowerPrice%2Cv_goodToKnow%2Cv_parent_unbxd%2Cv_productMeasurements%2Cv_normalPrice%2Cv_externalImageNormal%2Cv_title%2Cv_brand%2Cv_short_description%2Cv_uniqueId%2Cv_productUrl%2Cv_sku%2Cv_price%2Cv_isNew%2Cv_averageRating%2Cv_ratingCount%2Cv_isBreathtakingItem%2Cv_isBuyable&stats=price&fields=imageUrl,isNewLowerPrice,goodToKnow,parent_unbxd,productMeasurements,normalPrice,externalImageNormal,title,brand,short_description,uniqueId,productUrl,sku,price,isNew,averageRating,ratingCount,isBreathtakingItem,isBuyable&facet.multiselect=true&indent=off&device-type=Mobile&unbxd-url=https%3A%2F%2Fwww.ikea.com%2Fms%2Fen_US%2Fusearch%2F%3F";
        String searchURL = baseURL + keywords[0];
        for(int i = 1; i < keywords.length; i++)
        {
            searchURL = searchURL + "%20" + keywords[i];
        }
        searchURL += urlTail;
        System.out.println(searchURL);
        try
        {
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(getJSON(searchURL)).getAsJsonObject();
            System.out.println(getJSON(searchURL));
            //JSONObject obj = new JSONObject(getJSON(searchURL));
            JsonArray productsJArray = obj.getAsJsonObject("response").getAsJsonArray("products");
            for(int i = 0; i < productsJArray.size(); i++)
            {
                JsonObject ele = productsJArray.get(i).getAsJsonObject();
                String productURL = ele.get("productUrl").getAsString();
                double price = ele.get("price").getAsFloat();
                String imgURL = ele.getAsJsonArray("imageUrl").get(0).getAsString();
                String title = ele.get("title").getAsString();
                String info = ele.get("goodToKnow").getAsString();
                JsonObject measurements = ele.getAsJsonObject("productMeasurements");
                // todo: parse height, width, and length
                // parseMeasurements(measurements);
            }
        }
        catch(Exception e)
        {
            System.err.println("Error in failed search connection:");
            System.err.println("    " + e.getMessage());
        }
        return results;
    }

    // todo: implement
    private static ArrayList<Double> parseMeasurements(JsonObject measurements)
    {
        ArrayList<Double> dimensions = new ArrayList<>();

        return dimensions;
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
