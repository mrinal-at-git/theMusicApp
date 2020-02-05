package com.example.themusicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    MediaPlayer player;
    ImageButton play, pause, stop;
    TextView time_elapsed, time_remaining;
    SeekBar position_bar, volume_bar;
    int totalTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player = MediaPlayer.create(this, R.raw.audio);
        player.setLooping(true);
        player.seekTo(0);
        player.setVolume(0.5f, 0.5f);
        totalTime = player.getDuration();

        play = findViewById(R.id.btn_play);
        pause = findViewById(R.id.btn_pause);
        stop = findViewById(R.id.btn_stop);

        time_elapsed = findViewById(R.id.elapsed_time);
        time_remaining = findViewById(R.id.remaining_time);

        position_bar = findViewById(R.id.position_bar);
        position_bar.setMax(totalTime);
        position_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            // onProgressChanged  is called each time we change the bar
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    player.seekTo(progress);
                    position_bar.setProgress(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        volume_bar = findViewById(R.id.volume_bar);
        volume_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            // onProgressChanged  is called each time we change the bar
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                float volumeNum = progress / 100f;    // the default value of progress is  min =0  and max 100,  setVolume()  takes float value between 0.0 and 1.0 , that's why we need division.
                player.setVolume(volumeNum, volumeNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //thread updates position_bar & timeLabel continuously until song does't gets over
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (player != null) {

                    try {
                        Message msg = new Message();
                        msg.what = player.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }


                }
            }
        }).start();


        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);

    }


    // update the position_bar, time elapsed and time remaining
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;

            //Update position_bar
            position_bar.setProgress(currentPosition);

            //update labels
            String as_time = createTimelabel(currentPosition);
            time_elapsed.setText(as_time);


            String man_time = createTimelabel(totalTime - currentPosition);
            time_remaining.setText("-" + man_time);

        }


    };


    // calculates time to be displayed in  min:sec format
    public String  createTimelabel(int time){

        String timeLabel ="";

        int min = time/1000/60;
        int sec = time/1000 %60;

        timeLabel = min + ":";

        if(sec <10) timeLabel += "0";
        timeLabel +=sec;

        return timeLabel;

    }














    @Override
    public void onClick(View v){

     if(v == play){

      if(!player.isPlaying())
          player.start(); // for playing the music if it's not playing

     }


     if(v == pause){


      player.pause(); // for pausing the music

     }

     if(v == stop){


      player.stop(); // for stopping the music


     }






    }
}
