package map.kll.org.brickkilnnew;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.gesture.Gesture;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v7.widget.SearchView;
import android.view.MotionEvent;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.mapsforge.core.graphics.Align;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.FontFamily;
import org.mapsforge.core.graphics.FontStyle;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.graphics.AndroidResourceBitmap;
import org.mapsforge.map.android.rendertheme.AssetsRenderTheme;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderThemeMenuCallback;
import org.mapsforge.map.rendertheme.XmlRenderThemeStyleLayer;
import org.mapsforge.map.rendertheme.XmlRenderThemeStyleMenu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.zip.Inflater;

import map.kll.org.brickkilnnew.cluster.ClusterManager;
import map.kll.org.brickkilnnew.cluster.GeoItem;
import map.kll.org.brickkilnnew.cluster.MarkerBitmap;
import map.kll.org.brickkilnnew.library.BrickKiln;
import map.kll.org.brickkilnnew.library.JSONParse;
import map.kll.org.brickkilnnew.library.OnAsyncTaskComplete;


public class MapActivity extends ActionBarActivity implements OnAsyncTaskComplete, XmlRenderThemeMenuCallback {
    private static final String MAPFILE = "kathmandu.map";
    public MapView mapView;


    private ArrayList<MyGeoItem> myGeoItems = new ArrayList<>();
    protected ClusterManager clusterer = null;

    protected XmlRenderThemeStyleMenu renderThemeStyleMenu;
    protected SharedPreferences sharedPreferences;

    private TileCache tileCache;
    public static ArrayList<BrickKiln> brickKilnArrayList=null;
    private TileRendererLayer tileRendererLayer;
    static String name = null;
    static Double latitude = null;
    static Double longitude = null;


   // private SimpleCursorAdapter searchAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidGraphicFactory.createInstance(this.getApplication());
        this.mapView = new MapView(this);
        setContentView(this.mapView);
        this.mapView.setClickable(true);

        this.mapView.getMapScaleBar().setVisible(true);
       // this.mapView.setBuiltInZoomControls(true);
        this.mapView.getMapZoomControls().setShowMapZoomControls(true);
        this.mapView.getMapZoomControls().setZoomLevelMin((byte) 10);
        this.mapView.getMapZoomControls().setZoomLevelMax((byte) 19);

        // create a tile cache of suitable size
        this.tileCache = AndroidUtil.createTileCache(this, "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f,
                this.mapView.getModel().frameBufferModel.getOverdrawFactor());

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


    }

    @Override
    public void onStart() {
        super.onStart();
        this.mapView.getModel().mapViewPosition.setCenter(new LatLong(27.7000, 85.3333));
        this.mapView.getModel().mapViewPosition.setZoomLevel((byte) 10);
        // tile renderer layer using internal render theme
        this.tileRendererLayer = new TileRendererLayer(tileCache,
        this.mapView.getModel().mapViewPosition,false,true, AndroidGraphicFactory.INSTANCE);
        tileRendererLayer.setMapFile(getMapFile());
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
        //tileRendererLayer.setXmlRenderTheme(getRenderTheme());
        // only once a layer is associated with a mapView the rendering starts
        this.mapView.getLayerManager().getLayers().add(tileRendererLayer);


        getKilnData();

    }




    protected XmlRenderTheme getRenderTheme() {
        //TODO use this method to allow switching overlays
        try {
            return new AssetsRenderTheme(this, getRenderThemePrefix(), getRenderThemeFile(), this);
        } catch (IOException e) {
            Log.e("IO EXCEPTION", "Render theme failure " + e.toString());
        }
        return null;
    }

    protected String getRenderThemePrefix() {
        return "";
    }

    @Override
    public Set<String> getCategories(XmlRenderThemeStyleMenu menuStyle) {
        this.renderThemeStyleMenu = menuStyle;
        String id = this.sharedPreferences.getString(this.renderThemeStyleMenu.getId(),
                this.renderThemeStyleMenu.getDefaultValue());

        XmlRenderThemeStyleLayer baseLayer = this.renderThemeStyleMenu.getLayer(id);
        if (baseLayer == null) {
            return null;
        }
        Set<String> result = baseLayer.getCategories();

        // add the categories from overlays that are enabled
        for (XmlRenderThemeStyleLayer overlay : baseLayer.getOverlays()) {
            if (this.sharedPreferences.getBoolean(overlay.getId(), overlay.isEnabled())) {
                result.addAll(overlay.getCategories());
            }
        }

        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayCluster();

    }

    public void displayCluster(){
        // create clusterer instance
        clusterer = new ClusterManager(this,
                mapView,
                getMarkerBitmap(this),
                getZoomLevelMax(),
                false);
        // this uses the framebuffer position, the mapview position can be out of sync with
        // what the user sees on the screen if an animation is in progress
        this.mapView.getModel().frameBufferModel.addObserver(clusterer);
        // add geoitems for clustering


        for (int i = 0; i < myGeoItems.size(); i++) {
            clusterer.addItem(myGeoItems.get(i));
        }
        clusterer.redraw();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (clusterer != null) {
            clusterer.destroyGeoClusterer();
            this.mapView.getModel().frameBufferModel.removeObserver(clusterer);
            clusterer = null;
        }
    }

    /**
     * @return the maximum zoom level of the map view.
     */
    protected byte getZoomLevelMax() {
        return (byte) 24;
    }

    protected String getRenderThemeFile() {
        return "renderthemes/rendertheme-v4.xml";
    }

    private static ArrayList<MarkerBitmap> getMarkerBitmap(Context context) {
        ArrayList<MarkerBitmap> markerBitmaps = new ArrayList<>();
        // prepare for marker icons.
        Drawable balloon;
        // small icon for maximum single item
        balloon = context.getResources().getDrawable(R.drawable.marker_green);
        Bitmap bitmap_climbing_peak = AndroidGraphicFactory.convertToBitmap(balloon);
        bitmap_climbing_peak.incrementRefCount();
        balloon = context.getResources().getDrawable(R.drawable.marker_red);
        Bitmap marker_red_s = AndroidGraphicFactory.convertToBitmap(balloon);
        marker_red_s.incrementRefCount();
        Paint paint_1;
        paint_1 = AndroidGraphicFactory.INSTANCE.createPaint();
        paint_1.setStyle(Style.STROKE);
        paint_1.setTextAlign(Align.CENTER);
        FontFamily fontFamily = FontFamily.DEFAULT;
        FontStyle fontStyle = FontStyle.BOLD;
        paint_1.setTypeface(fontFamily, fontStyle);
        paint_1.setColor(Color.RED);
        markerBitmaps.add(new MarkerBitmap(context, bitmap_climbing_peak, marker_red_s,
                new Point(0, 0), 10f, 1, paint_1));

        // TODO: check what happens with map rotation.
        // small icon. for 10 or less items.
        balloon = context.getResources().getDrawable(R.drawable.balloon_s_n);
        Bitmap bitmap_balloon_s_n = AndroidGraphicFactory
                .convertToBitmap(balloon);
        bitmap_balloon_s_n.incrementRefCount();
        balloon = context.getResources().getDrawable(R.drawable.balloon_s_s);
        Bitmap bitmap_balloon_s_s = AndroidGraphicFactory
                .convertToBitmap(balloon);
        bitmap_balloon_s_s.incrementRefCount();
        Paint paint_2;
        paint_2 = AndroidGraphicFactory.INSTANCE.createPaint();
        paint_2.setStyle(Style.FILL);
        paint_2.setTextAlign(Align.CENTER);
        fontFamily = FontFamily.DEFAULT;
        fontStyle = FontStyle.BOLD;
        paint_2.setTypeface(fontFamily, fontStyle);
        paint_2.setColor(Color.BLACK);
        markerBitmaps.add(new MarkerBitmap(context, bitmap_balloon_s_n,
                bitmap_balloon_s_s, new Point(0, 0), 9f, 10,paint_2));

        // large icon. 100 will be ignored.
        balloon = context.getResources().getDrawable(R.drawable.balloon_m_n);
        Bitmap bitmap_balloon_m_n = AndroidGraphicFactory
                .convertToBitmap(balloon);
        bitmap_balloon_m_n.incrementRefCount();
        balloon = context.getResources().getDrawable(R.drawable.balloon_m_s);
        Bitmap bitmap_balloon_m_s = AndroidGraphicFactory
                .convertToBitmap(balloon);
        bitmap_balloon_m_s.incrementRefCount();
        Paint paint_3;
        paint_3 = AndroidGraphicFactory.INSTANCE.createPaint();
        paint_3.setStyle(Style.FILL);
        paint_3.setTextAlign(Align.CENTER);
        fontFamily = FontFamily.DEFAULT;
        fontStyle = FontStyle.BOLD;
        paint_3.setTypeface(fontFamily, fontStyle);
        paint_3.setColor(Color.BLACK);
        markerBitmaps.add(new MarkerBitmap(context, bitmap_balloon_m_n,
                bitmap_balloon_m_s, new Point(0, 0), 18f, 100,paint_3));
        return markerBitmaps;
    }


    protected class MyGeoItem implements GeoItem {
        public String title;
        public LatLong latLong;
        public BrickKiln brickKiln;

        public MyGeoItem(String title, LatLong latLong, BrickKiln brickKiln) {
            this.title = title;
            this.latLong = latLong;
            this.brickKiln = brickKiln;
        }
        public LatLong getLatLong(){
            return latLong;
        }
        public String getTitle(){
            return String.valueOf(this.title);
        }
        public BrickKiln getBrickKiln(){return brickKiln;}
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

       /* searchAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                null,
                null,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);*/
        // Associate searchable configuration with the SearchView

        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryRefinementEnabled(true);
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> suggestList=onSearchSuggestion(newText);
                String[] column = new String[]{"_id","name"};
                Object[] temp = new Object[]{0,"default"};
                final MatrixCursor cursor = new MatrixCursor(column);
                Log.i("Size",Integer.toString(suggestList.size()));
                for(int i = 0; i < suggestList.size(); i++) {
                    temp[0] = i;
                    temp[1] = suggestList.get(i);
                    cursor.addRow(temp);

                }


                final SuggestionAdapter suggestionAdapter = new SuggestionAdapter(getBaseContext(), cursor,suggestList);
                      searchView.setSuggestionsAdapter(suggestionAdapter);
                  searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {

                    @Override
                    public boolean onSuggestionClick(int position) {

                        Cursor cursor1 = (Cursor) searchView.getSuggestionsAdapter().getItem(
                                position);
                        String query = cursor1.getString(cursor1.getColumnIndex("name"));

                        Log.i("Query",query);
                        Log.i("Position", Integer.toString(position));
                        onSearchByName(query);

                        searchView.setIconified(false);
                        return false;

                    }

                    @Override
                    public boolean onSuggestionSelect(int position) {

                        return false;
                    }
                });
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                onSearchByName(query);
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
                 return false;
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
        myGeoItems.clear();
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
             longitude= brickKilnArrayList.get(i).longitude;
             latitude= brickKilnArrayList.get(i).latitude;
             addOverlayLayers(name,latitude,longitude,brickKilnArrayList.get(i));

        }
        displayCluster();

    }

    public void addOverlayLayers(String name, double lat, double lon, BrickKiln brickKiln){
        LatLong latLong = new LatLong(lat, lon);
        //TODO add geoitem to the cluster
        myGeoItems.add(new MyGeoItem(name,latLong,brickKiln));


    }
    public ArrayList<String> onSearchSuggestion(String text){

        int length = this.brickKilnArrayList.size();
        ArrayList<String> nameArray = new ArrayList<String>();


        int i;

        for (i=0;i<length;i++){
            String name=this.brickKilnArrayList.get(i).name;

            if((text.length()!=0)&&(text.length()>1)&& (name.toUpperCase().contains(text.toUpperCase())==true)){

                    nameArray.add(name);


            }
        }

        return nameArray;

    }





    public void onSearchByName(String nameKiln){
       int length = this.brickKilnArrayList.size();
        int i;

        for(i=0;i<length;i++){
            Double lat = this.brickKilnArrayList.get(i).latitude;
            Double lon = this.brickKilnArrayList.get(i).longitude;
            String name = this.brickKilnArrayList.get(i).name;
            nameKiln=nameKiln.toUpperCase();
            name=name.toUpperCase();

            if(nameKiln.equals(name)){
                Log.i("Matched",nameKiln);
                Log.i("Lat",Double.toString(lat));
                Log.i("Long",Double.toString(lon));
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
