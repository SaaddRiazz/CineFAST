package com.example.cinefast;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;

public class JSONParser {
    public static ArrayList<Movie> getMoviesFromJson(Context context, String filterType) {
        ArrayList<Movie> movieList = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("movies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String type = obj.getString("type");

                if (type.equals(filterType)) {
                    movieList.add(new Movie(
                            obj.getString("title"),
                            obj.getString("info"),
                            obj.getString("posterName"),
                            obj.getString("trailerLink"),
                            type
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieList;
    }
}
