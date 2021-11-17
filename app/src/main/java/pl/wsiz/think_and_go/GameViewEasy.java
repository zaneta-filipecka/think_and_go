package pl.wsiz.think_and_go;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class GameViewEasy extends View {

    //deklaracja zmiennych - parametry labiryntu
    private Komorka[][] komorki;
    private static final int liczbaKolumn = 5, liczbaWierszy = 8;
    private static final float grubosc_scian = 5;

    //deklaracja zmiennych - malowanie labiryntu
    private float rozmiarKomorki, marginesPoziom, marginesPion;
    private Paint sciana;

    //deklaracja zmiennych - zmienna do losowania sąsiada
    private Random los;

    //deklaracja zmiennych - gracz i wyjście
    private Komorka gracz, wyjscie;
    private Paint zawodnik, koniecPoziomu;

    public int wynik = 0;

    //deklaracja kierunków
    private enum Kierunek{
        gora, dol, prawo, lewo      //enum to typ danych dla samodefiniujących się stałych
    }

    Context context;

    //konstruktor klasy GameViewEasy
    public GameViewEasy(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //tworzenie obiektu ścian labiryntu
        sciana = new Paint();
        sciana.setColor(Color.BLACK); //kolor labiryntu
        sciana.setStrokeWidth(grubosc_scian);

        //tworzenie obiektu gracza
        zawodnik = new Paint();
        zawodnik.setColor(Color.RED);

        //tworzenie obiektu wyjścia
        koniecPoziomu = new Paint();
        koniecPoziomu.setColor(Color.BLACK);

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

        //określenie pozycji początkowej dla gracza i wyścia
        gracz = komorki[0][0];
        wyjscie = komorki [liczbaKolumn-1][liczbaWierszy-1];

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

        //margines dla gracza i wyjścia
        float margines = rozmiarKomorki/2;

        //wyświetlanie gracza i wyjścia
        canvas.drawCircle(gracz.kolumna*rozmiarKomorki+margines,gracz.wiersz*rozmiarKomorki+margines, (rozmiarKomorki/2)-5, zawodnik);
        canvas.drawCircle(wyjscie.kolumna*rozmiarKomorki+margines,wyjscie.wiersz*rozmiarKomorki+margines, (rozmiarKomorki/2)-5, koniecPoziomu);
    }

    //metoda sprawdzająca, czy gracz doszedł do końca planszy
    private void sprawdzWyjscie(){
        GameActivityEasy.setWyn("Wynik:" + wynik);
        if(gracz == wyjscie){
            wynik = wynik + 10;
            stworzLabirynt();
            GameActivityEasy.setWyn("Wynik:" + wynik);
        }
    }

    private void przesunGracza(Kierunek kierunek){
        switch (kierunek){
            case gora:
                //jeśli nie ma górnej ściany, to idzmy w górę
                if(!gracz.gornaSciana)
                    gracz = komorki[gracz.kolumna][gracz.wiersz-1];
                break;
            case dol:
                //jeśli nie ma dolnej ściany, to idziemy w dół
                if(!gracz.dolnaSciana)
                    gracz = komorki[gracz.kolumna][gracz.wiersz+1];
                break;
            case prawo:
                //jeśli nie ma prawej ściany, to idziemy w prawo
                if(!gracz.prawaSciana)
                    gracz = komorki[gracz.kolumna+1][gracz.wiersz];
                break;
            case lewo:
                //jeśli nie ma lewej ściany, to idziemy w lewo
                if(!gracz.lewaSciana)
                    gracz = komorki[gracz.kolumna-1][gracz.wiersz];
                break;
        }
        sprawdzWyjscie();
        invalidate();   //wywołuje metodę onDraw najszybciej jak to możliwe
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //żeby ACTION_MOVE mogło zadziałać, to najpierw trzeba dać ACTION_DOWN, bo ACTION_MOVE jest częścią większej struktury zaczynającej się od ACTION_DOWN. Jak nie ma ACTION_DOWN, to nie działa ACTION_MOVE
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            return true;
        }

        if(event.getAction() == MotionEvent.ACTION_MOVE){
            float x = event.getX();
            float y = event.getY();

            float srodekGraczaX = marginesPoziom + (gracz.kolumna+0.5f) * rozmiarKomorki;
            float srodekGraczaY = marginesPion + (gracz.wiersz+0.5f) * rozmiarKomorki;

            //obliczanie różnic pomiędzy pozycją gracza a pozycją zdarzenia
            float kierunekX = x - srodekGraczaX;
            float kierunekY = y - srodekGraczaY;

            //obliczanie wartości bezwzględnych dla różnic w pozycjach
            float bezDlaX = Math.abs(kierunekX);
            float bezDlaY = Math.abs(kierunekY);

            //gracz porusza się jeśli różnica bezwzględna jest większa niż rozmiar komórki
            if(bezDlaX > rozmiarKomorki || bezDlaY > rozmiarKomorki){
                if(bezDlaX > bezDlaY){
                    //poruszamy się w kierunku X
                    if(kierunekX > 0){
                        //gracz idzie w prawo
                        przesunGracza(Kierunek.prawo);
                    }
                    else{
                        //gracz idzie w lewo
                        przesunGracza(Kierunek.lewo);
                    }
                }
                else{
                    //poruszamy się w kierunku Y
                    if(kierunekY > 0){
                        //gracz idzie w dół
                        przesunGracza(Kierunek.dol);
                    }
                    else{
                        //gracz idzie do góry
                        przesunGracza(Kierunek.gora);
                    }
                }
            }
            return true;
        }
        return super.onTouchEvent(event);
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
