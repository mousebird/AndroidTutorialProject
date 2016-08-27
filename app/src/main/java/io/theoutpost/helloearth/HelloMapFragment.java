package io.theoutpost.helloearth;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mousebird.maply.AttrDictionary;
import com.mousebird.maply.ComponentObject;
import com.mousebird.maply.GlobeMapFragment;
import com.mousebird.maply.LabelInfo;
import com.mousebird.maply.MBTiles;
import com.mousebird.maply.MBTilesImageSource;
import com.mousebird.maply.MapController;
import com.mousebird.maply.MaplyBaseController;
import com.mousebird.maply.MarkerInfo;
import com.mousebird.maply.Point2d;
import com.mousebird.maply.QuadImageTileLayer;
import com.mousebird.maply.RemoteTileInfo;
import com.mousebird.maply.RemoteTileSource;
import com.mousebird.maply.ScreenLabel;
import com.mousebird.maply.ScreenMarker;
import com.mousebird.maply.SelectedObject;
import com.mousebird.maply.SphericalMercatorCoordSystem;
import com.mousebird.maply.VectorObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class HelloMapFragment extends GlobeMapFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle inState) {
        super.onCreateView(inflater, container, inState);

        // Do app specific setup logic.

        return baseControl.getContentView();
    }

    @Override
    protected MapDisplayType chooseDisplayType() {
        return MapDisplayType.Map;
    }

    @Override
    protected void controlHasStarted() {
//        Activity activity = getActivity();
//        final int REQUEST_EXTERNAL_STORAGE = 1;
//        String[] PERMISSIONS_STORAGE = {
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//        };
//        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        }
//
//        File storageDir = Environment.getExternalStorageDirectory();
//        File mbtilesDir = new File(storageDir, "mbtiles");
//        File mbtilesFile = new File(mbtilesDir, "geography-class_medres.mbtiles");
//        if (!mbtilesFile.exists()) {
//            new AlertDialog.Builder(activity)
//                    .setTitle("Missing MBTiles")
//                    .setMessage("Could not find MBTiles file.")
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // do nothing
//                        }
//                    }).show();
//        }
//        MBTiles mbtiles = new MBTiles(mbtilesFile);
//        MBTilesImageSource localTileSource = new MBTilesImageSource(mbtiles);


        // setup base layer tiles
        String cacheDirName = "stamen_watercolor";
        File cacheDir = new File(getActivity().getCacheDir(), cacheDirName);
        cacheDir.mkdir();
        RemoteTileSource remoteTileSource = new RemoteTileSource(new RemoteTileInfo("http://tile.stamen.com/watercolor/", "png", 0, 18));
        remoteTileSource.setCacheDir(cacheDir);
        SphericalMercatorCoordSystem coordSystem = new SphericalMercatorCoordSystem();

        // globeControl is the controller when using MapDisplayType.Globe
        // mapControl is the controller when using MapDisplayType.Map
        QuadImageTileLayer baseLayer = new QuadImageTileLayer(mapControl, coordSystem, remoteTileSource);
        baseLayer.setImageDepth(1);
        baseLayer.setSingleLevelLoading(false);
        baseLayer.setUseTargetZoomLevel(false);
        baseLayer.setCoverPoles(true);
        baseLayer.setHandleEdges(true);

        // add layer and position
        mapControl.addLayer(baseLayer);
        mapControl.animatePositionGeo(-3.6704803, 40.5023056, 5, 1.0);
        mapControl.setAllowRotateGesture(false);

        // Set controller to be gesture delegate.
        // Needed to allow selection.
        mapControl.gestureDelegate = this;

        // Add GeoJSON
        final String url = "https://s3.amazonaws.com/whirlyglobedocs/tutorialsupport/RUS.geojson";
        GeoJsonHttpTask task = new GeoJsonHttpTask(mapControl);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);

        // Insert Markers
        insertMarkers();

        // Insert Labels
//        insertLabels();
    }

    public class MarkerProperties {
        public String city;
        public String subject;
    }

    /**
     * Our markers are the top 5 largest cities in Russia.
     * https://en.wikipedia.org/wiki/List_of_cities_and_towns_in_Russia_by_population
     */
    private void insertMarkers() {
        List<ScreenMarker> markers = new ArrayList<>();

        MarkerInfo markerInfo = new MarkerInfo();
        Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_city);
        Point2d markerSize = new Point2d(180, 180);

        // Moskow - Москва
        MarkerProperties properties = new MarkerProperties();
        properties.city = "Moskow";
        properties.subject = "Moskow";
        ScreenMarker moskow = new ScreenMarker();
        moskow.loc = Point2d.FromDegrees(37.616667, 55.75); // Longitude, Latitude
        moskow.image = icon;
        moskow.size = markerSize;
        moskow.selectable = true;
        moskow.userObject = properties;
        markers.add(moskow);

        // 	Saint Petersburg - Санкт-Петербург
        properties = new MarkerProperties();
        properties.city = "Saint Petersburg";
        properties.subject = "Saint Petersburg";
        ScreenMarker stPetersburg = new ScreenMarker();
        stPetersburg.loc = Point2d.FromDegrees(30.3, 59.95); // Longitude, Latitude
        stPetersburg.image = icon;
        stPetersburg.size = markerSize;
        stPetersburg.selectable = true;
        stPetersburg.userObject = properties;
        markers.add(stPetersburg);

        // Novosibirsk - Новосибирск
        properties = new MarkerProperties();
        properties.city = "Novosibirsk";
        properties.subject = "Novosibirsk Oblast";
        ScreenMarker novosibirsk = new ScreenMarker();
        novosibirsk.loc = Point2d.FromDegrees(82.95, 55.05); // Longitude, Latitude
        novosibirsk.image = icon;
        novosibirsk.size = markerSize;
        novosibirsk.userObject = properties;
        markers.add(novosibirsk);

        // Yekaterinburg - Екатеринбург
        properties = new MarkerProperties();
        properties.city = "Yekaterinburg";
        properties.subject = "Sverdlovsk Oblast";
        ScreenMarker yekaterinburg = new ScreenMarker();
        yekaterinburg.loc = Point2d.FromDegrees(60.583333, 56.833333); // Longitude, Latitude
        yekaterinburg.image = icon;
        yekaterinburg.size = markerSize;
        yekaterinburg.selectable = true;
        yekaterinburg.userObject = properties;
        markers.add(yekaterinburg);

        // Nizhny Novgorod - Нижний Новгород
        properties = new MarkerProperties();
        properties.city = "Nizhny Novgorod";
        properties.subject = "Nizhny Novgorod Oblast";
        ScreenMarker nizhnyNovgorod = new ScreenMarker();
        nizhnyNovgorod.loc = Point2d.FromDegrees(44.0075, 56.326944); // Longitude, Latitude
        nizhnyNovgorod.image = icon;
        nizhnyNovgorod.size = markerSize;
        nizhnyNovgorod.rotation = Math.PI;
        nizhnyNovgorod.selectable = true;
        nizhnyNovgorod.userObject = properties;
        markers.add(nizhnyNovgorod);

        // Add your markers to the map controller.
        ComponentObject markersComponentObject = mapControl.addScreenMarkers(markers, markerInfo, MaplyBaseController.ThreadMode.ThreadAny);

        // ComponentObject is your handle to the marker in the map controller.
        // You can use this to enable, disable, and remove your marker from the map.
//        mapControl.removeObject(markersComponentObject, MaplyBaseController.ThreadMode.ThreadAny);
    }

    private void insertLabels() {
        List<ScreenLabel> labels = new ArrayList<>();

        LabelInfo labelInfo = new LabelInfo();
        labelInfo.setFontSize(30f);
        labelInfo.setTextColor(Color.BLACK);
        labelInfo.setTypeface(Typeface.SERIF);
        labelInfo.setLayoutPlacement(LabelInfo.LayoutRight);
        labelInfo.setOutlineColor(Color.WHITE);
        labelInfo.setOutlineSize(1.f);

        float layoutImportance = 1.f;

        // Moskow - Москва
        ScreenLabel moskow = new ScreenLabel();
        moskow.loc = Point2d.FromDegrees(37.616667, 55.75); // Longitude, Latitude
        moskow.text = "Москва";
        moskow.layoutImportance = layoutImportance++;
        labels.add(moskow);

        // 	Saint Petersburg - Санкт-Петербург
        ScreenLabel stPetersburg = new ScreenLabel();
        stPetersburg.loc = Point2d.FromDegrees(30.3, 59.95); // Longitude, Latitude
        stPetersburg.text = "Санкт-Петербург";
        stPetersburg.layoutImportance = layoutImportance++;
        labels.add(stPetersburg);

        // Novosibirsk - Новосибирск
        ScreenLabel novosibirsk = new ScreenLabel();
        novosibirsk.loc = Point2d.FromDegrees(82.95, 55.05); // Longitude, Latitude
        novosibirsk.text = "Новосибирск";
        novosibirsk.layoutImportance = layoutImportance++;
        labels.add(novosibirsk);

        // Yekaterinburg - Екатеринбург
        ScreenLabel yekaterinburg = new ScreenLabel();
        yekaterinburg.loc = Point2d.FromDegrees(60.583333, 56.833333); // Longitude, Latitude
        yekaterinburg.text = "Екатеринбург";
        yekaterinburg.layoutImportance = layoutImportance++;
        labels.add(yekaterinburg);

        // Nizhny Novgorod - Нижний Новгород
        ScreenLabel nizhnyNovgorod = new ScreenLabel();
        nizhnyNovgorod.loc = Point2d.FromDegrees(44.0075, 56.326944); // Longitude, Latitude
        nizhnyNovgorod.text = "Нижний Новгород";
        nizhnyNovgorod.layoutImportance = layoutImportance++;
        nizhnyNovgorod.rotation = Math.PI / 8;
        labels.add(nizhnyNovgorod);

        // Add your markers to the map controller.
        ComponentObject labelsComponentObject = mapControl.addScreenLabels(labels, labelInfo, MaplyBaseController.ThreadMode.ThreadAny);
    }

    @Override
    public void userDidSelect(MapController mapControl, SelectedObject[] selObjs, Point2d loc, Point2d screenLoc) {
        String msg = "Selected feature count: " + selObjs.length;
        for (SelectedObject obj : selObjs) {
            // GeoJSON
            if (obj.selObj instanceof VectorObject) {
                VectorObject vectorObject = (VectorObject) obj.selObj;
                AttrDictionary attributes = vectorObject.getAttributes();
                String adminName = attributes.getString("ADMIN");
                msg += "\nVector Object: " + adminName;
            }
            // Screen Marker
            else if (obj.selObj instanceof ScreenMarker) {
                ScreenMarker screenMarker = (ScreenMarker) obj.selObj;
                MarkerProperties properties = (MarkerProperties) screenMarker.userObject;
                msg += "\nScreen Marker: " + properties.city + ", " + properties.subject;
            }
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
