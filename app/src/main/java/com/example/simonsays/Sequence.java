package com.example.simonsays;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

public class Sequence extends AppCompatActivity {
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
    private Button green;
    private Button yellow;
    private Button red;
    private Button blue;
    private InputStream inStream;
    private SharedPreferences shared;
    private SharedPreferences.Editor sharedEditor;
    private static ArrayList<Player> topPlayers = new ArrayList<>();
    private Handler handler = new Handler();
    private HashMap<String, Integer> audioColor = new HashMap<>();
    private MediaPlayer mediaPlayer;

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
        green = findViewById(R.id.greenBtn);
        yellow = findViewById(R.id.yellowBtn);
        blue = findViewById(R.id.blueBtn);
        red = findViewById(R.id.redBtn);
        buttonColor = new Button[]{green, yellow, blue, red};
        numberColor = new String[]{"#B9FCB9", "#F0F0B1", "#B7B7F3", "#F0A49D"};
        idColor.put("Green", 0);
        idColor.put("Yellow", 1);
        idColor.put("Blue", 2);
        idColor.put("Red", 3);
        audioColor.put("Green", R.raw.green);
        audioColor.put("Blue", R.raw.blue);
        audioColor.put("Yellow", R.raw.yellow);
        audioColor.put("Red", R.raw.red);
        audioColor.put("Fail", R.raw.fail);
        audioColor.put("Win", R.raw.win);
        shared = getPreferences(MODE_PRIVATE);
        sharedEditor = shared.edit();


        // Get count, index and color from intent
        score = this.getIntent().getIntExtra("score", -2);
        count = this.getIntent().getIntExtra("count", -3);
        colors = this.getIntent().getExtras().getStringArrayList("colors");
        playerName = this.getIntent().getExtras().getString("username", "");


        green.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                onCorrect("Green");
            }
        });
        yellow.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                onCorrect("Yellow");
            }
        });
        blue.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                onCorrect("Blue");
            }
        });
        red.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                onCorrect("Red");
            }
        });

        green.setEnabled(false);
        yellow.setEnabled(false);
        blue.setEnabled(false);
        red.setEnabled(false);

        initGame();
    }

    public void initGame(){
        scoreText.setText(score.toString());
        time = 1000;
        for (final String c : colors){
            time = time + 300;
            if (count < score+1) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // do something
                        changeColor(c, Boolean.TRUE, Boolean.TRUE);
                    }
                }, time);
                time = time + 300;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // do something
                        changeColor(c, Boolean.FALSE, Boolean.TRUE);
                    }
                }, time);
                time = time + 300;
                if (count+1 == colors.size()) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playGame();
                        }
                    }, time);
                    time = 0;
                }
            }else{
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        // do something
                        playGame();
                    }
                }, time);
                break;
            }
            count = count + 1;
        }
    }

    public void changeColor (final String color, Boolean change, Boolean sequencia){
        if (sequencia) {
            String temp = "Simon says " + color;
            title.setText(temp);
            scoreText.setText(score.toString());
        }
        if (sequencia && change){
            mediaPlayer = MediaPlayer.create(this, audioColor.get(color));
            mediaPlayer.start();
        }
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

    /*public void changeColor (final String color, Boolean change){
        currentBtn = buttonColor[idColor.get(color)];
        if(change) {
            currentBtn.setBackgroundColor(Color.parseColor(numberColor[idColor.get(color)]));
        }else{
            currentBtn.setBackgroundColor(Color.parseColor(originalColor[idColor.get(color)]));
        }
    }*/

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

    public void playGame(){
        green.setEnabled(true);
        yellow.setEnabled(true);
        blue.setEnabled(true);
        red.setEnabled(true);
        time = 1000;
        count = 0;
        Gson gson = new Gson();
        String rankJSON = shared.getString("username", "");
        if (rankJSON.equals("")){
            topPlayers = new ArrayList<Player>();
        }else{
            topPlayers = (ArrayList<Player>) gson.fromJson(rankJSON,  new TypeToken<ArrayList<Player>>() {
            }.getType());
        }
        String temp;
        if (score+1 > count) {
            temp = "Color: " + (count + 1);
            title.setText(temp);
        }
    }

    public void onCorrect(final String answer){
        if (colors.get(count).equals(answer)){
            mediaPlayer = MediaPlayer.create(this, audioColor.get(answer));
            mediaPlayer.start();
            changeColor(answer, Boolean.TRUE, Boolean.FALSE);
            time = 300;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // do something
                    changeColor(answer, Boolean.FALSE, Boolean.FALSE);
                }
            }, time);
            if ((count+1) == colors.size()){
                mediaPlayer = MediaPlayer.create(this, audioColor.get("Win"));
                mediaPlayer.start();
                score = score + 1;
                gameOver("YOU WIN!");
            }else{
                if (count == score){
                    count = 0;
                    score = score + 1;
                    green.setEnabled(false);
                    yellow.setEnabled(false);
                    blue.setEnabled(false);
                    red.setEnabled(false);
                    initGame();
                }else {
                    count = count + 1;
                    String temp = "Color: " + (count + 1);
                    title.setText(temp);
                }

            }
        }else{
            mediaPlayer = MediaPlayer.create(this, audioColor.get("Fail"));
            mediaPlayer.start();
            gameOver("GAME OVER!");
        }
    }

    public void gameOver(String newTitle) {
        Intent intent = new Intent(Sequence.this, Ranking.class);
        topPlayers.add(new Player(playerName, score));
        Collections.sort(topPlayers);
        List<Player> fivePlayers = new ArrayList<>();

        try {
            fivePlayers = topPlayers.subList(0, 5);
        }catch(Exception e){
            fivePlayers = topPlayers;
        }

        Gson gson = new Gson();
        String info = gson.toJson(fivePlayers);
        sharedEditor.putString("username", info);
        sharedEditor.commit();
        ArrayList<String> rankingString = new ArrayList<String>();
        for (Player o : fivePlayers) {
            rankingString.add(o.toString());
        }
        intent.putStringArrayListExtra("topFive", rankingString);
        intent.putExtra("myTitle", newTitle);
        intent.putExtra("myName", playerName);
        intent.putExtra("myScore", score);
        startActivity(intent);
    }



}
