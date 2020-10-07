package com.mousebirdconsulting.helloearth;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.mousebird.maply.AttrDictionary;
import com.mousebird.maply.BaseController;
import com.mousebird.maply.ComponentObject;
import com.mousebird.maply.GlobeController;
import com.mousebird.maply.MarkerInfo;
import com.mousebird.maply.Point2d;
import com.mousebird.maply.ScreenMarker;
import com.mousebird.maply.SelectedObject;
import com.mousebird.maply.VectorObject;

import java.util.ArrayList;
import java.util.List;

public class HelloMarkerFragment extends HelloGeoJsonFragment {

    @Override
    protected void controlHasStarted() {
        super.controlHasStarted();

        // Insert Markers
        insertMarkers();

        globeControl.gestureDelegate = this;
    }

    private void insertMarkers() {
        List<ScreenMarker> markers = new ArrayList<>();
        MarkerInfo markerInfo = new MarkerInfo();
        Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_city);
        Point2d markerSize = new Point2d(144, 144);

        // Moscow - Москва
        MarkerProperties properties = new MarkerProperties();
        properties.city = "Moscow";
        properties.subject = "Москва";
        ScreenMarker marker = new ScreenMarker();
        marker.loc = Point2d.FromDegrees(37.616667, 55.75); // Longitude, Latitude
        marker.image = icon;
        marker.size = markerSize;
        marker.selectable = true;
        marker.userObject = properties;
        markers.add(marker);

        //  Saint Petersburg - Санкт-Петербург
        properties = new MarkerProperties();
        properties.city = "Saint Petersburg";
        properties.subject = "Санкт-Петербург";
        marker = new ScreenMarker();
        marker.loc = Point2d.FromDegrees(30.3, 59.95);
        marker.image = icon;
        marker.size = markerSize;
        marker.selectable = true;
        marker.userObject = properties;
        markers.add(marker);

        // Novosibirsk - Новосибирск
        properties = new MarkerProperties();
        properties.city = "Novosibirsk";
        properties.subject = "Новосибирск";
        marker = new ScreenMarker();
        marker.loc = Point2d.FromDegrees(82.95, 55.05);
        marker.image = icon;
        marker.size = markerSize;
        marker.selectable = true;
        marker.userObject = properties;
        markers.add(marker);

        // Yekaterinburg - Екатеринбург
        properties = new MarkerProperties();
        properties.city = "Yekaterinburg";
        properties.subject = "Екатеринбург";
        marker = new ScreenMarker();
        marker.loc = Point2d.FromDegrees(60.583333, 56.833333);
        marker.image = icon;
        marker.size = markerSize;
        marker.selectable = true;
        marker.userObject = properties;
        markers.add(marker);

        // Nizhny Novgorod - Нижний Новгород
        properties = new MarkerProperties();
        properties.city = "Nizhny Novgorod";
        properties.subject = "Нижний Новгород";
        marker = new ScreenMarker();
        marker.loc = Point2d.FromDegrees(44.0075, 56.326944);
        marker.image = icon;
        marker.size = markerSize;
        marker.rotation = Math.PI;
        marker.selectable = true;
        marker.userObject = properties;
        markers.add(marker);

        // Add your markers to the map controller.
        ComponentObject co = globeControl.addScreenMarkers(markers, markerInfo, BaseController.ThreadMode.ThreadAny);
    }

    @Override
    public void userDidSelect(GlobeController globeControl, SelectedObject[] selObjs,
                              Point2d loc, Point2d screenLoc) {
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
                //addSelectedMarker(screenMarker);
            }
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public void addSelectedMarker(ScreenMarker screenMarker) {
        if (selectedMarkerComponent != null) {
            globeControl.removeObject(selectedMarkerComponent, BaseController.ThreadMode.ThreadAny);
        }
        MarkerInfo markerInfo = new MarkerInfo();
        markerInfo.setDrawPriority(Integer.MAX_VALUE);
        Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(), android.R.drawable.star_on);
        Point2d markerSize = new Point2d(100, 100);
        screenMarker.image = icon;
        screenMarker.size = markerSize;
        screenMarker.selectable = true;
        selectedMarkerComponent= globeControl.addScreenMarker(screenMarker, markerInfo, BaseController.ThreadMode.ThreadAny);
    }

    public class MarkerProperties {
        public String city;
        public String subject;
    }

    ComponentObject selectedMarkerComponent;
}
