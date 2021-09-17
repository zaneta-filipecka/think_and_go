package pl.wsiz.think_and_go;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class GameView extends View {

    //deklaracja zmiennych - parametry labiryntu
    private Komorka[][] komorki;
    private static final int liczbaKolumn = 11, liczbaWierszy = 20;
    private static final float grubosc_scian = 5;

    //deklaracja zmiennych - malowanie labiryntu
    private float rozmiarKomorki, marginesPoziom, marginesPion;
    private Paint sciana;

    //Zmienna do losowania sąsiada
    private Random los;

    //konstruktor klasy GameView
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //tworzenie obiektu ścian labiryntu
        sciana = new Paint();
        sciana.setColor(Color.BLACK); //kolor labiryntu
        sciana.setStrokeWidth(grubosc_scian);

        //inicjalizacja zmiennej do losowania sąsiadów
        los = new Random();

        stworzLabirynt();
    }

    private Komorka wezSasiada(Komorka komorka){
        ArrayList<Komorka> sasiedzi = new ArrayList<>();

        //lewy sąsiad
        if(komorka.kolumna>0) {
            if (!komorki[komorka.kolumna - 1][komorka.wiersz].odwiedzona) {
                sasiedzi.add(komorki[komorka.kolumna - 1][komorka.wiersz]);
            }
        }
        //prawy sąsiad
        if(komorka.kolumna<liczbaKolumn-1) {
            if (!komorki[komorka.kolumna + 1][komorka.wiersz].odwiedzona) {
                sasiedzi.add(komorki[komorka.kolumna + 1][komorka.wiersz]);
            }
        }
        //górny sąsiad
        if(komorka.wiersz>0) {
            if (!komorki[komorka.kolumna][komorka.wiersz - 1].odwiedzona) {
                sasiedzi.add(komorki[komorka.kolumna][komorka.wiersz - 1]);
            }
        }
        //dolny sąsiad
        if(komorka.wiersz<liczbaWierszy-1)
        if (!komorki[komorka.kolumna][komorka.wiersz + 1].odwiedzona){
            sasiedzi.add(komorki[komorka.kolumna][komorka.wiersz + 1]);
        }

        //losowe wybieranie sąsiada (obliczanie losowego indeksu)
        if(sasiedzi.size()>0) {
            int indeks = los.nextInt(sasiedzi.size()); //jeśli mamy 3 nieodwiedzonych sąsiadów, to indeks może mieć wartość 0, 1 albo 2
            return sasiedzi.get(indeks);
        }
        else {
            return null;
        }
    }

    //usuwanie ścian pomiędzy sąsiadami

    private void usunSciane(Komorka obecnaKom, Komorka nastepnaKom) {

        //górny sąsiad
        if(obecnaKom.kolumna == nastepnaKom.kolumna && obecnaKom.wiersz == nastepnaKom.wiersz+1){
            obecnaKom.gornaSciana = false;
            nastepnaKom.dolnaSciana = false;
        }
        //dolny sąsiad
        if(obecnaKom.kolumna == nastepnaKom.kolumna && obecnaKom.wiersz == nastepnaKom.wiersz-1){
            obecnaKom.dolnaSciana = false;
            nastepnaKom.gornaSciana = false;
        }
        //prawy sąsiad
        if(obecnaKom.kolumna == nastepnaKom.kolumna - 1 && obecnaKom.wiersz == nastepnaKom.wiersz){
            obecnaKom.prawaSciana = false;
            nastepnaKom.lewaSciana = false;
        }
        //lewy sąsiad
        if(obecnaKom.kolumna == nastepnaKom.kolumna + 1 && obecnaKom.wiersz == nastepnaKom.wiersz){
            obecnaKom.lewaSciana = false;
            nastepnaKom.prawaSciana = false;
        }
    }

    private void stworzLabirynt() {

        //budowanie stosu na odwiedzone komórki
        Stack<Komorka> stos = new Stack<>();
        Komorka obecnaKomorka, nastepnaKomorka;

        //określenie wielkości labiryntu
        komorki = new Komorka[liczbaKolumn][liczbaWierszy];

        for (int x=0; x<liczbaKolumn; x++) {
            for (int y=0; y<liczbaWierszy; y++) {
                komorki[x][y] = new Komorka(x, y);      //tworzenie nowego obiektu dla każdej komórki
            }
        }

        //ustawianie współrzędnych dla pierwszej komórki jako [0][0]
        obecnaKomorka = komorki[0][0];

        //wykonywanie czynności w pętli, aż do uzyskania całego labiryntu
        do {
            //znajdowanie "nieodwiedzonych" komórek
            nastepnaKomorka = wezSasiada(obecnaKomorka);
            obecnaKomorka.odwiedzona = true;

            //sprawdzenie czy komorka ma sąsiada
            if (nastepnaKomorka != null) {
                usunSciane(obecnaKomorka, nastepnaKomorka);
                stos.push(obecnaKomorka);
                obecnaKomorka = nastepnaKomorka;
                obecnaKomorka.odwiedzona = true;
            } else {
                obecnaKomorka = stos.pop(); //jeśli nie ma sąsiada, wracamy do poprzedniej komórki, która ma sąsiada, zdejmując współrzędne ze stosu
            }
        } while(!stos.empty());
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

        //przypisanie do komórki, czy została odwiedzona, czy nieodwiedzona
        boolean odwiedzona = false;

        //każda komórka ma swoją pozycję (określoną kolumnę i wiersz)
        int kolumna, wiersz;

        //konstruktor klasy Komorka
        public Komorka (int kolumna, int wiersz) {
            this.kolumna = kolumna;
            this.wiersz = wiersz;
        }
    }
}
