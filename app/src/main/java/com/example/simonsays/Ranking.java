package com.example.simonsays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Ranking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        //Define all interface object needed.
        TextView[] users = {findViewById(R.id.user1), findViewById(R.id.user2), findViewById(R.id.user3), findViewById(R.id.user4), findViewById(R.id.user5)};
        TextView[] scores = {findViewById(R.id.score1), findViewById(R.id.score2), findViewById(R.id.score3), findViewById(R.id.score4), findViewById(R.id.score5)};
        Button restart = findViewById(R.id.restartBtn);
        TextView myUsernameButton = findViewById(R.id.myUser);
        TextView myScoreButton = findViewById(R.id.myScore);
        TextView myTitleText = findViewById(R.id.titleText);

        //Get parameters from intent.
        ArrayList<String> topPlayers = this.getIntent().getExtras().getStringArrayList("topFive");
        String myName = this.getIntent().getExtras().getString("myName");
        String myTitle = this.getIntent().getExtras().getString("myTitle");
        int myScore = this.getIntent().getExtras().getInt("myScore");

        //Set title name as GAME OVER or YOU WIN.
        myTitleText.setText(myTitle);
        myUsernameButton.setText(myName);
        if (myTitle.contains("WIN")) {
            myTitleText.setTextColor(Color.GREEN);
        } else {
            myTitleText.setTextColor(Color.RED);
        }
        myScoreButton.setText(String.valueOf(myScore));

        //Create a list of players.
        ArrayList<Player> playerList = new ArrayList<>();
        for (String s : topPlayers) {
            String[] name = s.split(",");
            playerList.add(new Player(name[0], Integer.parseInt(name[1])));
        }

        //Put username and score at the TOP5 ranking.
        int i = 0;
        for (Player p : playerList) {
            users[i].setText(p.getUsername());
            scores[i].setText(String.valueOf(p.getScore()));
            i = i + 1;
        }

        //When Restart button is clicked go to main activity.
        restart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Ranking.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
