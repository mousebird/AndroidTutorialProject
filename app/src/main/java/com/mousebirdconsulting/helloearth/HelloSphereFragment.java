package com.mousebirdconsulting.helloearth;

import com.mousebird.maply.BaseController;
import com.mousebird.maply.ComponentObject;
import com.mousebird.maply.Point2d;
import com.mousebird.maply.Shape;
import com.mousebird.maply.ShapeInfo;
import com.mousebird.maply.ShapeSphere;

import java.util.ArrayList;
import java.util.List;

public class HelloSphereFragment extends HelloGlobeFragment {

    @Override
    protected void controlHasStarted() {
        super.controlHasStarted();

        insertSpheres();
    }

    private void insertSpheres() {
        List<Shape> shapes = new ArrayList<>();

        // Kansas City
        ShapeSphere shape = new ShapeSphere();
        shape.setLoc(Point2d.FromDegrees(-94.58, 39.1));
        shape.setRadius(0.04f); // 1.0 is the radius of the Earth
        shapes.add(shape);

        // Washington D.C.
        shape = new ShapeSphere();
        shape.setLoc(Point2d.FromDegrees(-77.036667, 38.895111));
        shape.setRadius(0.1f);
        shapes.add(shape);

        // McMurdo Station
        shape = new ShapeSphere();
        shape.setLoc(Point2d.FromDegrees(166.666667, -77.85));
        shape.setRadius(0.2f);
        shapes.add(shape);

        // Windhoek
        shape = new ShapeSphere();
        shape.setLoc(Point2d.FromDegrees(17.083611, -22.57));
        shape.setRadius(0.08f);
        shapes.add(shape);

        ShapeInfo shapeInfo = new ShapeInfo();
        shapeInfo.setColor(0.7f, 0.2f, 0.7f, 0.8f); // R,G,B,A - values [0.0 => 1.0]
        shapeInfo.setDrawPriority(1000000);

        ComponentObject co = globeControl.addShapes(shapes, shapeInfo, BaseController.ThreadMode.ThreadAny);

        double latitude = 40 * Math.PI / 180;
        double longitude = -100 * Math.PI / 180;
        double zoom_earth_radius = 1.0;
        globeControl.animatePositionGeo(longitude, latitude, zoom_earth_radius, 1.0);
    }
}
