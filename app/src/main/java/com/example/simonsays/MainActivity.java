package com.example.simonsays;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SharedMemory;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Random;
import org.jetbrains.annotations.Nullable;

public final class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ArrayList<String> allColors = new ArrayList<String>();
    private Integer randomValue;
    private Integer firstValue;
    private int level = 4;
    private EditText username;
    private String[] users = {"Level1", "Level2", "Level3", "Level4", "Level5"};
    private String[] fourColors;
    private Random random;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Define needed variables.
        random = new Random();
        firstValue = random.nextInt(4);
        fourColors = new String[]{"Green", "Yellow", "Blue", "Red"};
        allColors.add(fourColors[firstValue]);
        Button start = findViewById(R.id.startBtn);

        username = (EditText)findViewById(R.id.usernameInput);

        Spinner spin = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);

        start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                EditText currentName = findViewById(R.id.usernameInput);
                if(!currentName.getText().toString().equals("")){
                    //Colors Simon Says.
                    for (int i=0; i < level; i++){
                        randomValue = random.nextInt(4);
                        allColors.add(fourColors[randomValue]);
                    }
                    Intent intent = new Intent(MainActivity.this, Sequence.class);
                    intent.putStringArrayListExtra("colors", allColors);
                    intent.putExtra("count", 0);
                    intent.putExtra("score", 0);
                    String provaUser = username.getText().toString();
                    intent.putExtra("username", provaUser);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView) parent.getChildAt(0)).setTextSize(24);
        Toast.makeText(getApplicationContext(), "Selected User: "+users[position] , Toast.LENGTH_SHORT).show();
        level = (position+1) * 4;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

}

