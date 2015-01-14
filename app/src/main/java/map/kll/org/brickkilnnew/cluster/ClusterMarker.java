/*
 * Copyright (C) 2009 Huan Erdao
 * Copyright (C) 2014 Martin Vennekamp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package map.kll.org.brickkilnnew.cluster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Rectangle;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.layer.Layer;

import map.kll.org.brickkilnnew.library.BrickKiln;
import map.kll.org.brickkilnnew.library.MarkerActivity;


/**
 * Layer extended class to display Clustered Marker.
 * @param <T>
 */
public class ClusterMarker<T extends GeoItem> extends Layer {
    public final static String TAG = ClusterMarker.class.getSimpleName();
    /**
     * cluster object
     */
    protected final Cluster<T> mCluster;
    /**
     * icon marker type
     */
    protected int mMarkerType = 0;
//    /**
//     * the rectangle spanning around the bitmap of this ClusterMarer on the screen
//     */
//    protected Rectangle mBitmapRectangle;

    /**
     * Whether this marker should react on Tap (implement a working onTap
     * Listener)
     */

    protected boolean ignoreOnTap;

    private Bitmap bubble;
    /**
     * @param cluster a cluster to be rendered for this marker
     */
    public ClusterMarker(Cluster<T> cluster, boolean ignoreOnTap ) {
        this.mCluster = cluster;
        this.ignoreOnTap = ignoreOnTap;
        setMarkerBitmap();
    }

    /**
     * change icon bitmaps according to the state and content size.
     */
    public synchronized void setMarkerBitmap() {
        for (mMarkerType = 0; mMarkerType < mCluster.getClusterManager().mMarkerIconBmps.size(); mMarkerType++) {
            // Check if the number of items in this cluster is below or equal the limit of the MarkerBitMap
            if (mCluster.getItems().size() <= mCluster.getClusterManager().mMarkerIconBmps.get(mMarkerType).getItemMax()) {
                return;
            }
        }
        // set the mMarkerType to maximum value ==> reduce mMarkerType by one.
        mMarkerType--;
    }

    @Override
    public synchronized void draw(BoundingBox boundingBox, byte zoomLevel
            , org.mapsforge.core.graphics.Canvas canvas, Point topLeftPoint) {
        Boolean isSelected = isSelected();
        if (!mCluster.getClusterManager().isClustering &&
                (this.getLatLong() == null ||
                        mCluster.getClusterManager().mMarkerIconBmps.get(mMarkerType).
                                getBitmap(isSelected) == null)) {
            return;
        }
        long mapSize = MercatorProjection.getMapSize(zoomLevel, this.displayModel.getTileSize());
        double pixelX = MercatorProjection.longitudeToPixelX(this.getLatLong().longitude, mapSize);
        double pixelY = MercatorProjection.latitudeToPixelY(this.getLatLong().latitude, mapSize);

        double halfBitmapWidth = mCluster.getClusterManager().mMarkerIconBmps.get(mMarkerType).getBitmap(isSelected).getWidth() / 2f;
        double halfBitmapHeight = mCluster.getClusterManager().mMarkerIconBmps.get(mMarkerType).getBitmap(isSelected).getHeight() / 2f;

        int left = (int)(pixelX - topLeftPoint.x - halfBitmapWidth
                + mCluster.getClusterManager().mMarkerIconBmps.get(mMarkerType).getIconOffset().x);
        int top = (int)(pixelY - topLeftPoint.y - halfBitmapHeight
                + mCluster.getClusterManager().mMarkerIconBmps.get(mMarkerType).getIconOffset().y);
        int right = (left
                + mCluster.getClusterManager().mMarkerIconBmps.get(mMarkerType).getBitmap(isSelected)
                .getWidth());
        int bottom = (top
                + mCluster.getClusterManager().mMarkerIconBmps.get(mMarkerType).getBitmap(isSelected)
                .getHeight());
        Rectangle mBitmapRectangle = new Rectangle(left, top, right, bottom);
        Rectangle canvasRectangle = new Rectangle(0, 0, canvas.getWidth(), canvas.getHeight());
        if (!canvasRectangle.intersects(mBitmapRectangle)) {
            return;
        }
        // Draw bitmap
        canvas.drawBitmap(mCluster.getClusterManager().mMarkerIconBmps
                .get(mMarkerType).getBitmap(isSelected), left, top);

        // Draw Text
        if (mMarkerType == 0 ) {
            // Draw bitmap
            bubble = MarkerBitmap.getBitmapFromTitle(mCluster.getTitle(),
                    mCluster.getClusterManager().mMarkerIconBmps
                            .get(mMarkerType).getPaint(),
                    this.mCluster.getClusterManager().mContext);
            canvas.drawBitmap(bubble,
                    (int)(left + halfBitmapWidth - bubble.getWidth() / 2),
                    (int)(top - bubble.getHeight() ));
        }
        else {
            int x = (int)(left + halfBitmapWidth);
            int y = (int)(top + halfBitmapHeight
                    + mCluster.getClusterManager().mMarkerIconBmps
                    .get(mMarkerType).getPaint().getTextHeight(mCluster.getTitle()) / 2);
            canvas.drawText(mCluster.getTitle(), x, y,
                    mCluster.getClusterManager().mMarkerIconBmps
                            .get(mMarkerType).getPaint());
        }

    }

    /**
     * get center location of the marker.
     *
     * @return GeoPoint object of current marker center.
     */
    public LatLong getLatLong() {
        return mCluster.getLocation();
    }

    /**
     * @return Gets the LatLong Position of the Layer Object
     */
    @Override
    public LatLong getPosition() {
        return  getLatLong();
    }

    public String getTitle(){
        return mCluster.getTitle();
    }

    public int getItemsSize(){
        return mCluster.getItems().size();
    }

    @Override
    public synchronized boolean onTap(LatLong geoPoint, Point viewPosition,
                                      Point tapPoint) {
        if ( ignoreOnTap ) return false;
        // Log.w(TAG, "onTap is called...");
        // only needed if clustered items should not be selected
        if (/* mCluster.getItems().size() == 1 && */ contains(viewPosition, tapPoint)) {
            Log.w(TAG, "The Marker was touched with onTap: "
                    + this.getPosition().toString());
            Log.w(TAG,"The Marker with title " + mCluster.getTitle() + " was touched with onTap");
            Log.w(TAG,"The Marker with items " + mCluster.getItems().size() + " was touched with onTap");
            if (mCluster.getItems().size() == 1) {
                Log.w(TAG,"The Marker is in " + mCluster.getBrickKiln().city);
                launchMarkerActivity(mCluster.getBrickKiln(), mCluster.getClusterManager().mContext);
            }else{
                Toast.makeText(mCluster.getClusterManager().mContext,"There are "+ mCluster.getItems().size() + " brickkilns in the cluster, Zoom in for details",Toast.LENGTH_SHORT ).show();
            }
            mCluster.getClusterManager().setSelectedItem(null, this);

            requestRedraw();
            return true;
        }
        return false;
    }

    public void launchMarkerActivity(BrickKiln brickKiln, Context context){
        Intent intent = new Intent(context,MarkerActivity.class);
        Bundle bundle = getBundleFromBricKiln(brickKiln);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public Bundle getBundleFromBricKiln(BrickKiln brickKiln){
        Bundle bundle1 = new Bundle();

        bundle1.putString("name",brickKiln.name);
        bundle1.putString("city",brickKiln.city);
        bundle1.putString("ownership",brickKiln.ownership);
        bundle1.putString("market",brickKiln.market);
        bundle1.putString("operating_seasons",brickKiln.operating_seasons);
        bundle1.putString("days_open",brickKiln.days_open);
        bundle1.putString("raw_material",brickKiln.raw_material);
        bundle1.putString("fuel",brickKiln.fuel);
        bundle1.putString("fuel_quantity",brickKiln.fuel_quantity);
        bundle1.putString("brick_kind",brickKiln.brick_kind);
        bundle1.putString("chimney_cat",brickKiln.chimney_cat);
        bundle1.putString("chimney_height",brickKiln.chimney_height);
        bundle1.putString("chimney_number",brickKiln.chimney_number);
        bundle1.putString("moulding_process",brickKiln.moulding_process);
        bundle1.putString("firing",brickKiln.firing);
        bundle1.putString("capacity",brickKiln.capacity);
        bundle1.putString("brick_per_batch",brickKiln.brick_per_batch);
        bundle1.putString("quality",brickKiln.quality);
        bundle1.putString("labor_children",brickKiln.labor_children);
        bundle1.putString("labor_male",brickKiln.labor_male);
        bundle1.putString("labor_female",brickKiln.labor_female);
        bundle1.putString("labor_total",brickKiln.labor_total);
        bundle1.putString("labor_young",brickKiln.labor_young);
        bundle1.putString("labor_old",brickKiln.labor_old);
        bundle1.putString("labor_currently_studying",brickKiln.labor_currently_studying);
        bundle1.putString("labor_slc",brickKiln.labor_slc);
        bundle1.putString("labor_informal_edu",brickKiln.labor_informal_edu);
        bundle1.putString("labor_illiterate",brickKiln.labor_illiterate);
        bundle1.putString("food_allowance",brickKiln.food_allowance);
       bundle1.putStringArrayList("image",brickKiln.image);
        Log.i("Before sending bundle",brickKiln.image.get(0));
        return bundle1;
    }

    public synchronized boolean contains(Point viewPosition, Point tapPoint) {
        return getBitmapRectangle(viewPosition).contains(tapPoint);
    }

    private Rectangle getBitmapRectangle(Point center){
        Boolean isSelected = isSelected();
        return new Rectangle(
                center.x
                        - (float) mCluster.getClusterManager().mMarkerIconBmps.get(mMarkerType)
                        .getBitmap(isSelected).getWidth() / 2
                        + mCluster.getClusterManager().mMarkerIconBmps.get(mMarkerType).getIconOffset().x,
                center.y
                        - (float) mCluster.getClusterManager().mMarkerIconBmps.get(mMarkerType)
                        .getBitmap(isSelected).getHeight() / 2
                        + mCluster.getClusterManager().mMarkerIconBmps.get(mMarkerType).getIconOffset().y,
                center.x
                        + (float) mCluster.getClusterManager().mMarkerIconBmps.get(mMarkerType)
                        .getBitmap(isSelected).getWidth() / 2
                        + mCluster.getClusterManager().mMarkerIconBmps.get(mMarkerType).getIconOffset().x,
                center.y
                        + (float) mCluster.getClusterManager().mMarkerIconBmps.get(mMarkerType)
                        .getBitmap(isSelected).getHeight() / 2
                        + mCluster.getClusterManager().mMarkerIconBmps.get(mMarkerType).getIconOffset().y);
    }
    public Boolean isSelected(){
        return (this == mCluster.getClusterManager().getSelectedItem());
    }
}
