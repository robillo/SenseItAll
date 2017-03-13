package com.appbusters.robinkamboj.senseitall.view.robin;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appbusters.robinkamboj.senseitall.R;

public class VibratorActivity extends AppCompatActivity {

    private RelativeLayout activity_vibrate;
    private String sensor_name;
    private TextView textView;
    private Context context;
    private Vibrator vib;
    private boolean loop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrator);

        Intent i = getIntent();
        sensor_name = i.getStringExtra("sensorName");
        textView = (TextView) findViewById(R.id.textView);
        textView.setText(sensor_name);

        context = getApplicationContext();

        activity_vibrate = (RelativeLayout) findViewById(R.id.activity_vibrate);
        vib = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.card:{
                vib.vibrate(500);
                break;
            }
            case R.id.card2:{

                break;
            }
            case R.id.card3:{

                break;
            }
            case R.id.card4:{




                Snackbar.make(activity_vibrate, "Click To Exit the Vibrate Loop.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Exit", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loop = false;
                            }
                        }).show();
                break;
            }
        }
    }
}