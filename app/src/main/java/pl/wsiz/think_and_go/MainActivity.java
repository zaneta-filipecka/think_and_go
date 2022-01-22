package pl.wsiz.think_and_go;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //zmienne
    private Button graj, wyniki, wyjście;

    Wyniki bazaDanych;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        bazaDanych = new Wyniki(this);

        graj = (Button)findViewById(R.id.graj);
        graj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.select_difficulty)
                        .setItems(R.array.level, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0) {
                                    Intent in = new Intent();
                                    in.setClass(MainActivity.this, GameActivityEasy.class);
                                    startActivity(in);
                                }
                                else if(which==1) {
                                    Intent inten = new Intent();
                                    inten.setClass(MainActivity.this, GameActivityMedium.class);
                                    startActivity(inten);
                                }
                                else if(which==2){
                                    Intent inte = new Intent();
                                    inte.setClass(MainActivity.this, GameActivityHard.class);
                                    startActivity(inte);
                                }
                                else if(which==3){
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this, GameActivityExpert.class);
                                    startActivity(intent);
                                }
                            }
                        });
                // Stworzenie obiektu AlertDialog i zwrócenie go
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        wyniki = (Button)findViewById(R.id.wyniki);

        wyjście = (Button)findViewById(R.id.wyjście);
        wyjście.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        zobaczDane();
    }

    public void zobaczDane(){
        wyniki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = bazaDanych.getAllData();
                if(res.getCount() == 0){
                    pokazWiadomosc("Błąd", "Brak danych");
                    return;
                }
                else{
                    StringBuffer buffer = new StringBuffer();
                    while(res.moveToNext()){
                        buffer.append("Id: " + res.getString(0).toString() + " \t");
                        buffer.append("Gracz: " + res.getString(1).toString() +" \t");
                        buffer.append(res.getString(2).toString() +" \t\n");
                    }

                    pokazWiadomosc("Wyniki",buffer.toString());
                }
            }
        });
    }

    public void pokazWiadomosc(String tytul, String wiadomosc){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(tytul);
        builder.setMessage(wiadomosc);
        builder.show();
    }
}

