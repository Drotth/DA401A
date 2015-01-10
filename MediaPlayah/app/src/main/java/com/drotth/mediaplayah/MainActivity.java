package com.drotth.mediaplayah;

import android.app.ListActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ListActivity implements SensorEventListener {
    private ArrayList<Integer> songs = new ArrayList<Integer>();
    private SongsListAdapter<Integer> songsListAdapter;
    private int current_song;
    private int counter = 0;

    private MediaPlayer player;
    private Sensor sensor;
    private SensorManager sensorManager;
    private TextView reading_textView;
    private ListView songs_listView;

    private boolean knock_activated = false;
    private long knock_first = 0;
    private long knock_time = 1500;

    private long last_update = 0;
    private int knocks = 0;

    private final static int SAMPLE_TIME = 100;
    private final static double KNOCK_THRESHOLD = 0.4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songs.add(R.raw.kerning_city_theme);
        songs.add(R.raw.the_jesters_of_the_moon);
        songs.add(R.raw.ukulele_the_chocobo);
        current_song = songs.get(counter);

        songs_listView = (ListView) findViewById(android.R.id.list);
        songsListAdapter = new SongsListAdapter<Integer>(this, songs);
        songs_listView.setAdapter(songsListAdapter);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        reading_textView = (TextView) findViewById(R.id.sensorReading);
    }

    private int nextSong() {

        if (counter > 2) {
            counter = 0;
        } else {
            current_song = songs.get(counter);
            counter++;
        }
        return current_song;
    }

    private int prevSong() {

        if (counter < 0) {
            counter = 2;
        } else {
            current_song = songs.get(counter);
            counter--;
        }
        return current_song;
    }

//    private void updateView(){
//        ImageView view = (ImageView)(songs_listView.getChildAt(current_song).findViewById(R.id.list_ImageView));
//        view.setImageResource(R.drawable.pauseicon);
//    }

    public void playPause(View view){
        if (player == null || !player.isPlaying()) {
            Toast.makeText(this, "Starting music!", Toast.LENGTH_SHORT).show();
            player = MediaPlayer.create(getApplicationContext(), current_song);
            player.start();

        } else {
            Toast.makeText(this, "Pausing music!", Toast.LENGTH_SHORT).show();
            player.pause();
        }
    }

    public void next(View view){
        Toast.makeText(this, "Next song!", Toast.LENGTH_SHORT).show();
        if (player != null) {
            player.stop();
            player = MediaPlayer.create(getApplicationContext(), nextSong());
            player.start();
        }
    }

    public void previous(View view){
        Toast.makeText(this, "Previous song!", Toast.LENGTH_SHORT).show();
        if (player != null) {
            player.stop();
            player = MediaPlayer.create(getApplicationContext(), prevSong());
            player.start();
        }
    }

    public void stop(View view){
        Toast.makeText(this, "Stopping music!", Toast.LENGTH_SHORT).show();
        if (player != null && player.isPlaying()) {
            player.stop();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - last_update) > SAMPLE_TIME){
            last_update = currentTime;

            float z_value = event.values[2];
            float corrected_value = Math.abs(z_value - SensorManager.GRAVITY_EARTH);

            //Log.d("GODDAMMIT", "Number of knocks: " + knocks + " Corrected value: " + corrected_value);
            reading_textView.setText("Z: " + corrected_value + " Knocks: " + knocks);

            if (knocks > 4) {
                knock_activated = false;
                knocks = 0;
                knock_first = 0;
            }

            if (corrected_value > KNOCK_THRESHOLD) {
                knock_first = System.currentTimeMillis();
                knock_activated = true;
                knocks++;
            }

            if (currentTime - knock_first > knock_time && knock_activated){
                switch (knocks) {
                    case 1:
                        playPause(null);
                        //updateView();
                        break;

                    case 2:
                        next(null);
                        break;

                    case 3:
                        previous(null);
                        break;

                    case 4:
                        stop(null);
                        break;

                    default:
                        Toast.makeText(this, "Too many knocks, calm down!", Toast.LENGTH_SHORT).show();
                        break;
                }

                knock_activated = false;
                knocks = 0;
                knock_first = 0;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onStop(){
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        super.onStop();
    }

    @Override
    public void onDestroy(){
        sensorManager.unregisterListener(this, sensor);
        super.onDestroy();
    }
}