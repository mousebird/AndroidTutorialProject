package com.mousebirdconsulting.helloearth;

import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mousebird.maply.AttrDictionary;
import com.mousebird.maply.BaseController;
import com.mousebird.maply.ComponentObject;
import com.mousebird.maply.GlobeController;
import com.mousebird.maply.Point2d;
import com.mousebird.maply.SelectedObject;
import com.mousebird.maply.VectorInfo;
import com.mousebird.maply.VectorObject;

public class HelloGeoJsonFragment extends HelloGlobeFragment {

    @Override
    protected void controlHasStarted() {
        super.controlHasStarted();

        double latitude = 60 * Math.PI / 180;
        double longitude = 90 * Math.PI / 180;
        double zoom_earth_radius = 2.0;
        globeControl.animatePositionGeo(longitude, latitude, zoom_earth_radius, 1.0);

        final String url = "https://s3.amazonaws.com/whirlyglobedocs/tutorialsupport/RUS.geojson";

        if (loadTask != null) {
            loadTask.cancel(true);
            loadTask = null;
        }
        loadTask = new GeoJsonHttpTask(globeControl);

        loadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);

        // Tell the controller that this object is the gesture delegate.
        globeControl.gestureDelegate = this;
    }

    @Override
    public void userDidSelect(GlobeController globeControl, SelectedObject[] selObjs, Point2d loc, Point2d screenLoc) {
        super.userDidSelect(globeControl, selObjs, loc, screenLoc);

        String msg = "Selected feature count: " + selObjs.length;
        for (SelectedObject obj : selObjs) {
            // GeoJSON
            if (obj.selObj instanceof VectorObject) {
                VectorObject vectorObject = (VectorObject) obj.selObj;
                AttrDictionary attributes = vectorObject.getAttributes();
                String adminName = attributes.getString("ADMIN");
                msg += "\nVector Object: " + adminName;
                addSelectedObject(vectorObject);
            }
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    private void addSelectedObject(VectorObject vectorObject) {
        // remove any previously-selected object
        if (selectedComponentObject != null) {
            globeControl.removeObject(selectedComponentObject, BaseController.ThreadMode.ThreadAny);
        }

        // Re-add the object with different info
        VectorInfo vectorInfo = new VectorInfo();
        vectorInfo.setColor(Color.argb(255,255,140,0)); // Gold
        vectorInfo.setLineWidth(10.f);
        vectorInfo.setDrawPriority(Integer.MAX_VALUE); // Make sure it draws on top of unselected vector
        selectedComponentObject = globeControl.addVector(vectorObject, vectorInfo, BaseController.ThreadMode.ThreadAny);
    }

    @Override
    public void onDestroy() {
        if (loadTask != null) {
            loadTask.cancel(true);
            loadTask = null;
        }
        super.onDestroy();
    }

    private GeoJsonHttpTask loadTask = null;

    private ComponentObject selectedComponentObject;
}
