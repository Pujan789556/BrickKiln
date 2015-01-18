package map.kll.org.brickkilnnew.library;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class JSONParse extends AsyncTask<ArrayList<BrickKiln>, String, ArrayList<BrickKiln>> {

    private Activity activity;
    private Context context;
    private OnAsyncTaskComplete callback;
    private ProgressDialog pDialog;
    JSONArray geoLocation = null;
    JSONArray pictures = null;
    //string varialbles for general information of kiln
    static String name = null;
    static String city = null;
    static Double latitude = null;
    static Double longitude = null;
    static String ownership = null;
    static String market = null;
    static String operating_seasons = null;
    static String days_open = null;

    //variables for technical detail
    static String raw_material = null;
    static String fuel = null;
    static String fuel_quantity = null;
    static String brick_kind = null;
    static String chimney_cat = null;
    static String chimney_height = null;
    static String chimney_number = null;
    static String moulding_process = null;
    static String firing = null;
    static String capacity = null;
    static String brick_per_batch = null;
    static String quality = null;

    //varialbles for socio-economic details
    static String labor_children = null;
    static String labor_male = null;
    static String labor_female = null;
    static String labor_total = null;
    static String labor_young = null;
    static String labor_old = null;
    static String labor_currently_studying = null;
    static String labor_slc = null;
    static String labor_informal_edu = null;
    static String labor_illiterate = null;
    static String food_allowance = null;


    //array list of BrickKiln types
    ArrayList<BrickKiln> brickKilns = new ArrayList<BrickKiln>();


    public JSONParse(Context c) {
        this.context = c;
        Activity a = (Activity) c;
        this.activity = a;
        this.callback = (OnAsyncTaskComplete) a;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Getting Data ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }
    @Override
    protected ArrayList<BrickKiln> doInBackground(ArrayList<BrickKiln>... args) {
        JSONParser jsonParser = new JSONParser();
        JSONArray json = jsonParser.getJSON(activity);
        int length = json.length();

        int i = 0;
        do {
            ArrayList<String> image = new ArrayList<String>();
            try {
                JSONObject json_obj = json.getJSONObject(i);
                geoLocation = json_obj.getJSONArray("_geolocation");
                   if (json_obj.has("pictures")) {
                    pictures = json_obj.getJSONArray("pictures");

               for(int j = 0;j<pictures.length();j++){
                     JSONObject img_json_obj = (JSONObject) pictures.getJSONObject(j);
                        Log.i("Index",Integer.toString(j));
                        String imgStr = img_json_obj.getString("pictures/photo");
                        Log.i("Image String",imgStr);
                        image.add(imgStr);


                }
                       for (int k = 0;k<image.size();k++) {
                           Log.i("Image", image.get(k));
                       }
                   }

                //gettong general information from JSON
                name = json_obj.getString("name");
                Log.i("Name",name);
                if (json_obj.has("city")) {
                    city = json_obj.getString("city");
                } else {
                    city = "N/A";
                }
                if (json_obj.has("ownership")) {
                    ownership = json_obj.getString("ownership");
                } else {
                    ownership = "N/A";
                }
                if (json_obj.has("market")) {
                    market = json_obj.getString("market");
                } else {
                    market = "N/A";
                }
                if (json_obj.has("operating_season")) {
                    operating_seasons = json_obj.getString("operating_season");
                } else {
                    operating_seasons = "N/A";
                }
                if (json_obj.has("days_open")) {
                    days_open = json_obj.getString("days_open");
                } else {
                    days_open = "N/A";
                }


                //getting technical detail from json
                if (json_obj.has("raw_material")) {
                    raw_material = json_obj.getString("raw_material");
                } else {
                    raw_material = "N/A";
                }
                if (json_obj.has("fuel")) {
                    fuel = json_obj.getString("fuel");
                } else {
                    fuel = "N/A";
                }
                if (json_obj.has("fuel_quantity")) {
                    fuel_quantity = json_obj.getString("fuel_quantity");
                } else {
                    fuel_quantity = "N/A";
                }
                if (json_obj.has("brick_kind")) {
                    brick_kind = json_obj.getString("brick_kind");
                } else {
                    brick_kind = "N/A";
                }
                if (json_obj.has("chimney_category")) {
                    chimney_cat = json_obj.getString("chimney_category");
                } else {
                    chimney_cat = "N/A";
                }
                if (json_obj.has("chimney_height")) {
                    chimney_height = json_obj.getString("chimney_height");
                } else {
                    chimney_height = "N/A";
                }
                if (json_obj.has("chimney_numbers")) {
                    chimney_number = json_obj.getString("chimney_numbers");
                } else {
                    chimney_number = "N/A";
                }
                if (json_obj.has("moulding_process")) {
                    moulding_process = json_obj.getString("moulding_process");
                } else {
                    moulding_process = "N/A";
                }
                if (json_obj.has("firing")) {
                    firing = json_obj.getString("firing");
                } else {
                    firing = "N/A";
                }
                if (json_obj.has("capacity")) {
                    capacity = json_obj.getString("capacity");
                } else {
                    capacity = "N/A";
                }
                if (json_obj.has("brick_production")) {
                    brick_per_batch = json_obj.getString("brick_production");
                } else {
                    brick_per_batch = "N/A";
                }
                if (json_obj.has("brick_quality")) {
                    quality = json_obj.getString("brick_quality");
                } else {
                    quality = "N/A";
                }



                //getting socio-economic detail from json
                if (json_obj.has("labor_children")) {
                    labor_children = json_obj.getString("labor_children");
                } else {
                    labor_children = "N/A";
                }

                if (json_obj.has("labor_male")) {
                    labor_male = json_obj.getString("labor_male");
                } else {
                    labor_male = "N/A";
                }
                if (json_obj.has("labor_female")) {
                    labor_female = json_obj.getString("labor_female");
                } else {
                    labor_female = "N/A";
                }
                if (json_obj.has("labor_total")) {
                    labor_total = json_obj.getString("labor_total");
                } else {
                    labor_total = "N/A";
                }
                if (json_obj.has("labor_young")) {
                    labor_young = json_obj.getString("labor_young");
                } else {
                    labor_young = "N/A";
                }

                if (json_obj.has("labor_old")) {
                    labor_old = json_obj.getString("labor_old");
                } else {
                    labor_old = "N/A";
                }
                if (json_obj.has("labor_currently_studing")) {
                    labor_currently_studying = json_obj.getString("labor_currently_studing");
                } else {
                    labor_currently_studying = "N/A";
                }
                if (json_obj.has("labor_slc")) {
                    labor_slc = json_obj.getString("labor_slc");
                } else {
                    labor_slc = "N/A";
                }
                if (json_obj.has("labor_informal_edu")) {
                    labor_informal_edu = json_obj.getString("labor_informal_edu");
                } else {
                    labor_informal_edu = "N/A";
                }
                if (json_obj.has("labor_illiterate")) {
                    labor_illiterate = json_obj.getString("labor_illiterate");
                } else {
                    labor_illiterate = "N/A";
                }
                if (json_obj.has("food_allowance")) {
                    food_allowance = json_obj.getString("food_allowance");
                } else {
                    food_allowance = "N/A";
                }

                //adding in arraylist
                if (geoLocation.getString(0) != "null" || geoLocation.getString(1) != "null") {
                    latitude = geoLocation.getDouble(0);
                    longitude = geoLocation.getDouble(1);
                    BrickKiln brickKiln = new BrickKiln(name, latitude, longitude, city, ownership, market, operating_seasons, days_open,
                            raw_material, fuel, fuel_quantity, brick_kind, chimney_cat, chimney_height, chimney_number, moulding_process, firing, capacity, brick_per_batch, quality,
                            labor_children, labor_male, labor_female, labor_total, labor_young, labor_old, labor_currently_studying, labor_slc, labor_informal_edu, labor_illiterate, food_allowance,
                            image);
                    brickKilns.add(brickKiln);
                    i++;
                } else {
                    BrickKiln brickKiln = new BrickKiln(name, 0.0, 0.0, city, ownership, market, operating_seasons, days_open,
                            raw_material, fuel, fuel_quantity, brick_kind, chimney_cat, chimney_height, chimney_number, moulding_process, firing, capacity, brick_per_batch, quality,
                            labor_children, labor_male, labor_female, labor_total, labor_young, labor_old, labor_currently_studying, labor_slc, labor_informal_edu, labor_illiterate, food_allowance,
                            image);
                    brickKilns.add(brickKiln);
                    i++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } while (i != length);
        return brickKilns;
    }


    @Override
    protected void onPostExecute(ArrayList<BrickKiln> bk) {
        pDialog.dismiss();
        callback.onTaskComplete(bk);
    }
}

