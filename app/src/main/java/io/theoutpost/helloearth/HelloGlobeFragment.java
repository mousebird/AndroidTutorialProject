package io.theoutpost.helloearth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mousebird.maply.ComponentObject;
import com.mousebird.maply.GlobeMapFragment;
import com.mousebird.maply.MaplyBaseController;
import com.mousebird.maply.Point2d;
import com.mousebird.maply.QuadImageTileLayer;
import com.mousebird.maply.RemoteTileInfo;
import com.mousebird.maply.RemoteTileSource;
import com.mousebird.maply.Shape;
import com.mousebird.maply.ShapeInfo;
import com.mousebird.maply.ShapeSphere;
import com.mousebird.maply.SphericalMercatorCoordSystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class HelloGlobeFragment extends GlobeMapFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle inState) {
        super.onCreateView(inflater, container, inState);

        // Do app specific setup logic.

        return baseControl.getContentView();
    }

    @Override
    protected MapDisplayType chooseDisplayType() {
        return MapDisplayType.Globe;
    }

    @Override
    protected void controlHasStarted() {
        // setup base layer tiles
        String cacheDirName = "stamen_watercolor";
        File cacheDir = new File(getActivity().getCacheDir(), cacheDirName);
        cacheDir.mkdir();
        RemoteTileSource remoteTileSource = new RemoteTileSource(new RemoteTileInfo("http://tile.stamen.com/watercolor/", "png", 0, 18));
        remoteTileSource.setCacheDir(cacheDir);
        SphericalMercatorCoordSystem coordSystem = new SphericalMercatorCoordSystem();

        // globeControl is the controller when using MapDisplayType.Globe
        // mapControl is the controller when using MapDisplayType.Map
        QuadImageTileLayer baseLayer = new QuadImageTileLayer(globeControl, coordSystem, remoteTileSource);
        baseLayer.setImageDepth(1);
        baseLayer.setSingleLevelLoading(false);
        baseLayer.setUseTargetZoomLevel(false);
        baseLayer.setCoverPoles(true);
        baseLayer.setHandleEdges(true);

        // add layer and position
        globeControl.addLayer(baseLayer);
        globeControl.animatePositionGeo(-3.6704803, 40.5023056, 5, 1.0);

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
        shapeInfo.setColor(0.7f, 0.2f, 0.7f, 0.8f); // R, G, B, A - values 0.0 -> 1.0
        shapeInfo.setDrawPriority(1000000);

        ComponentObject componentObject = globeControl.addShapes(shapes, shapeInfo, MaplyBaseController.ThreadMode.ThreadAny);
    }
}
