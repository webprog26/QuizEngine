package com.dark.webprog26.worktastengine.engine.managers;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by webpr on 20.03.2017.
 */

public class ReadJSONFromAssetsManager {

    public static String loadJSONFromAsset(AssetManager assetManager, String jsonFilename) {
        String json;
        try {
            InputStream is = assetManager.open(jsonFilename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
