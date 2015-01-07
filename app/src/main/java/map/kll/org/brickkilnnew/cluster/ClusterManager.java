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

import android.content.Context;
import android.os.AsyncTask;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.model.common.Observer;
import org.mapsforge.map.util.MapViewProjection;
import org.mapsforge.map.view.MapView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for Clustering geotagged content. this clustering came from
 * "markerclusterer" which is available as opensource at
 * https://code.google.com/p/android-maps-extensions/, resp
 * https://github.com/googlemaps/android-maps-utils this is android ported
 * version with modification to fit to the application refer also to other
 * implementations:
 * https://code.google.com/p/osmbonuspack/source/browse/#svn%2Ftrunk
 * %2FOSMBonusPack%2Fsrc%2Forg%2Fosmdroid%2Fbonuspack%2Fclustering
 * http://developer.nokia.com/community/wiki/
 * Map_Marker_Clustering_Strategies_for_the_Maps_API_for_Java_ME
 *
 * based on http://ge.tt/7Zq63CH/v/1
 *
 * Should be added as Observer on Mapsforge frameBufferModel.
 */
public class ClusterManager<T extends GeoItem> implements Observer, SelectionHandler<T> {
    private static String TAG = ClusterManager.class.getSimpleName();
    /**
     * Context for the access to some resources.
     */
    protected Context mContext;
    /**
     * grid size for Clustering(dip).
     */
    protected final float GRIDSIZE = 28 * DisplayModel.getDeviceScaleFactor();

    /**
     * MarkerBitmap object for marker icons, uses Static access.
     */
    protected ArrayList<MarkerBitmap> mMarkerIconBmps = new ArrayList<MarkerBitmap>();
    /**
     * MapView object.
     */
    protected final MapView mMapView;

    /**
     * The maximum (highest) zoom level for clustering the items.,
     */
    protected final byte mMaxClusteringZoom;

    public MapView getMapView() {
        return mMapView;
    }

    /**
     * GeoItem ArrayList object that are out of viewport and need to be
     * clustered on panning.
     */
    protected List<T> mLeftItems = Collections
            .synchronizedList(new ArrayList<T>());
    /**
     * Clustered object list.
     */
    protected List<Cluster<T>> mClusters = new ArrayList<Cluster<T>>();

    /**
     * saves the actual ZoolLevel of the MapView
     */
    private double oldZoolLevel;
    /**
     * saves the actual Center as LatLong of the MapViewPosition
     */
    private LatLong oldCenterLatLong;
    /**
     * Lock for the re-Clustering of the items.
     */
    public boolean isClustering = false;
    private AsyncTask<Boolean, Void, Void> clusterTask;
    public List<T> getAllItems() {
        ArrayList<T> rtnList = new ArrayList<T>();
        rtnList.addAll(mLeftItems);
        for (Cluster<T> cluster : mClusters ) {
            rtnList.addAll(cluster.getItems());
        }
        return rtnList;
    }
    /**
     * Selected cluster
     */
    protected ClusterMarker<T> selectedItem = null;
    private boolean ignoreOnTapCallBack;

    public ClusterMarker<T> getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(SelectionHandler<T> sender, ClusterMarker<T> selectedItem) {
        this.selectedItem = selectedItem;
    }

    // public static float gap;

    /**
     * @param context The context to access some resources
     * @param mapView The Mapview in which the markers are shown
     * @param markerBitmaps a list of well formed {@link MarkerBitmap}
     * @param maxClusteringZoom The maximum zoom level, beyond this level no clustering is performed.
     */
    public ClusterManager(
            Context context,
            MapView mapView,
            ArrayList<MarkerBitmap> markerBitmaps,
            byte maxClusteringZoom,
            boolean ignoreOnTapCallBack) {
        this.mContext = context;
        this.mMapView = mapView;
        // set to impossible values to trigger clustering at first onChange
        oldZoolLevel = -1;
        oldCenterLatLong = new LatLong(-180.0, -180.0);
        // Check correct order of the makerbitmaps within the list
        for (int i = 1; i < markerBitmaps.size(); i++) {
            if (markerBitmaps.get(i - 1).getItemMax() > markerBitmaps.get(i)
                    .getItemMax()) {
                throw new IllegalArgumentException(
                        "markerBitmaps must be in "
                                + "order from smallest to largest 'maxSize' values, but "
                                + (i - 1)
                                + ".element's maxSize is larger than the following value!");
            }
        }
        this.mMarkerIconBmps = markerBitmaps;
        // ClusterManager.gap = Caption.DEFAULT_GAP * mapView.getModel().displayModel.getScaleFactor();
        this.mMaxClusteringZoom = maxClusteringZoom;
        this.ignoreOnTapCallBack = ignoreOnTapCallBack;
    }

    /**
     * add item and do isClustering. NOTE: this method will not redraw screen.
     * after adding all items, you must call redraw() method.
     *
     * @param item
     *            GeoItem to be clustered.
     */
    public synchronized void addItem(T item) {
        // if mapView is not inflated or if not in viewport, add to mLeftItems
        if (mMapView.getWidth() == 0 || !isItemInViewport(item)) {
            synchronized (mLeftItems) {
                mLeftItems.add(item);
            }
        } else if (mMaxClusteringZoom >= mMapView.getModel().mapViewPosition
                .getZoomLevel()) {
            // else add to a cluster;
            MapViewProjection proj = new MapViewProjection(mMapView);
            Point pos = proj.toPixels(item.getLatLong());
            // check existing cluster
            for (int i = mClusters.size() - 1; i >= 0; i--) {
                // find a cluster which contains the marker.
                // use 1st element to fix the location, hinder the cluster from
                // running around and isClustering.
                LatLong gpCenter = mClusters.get(i).getItems().get(0)
                        .getLatLong();
                Point ptCenter = proj.toPixels(gpCenter);
                // find a cluster which contains the marker.
                if (pos.distance(ptCenter) <= GRIDSIZE
				/*
				 * pos.x >= ptCenter.x - GRIDSIZE && pos.x <= ptCenter.x +
				 * GRIDSIZE && pos.y >= ptCenter.y - GRIDSIZE && pos.y <=
				 * ptCenter.y + GRIDSIZE
				 */) {
                    mClusters.get(i).addItem(item);
                    return;
                }
            }
            // No cluster contain the marker, create a new cluster.
            mClusters.add(createCluster(item));
        } else {
            // No clustering allowed, create a new cluster with single item.
            mClusters.add(createCluster(item));
        }
    }

    /**
     * Create Cluster Object. override this method, if you want to use custom
     * GeoCluster class.
     *
     * @param item
     *            GeoItem to be set to cluster.
     */
    public Cluster<T> createCluster(T item) {
        return new Cluster<T>(this, item);
    }

    /**
     * redraws clusters
     */
    public void redraw() {
        for (int i = 0; i < mClusters.size(); i++) {
            mClusters.get(i).redraw();
        }
    }

    /**
     * check if the item is within current viewport.
     *
     * @return true if item is within viewport.
     */
    protected boolean isItemInViewport(GeoItem item) {
        return getCurBounds().contains(item.getLatLong());
    }

    /**
     * get current Bound
     *
     * @return current GeoBounds
     */
    protected BoundingBox getCurBounds() {
        MapViewProjection projection = new MapViewProjection(mMapView);
        if (mMapView == null) {
            throw new NullPointerException("mMapView == null");
        }
        if (mMapView.getWidth() == 0) {
            throw new NullPointerException("mMapView.getWidth() == 0");
        }
        /** North-West geo point of the bound */
        LatLong nw_ = projection.fromPixels(0, 0);
        /** South-East geo point of the bound */
        LatLong se_ = projection.fromPixels(mMapView.getWidth(),
                mMapView.getHeight());
        return new BoundingBox(se_.latitude, nw_.longitude, nw_.latitude,
                se_.longitude);
    }

    /**
     * add items that were not clustered in last isClustering.
     */
    protected synchronized void addLeftItems() {
        if (mLeftItems.size() == 0) {
            return;
        }
        synchronized (mLeftItems) {
            ArrayList<T> currentLeftItems = new ArrayList<T>();
            currentLeftItems.addAll(mLeftItems);
            mLeftItems.clear();
            for (int i = 0; i < currentLeftItems.size(); i++) {
                addItem(currentLeftItems.get(i));
            }
        }
    }

    // *********************************************************************************************************************
    // Methods to implement 'Observer'
    // *********************************************************************************************************************
    @Override
    public void onChange() {
        if (isClustering)
            return;
        // Log.e(getClass().getSimpleName(), "Position: "
        // + mMapView.getModel().mapViewPosition.getCenter().toString()
        // + "ZoomLavel: " +
        // mMapView.getModel().mapViewPosition.getZoomLevel());
        if (oldZoolLevel != mMapView.getModel().mapViewPosition.getZoomLevel()) {
            // react on zoom changes
            setSelectedItem(null, null);
            oldZoolLevel = mMapView.getModel().mapViewPosition.getZoomLevel();
            resetViewport(false);
        } else {
            // react on position changes
            MapViewPosition mapViewPosition = mMapView.getModel().mapViewPosition;
            MapViewProjection projection = new MapViewProjection(mMapView);

            Point posOld = projection.toPixels(oldCenterLatLong);
            Point posNew = projection.toPixels(mapViewPosition.getCenter());
            if (posOld.distance(posNew) > GRIDSIZE / 2) {
                // Log.d(TAG, "moving...");
                oldCenterLatLong = mapViewPosition.getCenter();
                resetViewport(true);
            }
        }
    }

    /**
     * reset current viewport, re-cluster the items when zoom has changed, else
     * add not clustered items .
     */
    private synchronized void resetViewport(boolean isMoving) {
        isClustering = true;
        clusterTask = new ClusterTask();
        clusterTask.execute(new Boolean[] { isMoving });
    }

    private class ClusterTask extends AsyncTask<Boolean, Void, Void> {

        @Override
        protected Void doInBackground(Boolean... params) {
            if (params[0]) {
                addLeftItems();
            }
            // If the cluster zoom level changed then destroy the cluster and
            // collect its markers.
            else {
                for (int i = mClusters.size(); i > 0; i--) {
                    Cluster<T> cluster = mClusters.get(i - 1);
                    synchronized (mLeftItems) {
                        mLeftItems.addAll(cluster.getItems());
                    }
                    cluster.clear();
                    mClusters.remove(cluster);
                }
                addLeftItems();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            redraw();
            isClustering = false;
        }
    }

    public void destroyGeoClusterer() {
        for (MarkerBitmap markerBitmap : mMarkerIconBmps) {
            if ( markerBitmap.getBitmap(true) != null)
                markerBitmap.getBitmap(true).decrementRefCount();
            if ( markerBitmap.getBitmap(false) != null)
                markerBitmap.getBitmap(false).decrementRefCount();
        }
        for (Cluster<T> cluster : mClusters) {
            cluster.clear();
        }
        /** Clustered object list. */
        synchronized (mLeftItems) {
            mLeftItems.clear();
        }
        MarkerBitmap.clearCaptionBitmap();
    }

    public boolean getIgnoreOnTap() {
        return this.ignoreOnTapCallBack;
    }
}
