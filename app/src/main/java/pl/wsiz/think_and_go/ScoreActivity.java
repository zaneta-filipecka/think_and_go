package pl.wsiz.think_and_go;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.room.Room;

//import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_score);
    }

   /* Wyniki.AppDatabase db = Room.databaseBuilder(getApplicationContext(),
            Wyniki.AppDatabase.class, "wyniki").build();
    Wyniki.UserDao userDao = db.userDao();
    List<Wyniki> users = userDao.getAll();*/

}
