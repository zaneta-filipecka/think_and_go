package pl.wsiz.think_and_go;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivityEasy extends AppCompatActivity {

    public static TextView wyn;
    public static Button back;

    Wyniki bazaDanych;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_easy);

        bazaDanych = new Wyniki(this);

        wyn = (TextView)findViewById(R.id.wynik);
        back = (Button)findViewById(R.id.menuEasy);
        AddData();
    }

    public void AddData() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = bazaDanych.insertData("player", wyn.getText().toString());
                if(isInserted = true) {
                    Toast.makeText(GameActivityEasy.this, R.string.add_data, Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    Toast.makeText(GameActivityEasy.this, R.string.not_inserted_data, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    public static void setWyn(String wyn) {
        GameActivityEasy.wyn.setText(wyn);
    }
}
