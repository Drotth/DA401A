package com.drotth.mediaplayah;

import android.app.ListActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
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

        songs.add(R.raw.game_of_thrones);
        songs.add(R.raw.the_big_bang_theory);
        songs.add(R.raw.the_simpsons);
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
        counter++;

        if (counter > 2) {
            counter = 0;
        } else {
            current_song = songs.get(counter);;
        }
        return current_song;
    }

    private int prevSong() {
        counter--;

        if (counter < 0) {
            counter = 2;
        } else {
            current_song = songs.get(counter);;
        }
        return current_song;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - last_update) > SAMPLE_TIME){
            last_update = currentTime;

            float z_value = event.values[2];
            float corrected_value = Math.abs(z_value - SensorManager.GRAVITY_EARTH);

            Log.d("GODDAMMIT", "Number of knocks: " + knocks + " Corrected value: " + corrected_value);
            //reading_textView.setText("Z: " + corrected_value);

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
                        if (player == null || !player.isPlaying()) {
                            Toast.makeText(this, "Starting music (" + knocks + ")", Toast.LENGTH_SHORT).show();
                            player = MediaPlayer.create(getApplicationContext(), current_song);
                            player.start();

                        } else if(player != null && player.isPlaying()) {
                            Toast.makeText(this, "Pausing music (" + knocks + ")", Toast.LENGTH_SHORT).show();
                            player.pause();
                        }
                        break;

                    case 2:
                        Toast.makeText(this, "Next song (" + knocks + ")", Toast.LENGTH_SHORT).show();
                        if (player != null) { //If music is playing already
                            player.stop();
                            player = MediaPlayer.create(getApplicationContext(), nextSong());
                            player.start();
                        }
                        break;

                    case 3:
                        Toast.makeText(this, "previous song (" + knocks + ")", Toast.LENGTH_SHORT).show();
                        if (player != null) { //If music is playing already
                            player.stop();
                            player = MediaPlayer.create(getApplicationContext(), prevSong());
                            player.start();
                        }
                        break;

                    case 4:
                        Toast.makeText(this, "Stopping music (" + knocks + ")", Toast.LENGTH_SHORT).show();
                        if (player != null && player.isPlaying()) { //If music is playing already
                            player.stop();
                        }
                        break;

                    default:
                        Toast.makeText(this, "Too many knocks: " + knocks, Toast.LENGTH_SHORT).show();
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
        sensorManager.unregisterListener(this, sensor);
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        super.onStop();
    }
}