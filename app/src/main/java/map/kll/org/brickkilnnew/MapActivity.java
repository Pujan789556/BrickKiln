package map.kll.org.brickkilnnew;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.support.v7.internal.view.menu.MenuItemImpl;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.graphics.AndroidResourceBitmap;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import map.kll.org.brickkilnnew.library.BrickKiln;
import map.kll.org.brickkilnnew.library.JSONParse;
import map.kll.org.brickkilnnew.library.OnAsyncTaskComplete;
import map.kll.org.brickkilnnew.library.Utils;


public class MapActivity extends ActionBarActivity implements OnAsyncTaskComplete{
    private static final String MAPFILE = "kathmandu.map";
        public static MapView mapView;
    private TileCache tileCache;
    public static ArrayList<BrickKiln> brickKilnArrayList=null;
    private TileRendererLayer tileRendererLayer;
    static String name = null;
    static String city = null;
    static String ownership = null;
    static String market = null;
    static String operating_seasons = null;
    static  String days_open = null;
    static Double latitude = null;
    static Double longitude = null;
    static String raw_material=null;
    static String fuel=null;
    static String fuel_quantity=null;
    static String brick_kind=null;
    static String chimney_cat=null;
    static String chimney_height=null;
    static String chimney_number=null;
    static String moulding_process=null;
    static String firing=null;
    static String capacity=null;
    static String brick_per_batch=null;
    static String quality=null;


    static String labor_children=null;
    static String labor_male=null;
    static String labor_female=null;
    static String labor_total=null;
    static String labor_young=null;
    static String labor_old=null;
    static String labor_currently_studying=null;
    static String labor_slc=null;
    static String labor_informal_edu=null;
    static String labor_illiterate=null;
    static String food_allowance=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidGraphicFactory.createInstance(this.getApplication());
        this.mapView = new MapView(this);
        setContentView(this.mapView);

        this.mapView.setClickable(true);
        this.mapView.getMapScaleBar().setVisible(true);
        this.mapView.setBuiltInZoomControls(true);
        this.mapView.getMapZoomControls().setZoomLevelMin((byte) 10);
        this.mapView.getMapZoomControls().setZoomLevelMax((byte) 19);

        // create a tile cache of suitable size
        this.tileCache = AndroidUtil.createTileCache(this, "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f,
                this.mapView.getModel().frameBufferModel.getOverdrawFactor());
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mapView.getModel().mapViewPosition.setCenter(new LatLong(27.7000, 85.3333));
        this.mapView.getModel().mapViewPosition.setZoomLevel((byte) 10);
        // tile renderer layer using internal render theme
        this.tileRendererLayer = new TileRendererLayer(tileCache,
        this.mapView.getModel().mapViewPosition, false, true, AndroidGraphicFactory.INSTANCE);
        tileRendererLayer.setMapFile(getMapFile());
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
        // only once a layer is associated with a mapView the rendering starts
        this.mapView.getLayerManager().getLayers().add(tileRendererLayer);
        getKilnData();

    }
    public File getMapFile() {
        File f = new File(getCacheDir()+MAPFILE);
        if (!f.exists()) try {

            InputStream is = getAssets().open(MAPFILE);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();


            FileOutputStream fos = new FileOutputStream(f);
            fos.write(buffer);
            fos.close();
        } catch (Exception e) { throw new RuntimeException(e); }
        return f;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {

                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                onSearch(query);

                   return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_search:
                 return true;
            case R.id.action_about:
                Intent intent = new Intent(this,About.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        this.mapView.getLayerManager().getLayers().remove(this.tileRendererLayer);
        this.tileRendererLayer.onDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.tileCache.destroy();
        this.mapView.getModel().mapViewPosition.destroy();
        this.mapView.destroy();
        AndroidResourceBitmap.clearResourceBitmaps();
    }



    @Override
    public void onTaskComplete(ArrayList<BrickKiln> bk){
        this.brickKilnArrayList = bk;
        overLayMarker();
        }

    public void getKilnData(){
            JSONParse jsonParse = new JSONParse(this);
            jsonParse.execute();
    }
    public void overLayMarker(){
        int length = this.brickKilnArrayList.size();
        for (int i =0;i<length;i++) {
             name = brickKilnArrayList.get(i).name;
             city = brickKilnArrayList.get(i).city;
             longitude= brickKilnArrayList.get(i).longitude;
             latitude= brickKilnArrayList.get(i).latitude;
             ownership = brickKilnArrayList.get(i).ownership;
             market = brickKilnArrayList.get(i).market;
             operating_seasons = brickKilnArrayList.get(i).operating_seasons;
             days_open=  brickKilnArrayList.get(i).days_open;


             raw_material = brickKilnArrayList.get(i).raw_material;
             fuel = brickKilnArrayList.get(i).fuel;
            fuel_quantity = brickKilnArrayList.get(i).fuel_quantity;
            brick_kind = brickKilnArrayList.get(i).brick_kind;
            chimney_cat = brickKilnArrayList.get(i).chimney_cat;
            chimney_height = brickKilnArrayList.get(i).chimney_height;
            chimney_number = brickKilnArrayList.get(i).chimney_number;
            moulding_process = brickKilnArrayList.get(i).moulding_process;
            firing = brickKilnArrayList.get(i).firing;
            capacity = brickKilnArrayList.get(i).capacity;
            brick_per_batch = brickKilnArrayList.get(i).brick_per_batch;
            quality = brickKilnArrayList.get(i).quality;

            labor_children = brickKilnArrayList.get(i).labor_children;
            labor_male = brickKilnArrayList.get(i).labor_male;
            labor_female = brickKilnArrayList.get(i).labor_female;
            labor_total = brickKilnArrayList.get(i).labor_total;
            labor_young = brickKilnArrayList.get(i).labor_young;
            labor_old = brickKilnArrayList.get(i).labor_old;
            labor_currently_studying = brickKilnArrayList.get(i).labor_currently_studying;
            labor_slc = brickKilnArrayList.get(i).labor_slc;
            labor_informal_edu = brickKilnArrayList.get(i).labor_informal_edu;
            labor_illiterate = brickKilnArrayList.get(i).labor_illiterate;
            food_allowance = brickKilnArrayList.get(i).food_allowance;



            addOverlayLayers(mapView.getLayerManager().getLayers(),
                    latitude, longitude, name,city,ownership,market,operating_seasons,days_open,
                    raw_material,fuel,fuel_quantity,brick_kind,chimney_cat,chimney_height,chimney_number,moulding_process,firing,capacity,brick_per_batch,quality,
                    labor_children,labor_male,labor_female,labor_total,labor_young,labor_old,labor_currently_studying,labor_slc,labor_informal_edu,labor_illiterate,food_allowance);

        }
    }

    public void addOverlayLayers(Layers layers,double lat, double lon,String name,String city,String ownership,String market,
                                 String operating_seasons,String days_open, String raw_material,String fuel,String fuel_quantity,String brick_kind,String chimney_cat,String chimney_height,String chimney_number,
                                 String moulding_process,String firing,String capacity,String brick_per_batch,String quality,
                                 String labor_children,String labor_male,String labor_female,String labor_total,String labor_young,String labor_old,String labor_currently_studying,String labor_slc,String labor_informal_edu,String labor_illiterate,String food_allowance){


        LatLong latLong = new LatLong(lat, lon);
        Marker marker1 = Utils.createTappableMarker(this,
                R.drawable.marker_kiln, latLong, name, city, ownership, market, operating_seasons, days_open,
                raw_material, fuel, fuel_quantity, brick_kind, chimney_cat, chimney_height, chimney_number, moulding_process, firing, capacity, brick_per_batch, quality,
                labor_children, labor_male, labor_female, labor_total, labor_young, labor_old, labor_currently_studying, labor_slc, labor_informal_edu, labor_illiterate, food_allowance);
        layers.add(marker1);


    }
    public void onSearch(String nameKiln){
       int length = this.brickKilnArrayList.size();
        int i;

        for(i=0;i<length;i++){
            Double lat = this.brickKilnArrayList.get(i).latitude;
            Double lon = this.brickKilnArrayList.get(i).longitude;
            String name = this.brickKilnArrayList.get(i).name;
            nameKiln=nameKiln.toUpperCase();
            name=name.toUpperCase();
            if(nameKiln.equals(name)){
                this.mapView.getModel().mapViewPosition.setCenter(new LatLong(lat, lon));
                this.mapView.getModel().mapViewPosition.setZoomLevel((byte) 18);
                break;
            }

        }
        if(i==length){
            Toast.makeText(getApplicationContext(),
                    "No Match Found", Toast.LENGTH_LONG).show();

        }
            }
}
