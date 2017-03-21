package com.dark.webprog26.worktastengine.engine.events;

import android.content.res.AssetManager;

/**
 * Created by webpr on 21.03.2017.
 */

public class ReadJSONFromAssetsEvent {

    private final String mJSONFileName;

    public ReadJSONFromAssetsEvent(String mJSONFileName) {
        this.mJSONFileName = mJSONFileName;
    }

    public String getJSONFileName() {
        return mJSONFileName;
    }
}
