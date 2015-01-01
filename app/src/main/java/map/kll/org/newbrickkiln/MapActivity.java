package map.kll.org.newbrickkiln;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
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


import map.kll.org.newbrickkiln.library.BrickKiln;

import map.kll.org.newbrickkiln.library.JSONParse;

import map.kll.org.newbrickkiln.library.OnAsyncTaskComplete;
import map.kll.org.newbrickkiln.library.Utils;



public  class MapActivity extends Activity implements OnAsyncTaskComplete{
    private static final String MAPFILE = "kathmandu.map";


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

    public static ArrayList<BrickKiln> brickKilns = null;

    private MapView mapView;
    private TileCache tileCache;
    private TileRendererLayer tileRendererLayer;




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
    protected void onStart() {
        super.onStart();
        this.mapView.getModel().mapViewPosition.setCenter(new LatLong(27.7000, 85.3333));
        this.mapView.getModel().mapViewPosition.setZoomLevel((byte) 10);

        // tile renderer layer using internal render theme
        this.tileRendererLayer = new TileRendererLayer(tileCache,
                this.mapView.getModel().mapViewPosition, false, false, AndroidGraphicFactory.INSTANCE);

        tileRendererLayer.setMapFile(getMapFile());
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);

        // only once a layer is associated with a mapView the rendering starts
        this.mapView.getLayerManager().getLayers().add(tileRendererLayer);

        JSONParse jsonParse = new JSONParse(this);
        jsonParse.execute();


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
    public void onTaskComplete(ArrayList<BrickKiln> bk){

      overLay(bk);
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


    public void addOverlayLayers(Layers layers,double lat, double lon,String name,String city,String ownership,String market,
                                 String operating_seasons,String days_open, String raw_material,String fuel,String fuel_quantity,String brick_kind,String chimney_cat,String chimney_height,String chimney_number,
                                 String moulding_process,String firing,String capacity,String brick_per_batch,String quality,
                                 String labor_children,String labor_male,String labor_female,String labor_total,String labor_young,String labor_old,String labor_currently_studying,String labor_slc,String labor_informal_edu,String labor_illiterate,String food_allowance){


        LatLong latLong = new LatLong(lat, lon);
        Marker marker1 = Utils.createTappableMarker(this,
                R.drawable.marker_kiln, latLong, name,city,ownership,market,operating_seasons,days_open,
                raw_material,fuel,fuel_quantity,brick_kind,chimney_cat,chimney_height,chimney_number,moulding_process,firing,capacity,brick_per_batch,quality,
                labor_children,labor_male,labor_female,labor_total,labor_young,labor_old,labor_currently_studying,labor_slc,labor_informal_edu,labor_illiterate,food_allowance);
        layers.add(marker1);


    }

    public void overLay(ArrayList<BrickKiln> brickKilns) {
        int length = brickKilns.size();
         for (int i =0;i<length;i++) {
            name = brickKilns.get(i).name;
            city = brickKilns.get(i).city;
            longitude= brickKilns.get(i).longitude;
            latitude= brickKilns.get(i).latitude;
            ownership = brickKilns.get(i).ownership;
            market = brickKilns.get(i).market;
            operating_seasons = brickKilns.get(i).operating_seasons;
            days_open=  brickKilns.get(i).days_open;


            raw_material = brickKilns.get(i).raw_material;
            fuel = brickKilns.get(i).fuel;
            fuel_quantity = brickKilns.get(i).fuel_quantity;
            brick_kind = brickKilns.get(i).brick_kind;
            chimney_cat = brickKilns.get(i).chimney_cat;
             chimney_height = brickKilns.get(i).chimney_height;
            chimney_number = brickKilns.get(i).chimney_number;
            moulding_process = brickKilns.get(i).moulding_process;
             firing = brickKilns.get(i).firing;
             capacity = brickKilns.get(i).capacity;
             brick_per_batch = brickKilns.get(i).brick_per_batch;
             quality = brickKilns.get(i).quality;

             labor_children = brickKilns.get(i).labor_children;
             labor_male = brickKilns.get(i).labor_male;
             labor_female = brickKilns.get(i).labor_female;
             labor_total = brickKilns.get(i).labor_total;
             labor_young = brickKilns.get(i).labor_young;
             labor_old = brickKilns.get(i).labor_old;
             labor_currently_studying = brickKilns.get(i).labor_currently_studying;
             labor_slc = brickKilns.get(i).labor_slc;
             labor_informal_edu = brickKilns.get(i).labor_informal_edu;
             labor_illiterate = brickKilns.get(i).labor_illiterate;
             food_allowance = brickKilns.get(i).food_allowance;



                addOverlayLayers(mapView.getLayerManager().getLayers(),
                        latitude, longitude, name,city,ownership,market,operating_seasons,days_open,
                        raw_material,fuel,fuel_quantity,brick_kind,chimney_cat,chimney_height,chimney_number,moulding_process,firing,capacity,brick_per_batch,quality,
                        labor_children,labor_male,labor_female,labor_total,labor_young,labor_old,labor_currently_studying,labor_slc,labor_informal_edu,labor_illiterate,food_allowance);

            }

    }



}


