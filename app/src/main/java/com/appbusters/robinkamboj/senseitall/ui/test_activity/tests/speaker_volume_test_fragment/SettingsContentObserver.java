package com.appbusters.robinkamboj.senseitall.ui.test_activity.tests.speaker_volume_test_fragment;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

import com.appbusters.robinkamboj.senseitall.R;
import com.appbusters.robinkamboj.senseitall.ui.test_activity.TestActivity;

public class SettingsContentObserver extends ContentObserver {

    private Context activityContext;

    SettingsContentObserver(Context activityContext, Handler handler) {
        super(handler);
        this.activityContext = activityContext;
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        TestActivity testActivity = (TestActivity) activityContext;
        if(testActivity != null) {
            SoundTestFragment fragment = (
                    SoundTestFragment) ((TestActivity) activityContext)
                    .getSupportFragmentManager()
                    .findFragmentByTag(activityContext.getString(R.string.tag_sound_test_fragment));

            if(fragment != null)
                fragment.setCroller();
        }
    }
}
