package com.appbusters.robinkamboj.senseitall.view.robin;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appbusters.robinkamboj.senseitall.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.Random;

public class MicrophoneActivity extends AppCompatActivity {

    private AVLoadingIndicatorView indic_1, indic_2;
    private Button startRec, stopRec, play, pause;
    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;
    private String AudioSavePath;
    private Random random;
    String RandomAudioFileName = "ABCDEFGHIJKL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microphone);

        random = new Random();
        startRec = (Button) findViewById(R.id.start_rec);
        stopRec = (Button) findViewById(R.id.stop_rec);
        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);

        stopRec.setEnabled(false);
        play.setEnabled(false);
        pause.setEnabled(false);

        startRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + CreateRandomAudioFileName(5) + "AudioRecording.3gp";
                MediaRecorderReady();

                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                startRec.setEnabled(false);
                stopRec.setEnabled(true);
            }
        });

        stopRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mediaRecorder.stop();
                    stopRec.setEnabled(false);
                    play.setEnabled(true);
                    startRec.setEnabled(true);
                    pause.setEnabled(false);
                }
                catch (IllegalStateException e){
                    e.printStackTrace();
                }

                Toast.makeText(MicrophoneActivity.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));
            i++ ;
        }
        return stringBuilder.toString();
    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePath);
    }
}
