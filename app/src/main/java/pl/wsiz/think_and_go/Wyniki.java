package pl.wsiz.think_and_go;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Wyniki extends SQLiteOpenHelper {

    public static final String nazwa_bazy = "Wyniki.db";
    public static final String nazwa_tablicy = "Tablica_wyników";
    public static final String kolumna_id = "Id";
    public static final String kolumna_gracz = "Gracz";
    public static final String kolumna_wynik = "Wynik";

    public Wyniki(@Nullable Context context) {
        super(context, nazwa_bazy, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + nazwa_tablicy + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, Gracz TEXT, Wynik INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db. execSQL("DROP TABLE IF EXISTS " + nazwa_tablicy);
        onCreate(db);
    }

    public boolean insertData(String gracz, String wyniki){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(kolumna_gracz, gracz);
        cv.put(kolumna_wynik, wyniki);
        long result = db.insert(nazwa_tablicy, null, cv);

        if(result==-1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor rezultat = db.rawQuery("select * from " + nazwa_tablicy, null);
        return rezultat;
    }
}
