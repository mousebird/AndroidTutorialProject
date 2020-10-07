package com.mousebirdconsulting.helloearth;

import android.graphics.Color;
import android.graphics.Typeface;

import com.mousebird.maply.BaseController;
import com.mousebird.maply.ComponentObject;
import com.mousebird.maply.LabelInfo;
import com.mousebird.maply.Point2d;
import com.mousebird.maply.ScreenLabel;

import java.util.ArrayList;
import java.util.List;

public class HelloLabelFragment extends HelloMarkerFragment {

    @Override
    protected void controlHasStarted() {
        super.controlHasStarted();

        // Insert Labels
        insertLabels();
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

        // Moscow - Москва
        ScreenLabel label = new ScreenLabel();
        label.loc = Point2d.FromDegrees(37.616667, 55.75); // Longitude, Latitude
        label.text = "Москва";
        label.layoutImportance = layoutImportance++;
        labels.add(label);

        //  Saint Petersburg - Санкт-Петербург
        label = new ScreenLabel();
        label.loc = Point2d.FromDegrees(30.3, 59.95);
        label.text = "Санкт-Петербург";
        label.layoutImportance = layoutImportance++;
        labels.add(label);

        // Novosibirsk - Новосибирск
        label = new ScreenLabel();
        label.loc = Point2d.FromDegrees(82.95, 55.05);
        label.text = "Новосибирск";
        label.layoutImportance = layoutImportance++;
        labels.add(label);

        // Yekaterinburg - Екатеринбург
        label = new ScreenLabel();
        label.loc = Point2d.FromDegrees(60.583333, 56.833333);
        label.text = "Екатеринбург";
        label.layoutImportance = layoutImportance++;
        labels.add(label);

        // Nizhny Novgorod - Нижний Новгород
        label = new ScreenLabel();
        label.loc = Point2d.FromDegrees(44.0075, 56.326944);
        label.text = "Нижний Новгород";
        label.layoutImportance = layoutImportance++;
        label.rotation = Math.PI / 8;
        labels.add(label);

        // Add your markers to the map controller.
        ComponentObject co = globeControl.addScreenLabels(labels, labelInfo, BaseController.ThreadMode.ThreadAny);    }
}
