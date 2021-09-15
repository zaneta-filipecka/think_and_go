package pl.wsiz.think_and_go;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class GameView extends View {

    //deklaracja zmiennych - parametry labiryntu
    private Komorka[][] komorki;
    private static final int liczbaKolumn = 11, liczbaWierszy = 20;
    private static final float grubosc_scian = 5;

    //deklaracja zmiennych - malowanie labiryntu
    private float rozmiarKomorki, marginesPoziom, marginesPion;
    private Paint sciana;

    //konstruktor klasy GameView
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //tworzenie obiektu ścian labiryntu
        sciana = new Paint();
        sciana.setColor(Color.BLACK); //kolor labiryntu
        sciana.setStrokeWidth(grubosc_scian);

        stworzLabirynt();
    }

    private void stworzLabirynt() {
        //określenie wielkości labiryntu
        komorki = new Komorka[liczbaKolumn][liczbaWierszy];

        for (int x=0; x<liczbaKolumn; x++) {
            for (int y=0; y<liczbaWierszy; y++) {
                komorki[x][y] = new Komorka(x, y);      //tworzenie nowego obiektu dla każdej komórki
            }
        }
    }

    //obliczenia wielkości labiryntu


    @Override
    protected void onDraw(Canvas canvas) {

        //pobranie wymiarów okna
        int szerokosc = getWidth();
        int wysokosc = getHeight();

        //obliczanie rozmiaru komórki na podstawie pobranych wymiarów okna
        if(szerokosc/wysokosc < liczbaKolumn/liczbaWierszy)
        {
            rozmiarKomorki = szerokosc/(liczbaKolumn+3);
        }
        else {
            rozmiarKomorki = wysokosc/(liczbaWierszy+3);
        }

        //rozmiar marginesów
        marginesPoziom = (szerokosc-liczbaKolumn*rozmiarKomorki)/2;
        marginesPion = (wysokosc-liczbaWierszy*rozmiarKomorki)/2;

        canvas.translate(marginesPoziom, marginesPion); //zamiast dodawania za każdym razem marginesu do parametrów rysowanej linii

        //rysowanie ścian labiryntu
        for (int x=0; x<liczbaKolumn; x++) {
            for (int y=0; y<liczbaWierszy; y++) {
                if(komorki[x][y].gornaSciana){
                    canvas.drawLine(x*rozmiarKomorki, y*rozmiarKomorki, (x+1)*rozmiarKomorki,y*rozmiarKomorki, sciana);
                }
                if(komorki[x][y].dolnaSciana){
                    canvas.drawLine(x*rozmiarKomorki, (y+1)*rozmiarKomorki, (x+1)*rozmiarKomorki,(y+1)*rozmiarKomorki, sciana);
                }
                if(komorki[x][y].prawaSciana){
                    canvas.drawLine((x+1)*rozmiarKomorki, y*rozmiarKomorki, (x+1)*rozmiarKomorki,(y+1)*rozmiarKomorki, sciana);
                }
                if(komorki[x][y].lewaSciana){
                    canvas.drawLine(x*rozmiarKomorki, y*rozmiarKomorki, x*rozmiarKomorki,(y+1)*rozmiarKomorki, sciana);
                }
            }
        }
    }

    private class Komorka{
        //każda komórka posiada 4 ściany
        boolean gornaSciana = true;
        boolean dolnaSciana = true;
        boolean prawaSciana = true;
        boolean lewaSciana = true;

        //każda komórka ma swoją pozycję (określoną kolumnę i wiersz)
        int kolumna, wiersz;

        //konstruktor klasy Komorka
        public Komorka (int kolumna, int wiersz) {
            this.kolumna = kolumna;
            this.wiersz = wiersz;
        }
    }
}
