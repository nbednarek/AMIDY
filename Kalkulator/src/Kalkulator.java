import java.util.Scanner;

class Kwadrat{                  //klasa Kwadrat zawierajaca zmienna bok
    private double bok;

    public Kwadrat(double bok){     //konstruktor
        this.bok = bok;
    }

    public double kwadratPole(){  //metoda obliczania pola dla kwadratu
        return bok * bok;
    }
}

class Prostokat{        //klasa prostokat ze zmiennymi
    private double dlugosc;
    private double szerokosc;

    public Prostokat(double dlugosc, double szerokosc){  //konstruktor
        this.dlugosc=dlugosc;
        this.szerokosc=szerokosc;
    }

    public double prostokatPole(){   //metoda obliczania pola dla prostokata
        return dlugosc * szerokosc;
    }
}

class Trojkat{      //klasa trojkat ze zmiennymi
    private double podstawa;
    private double wysokosc;

    public Trojkat(double podstawa, double wysokosc){  //konstruktor
        this.podstawa = podstawa;
        this.wysokosc = wysokosc;
    }

    public double trojkatPole(){  //obliczanie pola dla trojkata
        return podstawa * wysokosc * 0.5; // mnożenie przez 0.5, ponieważ jest to to samo co podzielenie na 2
    }
}

public class Kalkulator {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);  //scanner do odczytu danych z konsoli

        while(true){                      //wyswietlanie menu opcji w petli
            System.out.println("\nWybierz figurę do obliczeń:");
            System.out.println(" 1. Kwadrat");
            System.out.println(" 2. Prostokąt ");
            System.out.println(" 3. Trójkąt");
            System.out.println(" 4. Wyjście");

            System.out.println("Twój wybór: ");
            int wybor = scanner.nextInt();          //przypisanie wyboru z konsoli do zmiennej wybor

            if(wybor==4){                       //jezeli wybor to 4 konczy sie program
                System.out.println("Dziękujemy za skorzystanie z kalkulatora! Do widzenia");
                break;
            }

            double pole = 0.0;  //poczatkowa wartosc pola
            switch(wybor){
                case 1:                 //podawanie bokow dla kwadratu i obliczanie pola
                    System.out.println("Wybrano kwadrat");
                    System.out.println("Podaj długość boku kwadratu: ");
                    double bokKwadratu = scanner.nextDouble();
                    Kwadrat kwadrat = new Kwadrat(bokKwadratu);
                    pole = kwadrat.kwadratPole();
                    break;

                case 2:             //podawanie dlugosci i szerokosci dla prostokata i obliczanie pola
                    System.out.println("Wybrano prostokąt");
                    System.out.println("Podaj długość prostokąta: ");
                    double dlugoscProstokata = scanner.nextDouble();
                    System.out.println("Podaj szerokość prostokąta: ");
                    double szerokoscProstokata = scanner.nextDouble();
                    Prostokat prostokat = new Prostokat(dlugoscProstokata, szerokoscProstokata);
                    pole = prostokat.prostokatPole();
                    break;

                case 3:            //podawanie wysokosci i podstawy dla trojkata i obliczanie pola
                    System.out.println("Wybrano trójkąt");
                    System.out.println("Podaj wysokość trójkąta :");
                    double wysokoscTrojkata = scanner.nextDouble();
                    System.out.println("Podaj dlugosc podstawy: ");
                    double podstawaTrojkata = scanner.nextDouble();
                    Trojkat trojkat = new Trojkat(wysokoscTrojkata,podstawaTrojkata);
                    pole = trojkat.trojkatPole();
                    break;

                default:
                    System.out.println("Wybrano nieistniejącą opcję. Spróbuj wybrać ponownie");
                   continue;  // pomija reszte kodu ponizej
            }

            System.out.println("Pole powierzchni wybranej figury wynosi: " + pole);

        }

        scanner.close();


    }
}