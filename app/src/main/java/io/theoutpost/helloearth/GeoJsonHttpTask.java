package io.theoutpost.helloearth;


import android.os.AsyncTask;

import com.mousebird.maply.MaplyBaseController;

public class GeoJsonHttpTask extends AsyncTask<String, Void, String> {

    MaplyBaseController controller;

    public GeoJsonHttpTask(MaplyBaseController maplyBaseController) {
        controller = maplyBaseController;
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(String string) {

    }
}
