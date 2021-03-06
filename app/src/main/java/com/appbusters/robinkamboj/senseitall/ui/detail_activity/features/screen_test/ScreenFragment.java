package com.appbusters.robinkamboj.senseitall.ui.detail_activity.features.screen_test;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appbusters.robinkamboj.senseitall.R;
import com.appbusters.robinkamboj.senseitall.ui.detail_activity.abstract_stuff.feature_and_sensor.FeatureFragment;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenFragment extends FeatureFragment implements ScreenInterface {

    private DisplayMetrics displayMetrics;

    public ScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the parentView for this fragment
        View v = inflater.inflate(R.layout.fragment_screen, container, false);
        setup(v);
        return v;
    }

    @Override
    public void setup(View v) {

        ButterKnife.bind(this, v);
        initializeSensor();

        hideGoToTestIfNoTest();

        setupAbout();

        showSensorDetails();
    }

    @Override
    public void initializeSensor() {
        if(getActivity() == null) return;
        displayMetrics = getActivity().getResources().getDisplayMetrics();
    }

    @Override
    public void initializeBasicInformation() {
        if(displayMetrics != null) {
            addToDetailsList(sensorDetails, "Height In Pixels", String.valueOf(displayMetrics.heightPixels));
            addToDetailsList(sensorDetails, "Width In Pixels", String.valueOf(displayMetrics.widthPixels));
            addToDetailsList(sensorDetails, "Screen Density (Pixels)", String.valueOf(displayMetrics.density));
            addToDetailsList(sensorDetails, "Density Per Inch (DPI)", String.valueOf(displayMetrics.densityDpi));
            addToDetailsList(sensorDetails, "Scaled Density (SP)", String.valueOf(displayMetrics.scaledDensity));
            addToDetailsList(sensorDetails, "Width In DPI", String.valueOf(displayMetrics.xdpi));
            addToDetailsList(sensorDetails, "Height In DPI", String.valueOf(displayMetrics.ydpi));
            addToDetailsList(sensorDetails, "Width In SP", String.valueOf(
                    displayMetrics.widthPixels/displayMetrics.scaledDensity
            ));
            addToDetailsList(sensorDetails, "Height In SP", String.valueOf(
                    displayMetrics.heightPixels/displayMetrics.scaledDensity
            ));
        }
    }
}
