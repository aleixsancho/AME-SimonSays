package com.example.simonsays;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence);

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
    }
}
