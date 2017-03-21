package com.dark.webprog26.worktastengine.engine.events;

import android.content.res.AssetManager;

/**
 * Created by webpr on 21.03.2017.
 */

public class ReadJSONFromAssetsEvent {

    /**
     * This event takes place when there is no data in FirebaseDatabase to run the quiz, so the app should
     * read this data from JSON file stored in device assets directory
     */

    private final String mJSONFileName;

    public ReadJSONFromAssetsEvent(String mJSONFileName) {
        this.mJSONFileName = mJSONFileName;
    }

    public String getJSONFileName() {
        return mJSONFileName;
    }
}
