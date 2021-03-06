package com.appbusters.robinkamboj.senseitall.ui.test_activity.tests.proximity_test_fragment;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appbusters.robinkamboj.senseitall.R;
import com.github.anastr.speedviewlib.PointerSpeedometer;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProximityTestFragment extends Fragment implements ProximityTestInterface {

    private SensorManager manager;
    private int counter = 0;

    @BindView(R.id.pointer_speedometer)
    PointerSpeedometer speedometer;

    @BindView(R.id.counter)
    TextView counterText;

    public ProximityTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the parentView for this fragment
        View v = inflater.inflate(R.layout.fragment_proximity_test, container, false);
        setup(v);
        return v;
    }

    @Override
    public void setup(View v) {
        ButterKnife.bind(this, v);

        loadSensor();
    }

    @Override
    public void loadSensor() {
        if(getActivity() != null) manager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if(manager != null) {
            Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

            if(sensor != null)
                manager.
                        registerListener(eventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            else {
                Toast.makeText(getActivity(), "Failed to load sensor", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        }
    }

    SensorEventListener eventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(sensorEvent.sensor.getType()== Sensor.TYPE_PROXIMITY){
                float currentReading = sensorEvent.values[0];

                if(currentReading==0||currentReading==3){
                    speedometer.speedPercentTo(100, 200);
                    counterText.setText(String.format(Locale.ENGLISH, "%d", ++counter));
                }
                else {
                    speedometer.speedPercentTo(0, 200);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
}
