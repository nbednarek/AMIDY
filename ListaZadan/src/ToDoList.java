import java.util.ArrayList;
import java.util.Scanner;
class Zadanie {
    String nazwa;
    String opis;        //przypisanie zmiennych
    boolean ukonczone;

    public Zadanie(String nazwa, String opis) {
        this.nazwa = nazwa;            //obiekt klasy
        this.opis = opis;
        this.ukonczone = false;
    }

    public String toString(){
        if(ukonczone){
            return "[x] " + nazwa + ": " + opis; //metoda zwraca [x] przed nazwa gdy zadanie jest ukonczone
        }else{
            return "[ ] " + nazwa + ": " + opis; // gdy nie to [ ]
        }
    }
}

public class ToDoList {
    public static void main(String[] args) {
        ToDoList toDoList = new ToDoList();
        for(;;){
            toDoList.wejscie();  //nieskonczona petla do wyswietlania opcji
        }
    }
    Scanner scanner = new Scanner(System.in);  //odczyt danych z konsoli
    ArrayList<Zadanie> zadania = new ArrayList<>(); //pusta lista typu ArrayList do przechowywania zadan

    void wejscie() {
        System.out.println("-----------------------");
        System.out.println("  X   TO DO LIST   X  ");
        System.out.println("-----------------------");
        System.out.println("1. Dodaj nowe zadanie");                //opcje wyswietlane na poczatku
        System.out.println("2. Oznacz zadanie jako zakończone");
        System.out.println("3. Usuń zadanie");
        System.out.println("4. Wyświetl listę zadań");
        System.out.println("5. Wyjście");
        System.out.println("-----------------------");

        int opcje = scanner.nextInt();  //liczba z konsoli jako int o nazwie opcje
        scanner.nextLine();

        switch (opcje) {
            case 1:
                dodajZadanie();
                break;
            case 2:
                zaznaczUkonczone();
                break;                  //w zaleznosci od ,,opcje" bedzie wykonywalo metode
            case 3:
                usunZadanie();
                break;
            case 4:
                wyswietlListe();
                break;
            case 5:
                wyjscie();
                break;
        }
    }
    void dodajZadanie(){
        System.out.println("-----------------------");
        System.out.println("Podaj nazwę zadania: ");
        String nazwa = scanner.nextLine();
        System.out.println("Podaj opis zadania: ");       //wczytanie danych z konsoli do nazwy i opisu zadania
        String opis = scanner.nextLine();

        Zadanie noweZadanie = new Zadanie(nazwa, opis);  //obiekt ,,Zadanie"
        zadania.add(noweZadanie);                           //dodanie do listy
        System.out.println("Zadanie \"" + nazwa + "\" zostało dodane do listy."); //komunikat o dodaniu do listy
        System.out.println("-----------------------");
    }

    void zaznaczUkonczone(){
        System.out.println("Podaj numer zadania do oznaczenia jako zakończone: ");
        int indexZadania = scanner.nextInt();
        scanner.nextLine();                         //wczytanie liczby do zmiennej indexu zadania po podaniu przez uzytkownika

        if(indexZadania >= 1 && indexZadania <= zadania.size()){ //sprawdzenie czy numer miesci sie w zakresie 1-ilosc zadan
            Zadanie zadanie = zadania.get(indexZadania - 1);
            zadanie.ukonczone = true;
            System.out.println("Zadanie \"" + zadanie.nazwa + "\" zostało oznaczone jako zakończone."); //komunikat o zakonczeniu
        }else{
            System.out.println("Podano nieprawidłowy numer zadania."); //jezeli nie ma takiego numeru zadania
        }

        System.out.println("-----------------------");
    }

    void usunZadanie(){
        System.out.println("Podaj numer zadania do usunięcia: "); //metoda usuwania zadania, prosba o podanie numeru zadania
        int indexZadania = scanner.nextInt();
        scanner.nextLine();

        if(indexZadania >= 1 && indexZadania <= zadania.size()){  //sprawdza czy numer zadania miesci sie w zakresie
            Zadanie usunieteZadanie = zadania.remove(indexZadania - 1);  //jezeli tak to program usuwa zadanie z listy
            System.out.println("Zadanie \"" + usunieteZadanie.nazwa + "\" zostało pomyślnie usunięte z listy.");
        }else{
            System.out.println("Podano nieprawidłowy numer zadania.");
        }
        System.out.println("-----------------------");
    }

    void wyswietlListe(){
        System.out.println("Lista zadań:");  //naglowek listy zadan

        if(zadania.isEmpty()){
            System.out.println("Brak zadań do wykonania!"); //jezeli lista pusta to pokaze sie komunikat o braku zadan
        }else{
            for(int i = 0; i < zadania.size(); i++){
                System.out.println((i + 1) + ". " + zadania.get(i)); //pobieranie zadania z listy i wyswietlanie calosci
            }
        }
        System.out.println("-----------------------");
    }
    void wyjscie() {
        System.out.println("Koniec programu."); //metoda zakonczenia programu
        System.out.println("-----------------------");
        System.exit(0); //natychmiastowe zakonczenie programu
    }
}
