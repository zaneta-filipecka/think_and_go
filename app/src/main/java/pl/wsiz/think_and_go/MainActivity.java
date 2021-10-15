package pl.wsiz.think_and_go;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //zmienne
    private Button graj, wyniki, wyjście;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

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
                // Stworzenie obiektu AlertDialog i zwrócenie
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        wyniki = (Button)findViewById(R.id.wyniki);
        wyniki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, ScoreActivity.class);
                startActivity(i);
            }
        });

        wyjście = (Button)findViewById(R.id.wyjście);
        wyjście.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

