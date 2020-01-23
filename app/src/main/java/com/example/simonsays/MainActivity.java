package com.example.simonsays;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Random;
import org.jetbrains.annotations.Nullable;

public final class MainActivity extends AppCompatActivity {

    private ArrayList<String> allColors = new ArrayList<String>();
    private Integer randomValue;
    private Integer firstValue;
    private int level = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Define needed variables.
        Random random = new Random();
        firstValue = random.nextInt(4);
        String[] fourColors = new String[]{"Green", "Yellow", "Blue", "Red"};
        allColors.add(fourColors[firstValue]);
        Button start = findViewById(R.id.startBtn);

        //Colors Simon Says.
        for (int i=0; i < level; i++){
            randomValue = random.nextInt(4);
            allColors.add(fourColors[randomValue]);
        }

        start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, Sequence.class);
                intent.putStringArrayListExtra("colors", allColors);
                intent.putExtra("count", 0);
                intent.putExtra("score", 0);
                startActivity(intent);
            }
        });
    }
}
