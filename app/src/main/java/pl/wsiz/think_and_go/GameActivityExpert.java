package pl.wsiz.think_and_go;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivityExpert extends AppCompatActivity {

    public static TextView wyn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_expert);

        wyn = (TextView)findViewById(R.id.wynik);
    }

    public static void setWyn(String wyn) {
        GameActivityExpert.wyn.setText(wyn);
    }
}
