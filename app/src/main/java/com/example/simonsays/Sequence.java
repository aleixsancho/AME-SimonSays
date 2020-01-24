package com.example.simonsays;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

public class Sequence extends AppCompatActivity {
    private Intent intent;
    private ArrayList<String> colors = new ArrayList<String>();
    private Integer score;
    private Integer count;
    private HashMap<String, Integer> idColor = new HashMap<>();
    private Button currentBtn;
    private TextView title;
    private TextView scoreText;
    private String[] originalColor;
    private Button[] buttonColor;
    private String[] numberColor;
    private int time = 1000;
    private String playerName;
    private OutputStream outputStream;
    private InputStream inStream;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence);

        try{
            initBluetooth();
        }catch (Exception e){

        }
        //Fetch buttons and text areas.
        title = findViewById(R.id.titleText);
        scoreText = findViewById(R.id.scoreText);
        originalColor = new String[]{"#00FF00","#FFFF00","#0000FF","#F44336"};
        Button green = findViewById(R.id.greenBtn);
        Button yellow = findViewById(R.id.yellowBtn);
        Button blue = findViewById(R.id.blueBtn);
        Button red = findViewById(R.id.redBtn);
        buttonColor = new Button[]{green, yellow, blue, red};
        numberColor = new String[]{"#B9FCB9", "#F0F0B1", "#B7B7F3", "#F0A49D"};
        idColor.put("Green", 0);
        idColor.put("Yellow", 1);
        idColor.put("Blue", 2);
        idColor.put("Red", 3);


        // Get count, index and color from intent
        score = this.getIntent().getIntExtra("score", -2);
        count = this.getIntent().getIntExtra("count", -3);
        colors = this.getIntent().getExtras().getStringArrayList("colors");
        playerName = this.getIntent().getExtras().getString("username", "");

        scoreText.setText(score.toString());
        
        Handler handler = new Handler();
        for (final String c : colors){
            time = time + 300;
            if (count < score+1) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // do something
                        changeColor(c, Boolean.TRUE);
                    }
                }, time);
                time = time + 300;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // do something
                        changeColor(c, Boolean.FALSE);
                    }
                }, time);
                time = time + 300;
                if (count+1 == colors.size()) {
                    intent = new Intent(Sequence.this, Game.class);
                    count = 0;
                    intent.putStringArrayListExtra("colors", colors);
                    intent.putExtra("count", count);
                    intent.putExtra("score", score);
                    intent.putExtra("username", playerName);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // do something
                            startActivity(intent);
                        }
                    }, time);
                    time = 0;
                }
            }else{
                intent = new Intent(Sequence.this, Game.class);
                count = 0;
                intent.putStringArrayListExtra("colors", colors);
                intent.putExtra("count", count);
                intent.putExtra("score", score);
                intent.putExtra("username", playerName);
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        // do something
                        startActivity(intent);
                    }
                }, time);
                break;
            }
            count = count + 1;
        }
    }

    public void changeColor (final String color, Boolean change){
        String temp = "Simon says " + color;
        title.setText(temp);
        scoreText.setText(score.toString());
        currentBtn = buttonColor[idColor.get(color)];
        if(change) {
            currentBtn.setBackgroundColor(Color.parseColor(numberColor[idColor.get(color)]));
        }else{
            currentBtn.setBackgroundColor(Color.parseColor(originalColor[idColor.get(color)]));
        }
        try{
            write(String.valueOf(idColor.get(color)));
        }catch (Exception e){

        }
    }

    public void initBluetooth() throws IOException {
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();

                if (bondedDevices.size() > 0) {
                    Object[] devices = (Object[]) bondedDevices.toArray();
                    BluetoothDevice device = (BluetoothDevice) devices[0]; //posició bluetooth mobil.
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inStream = socket.getInputStream();
                }

                Log.e("error", "No appropriate paired devices.");
            } else {
                Log.e("error", "Bluetooth is disabled.");
            }
        }
    }

    public void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }

}
