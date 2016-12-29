package com.esiea.geoffroy_thibault.beerforlife;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thibaultgeoffroy on 26/12/2016.
 */

public class Beer {
    String categoryId = "";
    String countryId ="";
    String created_at="";
    String description = "";
    Integer id ;
    String name = "";
    String note = "";


    Beer() {
    }

    public static Beer createFromJson(JSONObject json) throws JSONException {
        Beer b = new Beer();
        b.categoryId = json.getString("category_id");
        b.countryId = json.getString("country_id");
        b.created_at = json.getString("created_at");
        b.description = json.getString("description");
        b.id = json.getInt("id");
        b.name = json.getString("name");
        b.note = json.getString("note");
        return b;
    }
}
