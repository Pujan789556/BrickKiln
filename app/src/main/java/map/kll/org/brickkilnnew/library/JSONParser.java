package map.kll.org.brickkilnnew.library;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JSONParser {

    static JSONArray jArr= null;
    static String json = "";
    // constructor

    public JSONArray getJSON(Context c) {

        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(c.getAssets().open(
                    "BrickKiln.json")));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        json = sb.toString();
        // try parse the string to a JSON object
        try {
            jArr = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jArr;
    }

    }







