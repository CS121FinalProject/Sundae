package com.cs121.project.trollie;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by ivanalvarado on 2/25/16.
 */
public class IntentUtil {
    private Activity activity;

    // constructor
    public IntentUtil(Activity activity) {
        this.activity = activity;
    }

    public void showAccessToken() {
        Intent i = new Intent(activity, AccessTokenActivity.class);
        activity.startActivity(i);
    }
}
