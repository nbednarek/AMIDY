import java.util.Scanner;

class SalaLekcyjna {
    private int numerSali;
    private int pojemnosc; // prywatne zmienne, które przechowują informacje
    private boolean dostepna; //o numerze sali, jej pojemności i dostępności

    public SalaLekcyjna(int numerSali, int pojemnosc) { //konstruktor dla SalaLekcyjna
        this.numerSali = numerSali;
        this.pojemnosc = pojemnosc;
        this.dostepna = true;
    }

    public int getNumerSali() {
        return numerSali;
    }

    public int getPojemnosc() {
        return pojemnosc;
    }

    public boolean salaDostepna() {         //metody, ktore pozwola na pobranie wartosci
        return dostepna;                    //oraz zmienia stan dostepnosci
    }

    public void zarezerwujSale() {
        dostepna = false;
    }

    public void zwolnijSale() {
        dostepna = true;
    }
}

class RezerwacjaSalSzkolnych {
    private static SalaLekcyjna[] listaSal = new SalaLekcyjna[10]; // Przykładowa maksymalna liczba sal
    private static int liczbaSal = 0;                        //aktualna liczba

    //ponizej dodawanie nowej sali oraz sprawdzenie czy nie przekroczono mozliwej ilosci sal
    public static void dodajSale(int numerSali, int pojemnosc) {
        if (liczbaSal < listaSal.length) {
            listaSal[liczbaSal] = new SalaLekcyjna(numerSali, pojemnosc);
            liczbaSal++;
            System.out.println("Nowa sala została dodana: Sala " + numerSali + " (Pojemność: " + pojemnosc + ")");
        } else {
            System.out.println("Dodana sala przekracza maksymalną liczbę sal. Sala nie została dodana.");
        }
    }

    //rezerwacja sali o okreslonej dacie i godzinie, jezeli sala jest dostepna oraz jesli istnieje
    public static void zarezerwujSale(int numerSali, String data, String godzina) {
        for (int i = 0; i < liczbaSal; i++) {
            if (listaSal[i].getNumerSali() == numerSali) {
                if (listaSal[i].salaDostepna()) {
                    listaSal[i].zarezerwujSale();
                    System.out.println("Z powodzeniem zarezerwowano sale " + numerSali + " na " + data + " o godzinie " + godzina);
                } else {
                    System.out.println("Sala " + numerSali + " jest już zajęta w podanym terminie.");
                }
                return;
            }
        }
        System.out.println("Nie znaleziono sali o numerze " + numerSali);
    }

    //sprawdzenie dostepnosci sali o okreslonej godzinie i dacie oraz informuje o zajeciu i dostepnosci
    public static void sprawdzDostepnoscSali(int numerSali, String data, String godzina) {
        for (int i = 0; i < liczbaSal; i++) {
            if (listaSal[i].getNumerSali() == numerSali) {
                if (listaSal[i].salaDostepna()) {
                    System.out.println("Sala " + numerSali + " jest dostępna na " + data + " o godzinie " + godzina);
                } else {
                    System.out.println("Sala " + numerSali + " jest zajęta na " + data + " o godzinie " + godzina);
                }
                return;
            }
        }
        System.out.println("Nie znaleziono sali o numerze " + numerSali);
    }

    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);  //Scanner do odczytu danych z konsoli

        while (true) {
            System.out.println("\n WITAMY W PROGRAMIE SAL LEKCYJNYCH");//wyswietlanie menu opcji w petli
            System.out.println("\nWybierz opcję:");
            System.out.println("1. Dodaj nową salę");
            System.out.println("2. Zarezerwuj salę");
            System.out.println("3. Sprawdź dostępność sali");
            System.out.println("4. Wyjście");

            int opcja = scanner.nextInt();    //przypisanie wyboru do zmiennej opcja po wczytaniu z konsoli

            switch (opcja) {
                case 1:     //dodawanie nowej sali: numer i pojemnosc
                    System.out.println("Podaj numer sali którą chcesz dodać:");
                    int numerSali = scanner.nextInt();
                    System.out.println("Podaj pojemność sali:");
                    int pojemnosc = scanner.nextInt();
                    dodajSale(numerSali, pojemnosc);
                    break;
                case 2:         //rezerwacja sali: podanie numeru, daty i godziny
                    System.out.println("Podaj numer sali którą chcesz zarezerwować:");
                    numerSali = scanner.nextInt();
                    System.out.println("Podaj datę (dd/mm/yyyy):");
                    String data = scanner.next();
                    System.out.println("Podaj godzinę (hh:mm):");
                    String godzina = scanner.next();
                    zarezerwujSale(numerSali, data, godzina);
                    break;
                case 3:     //sprawdzenie sali: podanie numeru, godziny i sali
                    System.out.println("Podaj numer sali ktorej chcesz sprawdzić dostepność:");
                    numerSali = scanner.nextInt();
                    System.out.println("Podaj datę (dd/mm/yyyy):");
                    data = scanner.next();
                    System.out.println("Podaj godzinę (hh:mm):");
                    godzina = scanner.next();
                    sprawdzDostepnoscSali(numerSali, data, godzina);
                    break;
                case 4:             //wyjscie z programu
                    System.out.println("Dziękujemy za skorzystanie z programu. Autor: Natalia Bednarek");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Niepoprawna opcja. Wybierz ponownie.");
            }
        }
    }
}

