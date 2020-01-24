package com.example.simonsays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.jetbrains.annotations.Nullable;

public class Game extends AppCompatActivity {

    private TextView title;
    private Button green;
    private Button yellow;
    private Button red;
    private Button restart;
    private Button blue;
    private ArrayList<String> colors = new ArrayList<String>();
    private Integer score;
    private Integer count;
    private HashMap<String, Integer> idColor = new HashMap<>();
    private int time = 0;
    private Button[] buttonColor;
    private Button currentBtn;
    private String[] originalColor;
    private String[] numberColor;
    private SharedPreferences shared;
    private SharedPreferences.Editor sharedEditor;
    private static ArrayList<Player> topPlayers = new ArrayList<>();
    private String playerName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Fetch buttons and text areas.
        title = findViewById(R.id.titleText);
        TextView scoreText = findViewById(R.id.scoreText);
        green = findViewById(R.id.greenBtn);
        yellow = findViewById(R.id.yellowBtn);
        blue = findViewById(R.id.blueBtn);
        red = findViewById(R.id.redBtn);
        restart = findViewById(R.id.restartBtn);
        idColor.put("Green",0);
        idColor.put("Yellow",1);
        idColor.put("Blue",2);
        idColor.put("Red",3);
        buttonColor = new Button[]{green, yellow, blue, red};
        originalColor = new String[]{"#00FF00","#FFFF00","#0000FF","#F44336"};
        numberColor = new String[]{"#B9FCB9", "#F0F0B1", "#B7B7F3", "#F0A49D"};
        shared = getPreferences(MODE_PRIVATE);
        sharedEditor = shared.edit();

        // Get count, index and color from intent
        score = this.getIntent().getIntExtra("score", -2);
        count = this.getIntent().getIntExtra("count", -3);
        colors = this.getIntent().getExtras().getStringArrayList("colors");
        playerName = this.getIntent().getExtras().getString("username", "");

        // Update displayed score
        scoreText.setText(score.toString());

        Gson gson = new Gson();
        String rankJSON = shared.getString("username", "");
        if (rankJSON.equals("")){
            topPlayers = new ArrayList<Player>();
        }else{
            topPlayers = (ArrayList<Player>) gson.fromJson(rankJSON,  new TypeToken<ArrayList<Player>>() {
            }.getType());
        }

        // Update title text
        String temp;
        if (score+1 > count) {
            temp = "Color: " + (count + 1);
            title.setText(temp);
        }

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

    }

    public void gameOver(String newTitle) {
        Intent intent = new Intent(Game.this, Ranking.class);
        topPlayers.add(new Player(playerName, score));
        Collections.sort(topPlayers);

        List<Player> fivePlayers = topPlayers.subList(0,5);

        Gson gson = new Gson();
        String info = gson.toJson(fivePlayers);
        sharedEditor.putString("username", info);
        sharedEditor.commit();
        ArrayList<String> rankingString = new ArrayList<String>();
        for (Player o : fivePlayers) {
            rankingString.add(o.toString());
        }
        intent.putStringArrayListExtra("topFive", rankingString);
        intent.putExtra("myName", playerName);
        intent.putExtra("myScore", score);
        startActivity(intent);
    }

    public void onCorrect(final String answer){
        if (colors.get(count).equals(answer)){
            changeColor(answer, Boolean.TRUE);
            time = time + 300;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // do something
                    changeColor(answer, Boolean.FALSE);
                }
            }, time);
            if ((count+1) == colors.size()){
                gameOver("YOU WIN!");
            }else{
                if (count == score){
                    Intent intent = new Intent(Game.this, Sequence.class);
                    count = 0;
                    score = score + 1;
                    intent.putStringArrayListExtra("colors", colors);
                    intent.putExtra("count", count);
                    intent.putExtra("score", score);
                    intent.putExtra("username", playerName);
                    startActivity(intent);
                }else {
                    count = count + 1;
                    String temp = "Color: " + (count + 1);
                    title.setText(temp);
                }

            }
        }else{
            gameOver("gameOver");
        }
    }

    public void changeColor (final String color, Boolean change){
        currentBtn = buttonColor[idColor.get(color)];
        if(change) {
            currentBtn.setBackgroundColor(Color.parseColor(numberColor[idColor.get(color)]));
        }else{
            currentBtn.setBackgroundColor(Color.parseColor(originalColor[idColor.get(color)]));
        }
    }
}
