import java.util.concurrent.TimeUnit;

class Termostat {
    private int aktualnaTemperatura = 16;
    private int ustawionaTemperatura = 22;              //przypisanie zmiennych
    private boolean ogrzewanieWlaczone = false;
    private boolean chlodzenieWlaczone = false;

    public void wlaczOgrzewanie() {
        if (!ogrzewanieWlaczone) {
            ogrzewanieWlaczone = true;
            chlodzenieWlaczone = false;   //włącza ogrzewanie tylko jeśli nie jest już włączone i wyłącza chłodzenie
            System.out.println("Włączono ogrzewanie."); //wypisuje komunikat "Włączono ogrzewanie." na konsolę.
            System.out.println("____________________");
        }
    }

    public void wlaczChlodzenie() {
        if (!chlodzenieWlaczone) {
            chlodzenieWlaczone = true;
            ogrzewanieWlaczone = false; //włącza chłodzenie tylko jeśli nie jest już włączone i wyłącza ogrzewanie
            System.out.println("Włączono chłodzenie."); //wypisuje komunikat "Włączono chłodzenie." na konsolę.
            System.out.println("____________________");
        }
    }

    public void wylaczOgrzewanie() {
        if (ogrzewanieWlaczone) {
            ogrzewanieWlaczone = false;
            System.out.println("____________________");  //wyłącza ogrzewanie jeśli ogrzewanie jest aktualnie włączone.
            System.out.println("Wyłączono ogrzewanie."); //komunikat na konsolę
        }
    }

    public void wylaczChlodzenie() {
        if (chlodzenieWlaczone) {
            chlodzenieWlaczone = false;
            System.out.println("____________________"); //wyłącza chłodzenie jeśli chłodzenie jest włączone
            System.out.println("Wyłączono chłodzenie.");//komunikat na konsolę
        }
    }

    public void sprawdzTemperature() throws InterruptedException {
        //Jeśli aktualna temperatura jest niższa niż ustawiona przez użytkownika,
        // to zostaje wyświetlona informacja o temperaturze i włączone jest ogrzewanie.
        if (aktualnaTemperatura < ustawionaTemperatura) {
            System.out.println("___________________________________________________________");
            System.out.println("Aktualna temperatura wynosi: "+aktualnaTemperatura+ "°C przed ogrzewaniem");
            System.out.println("Pożądana przez użytkownika temperatura: "+ustawionaTemperatura+"°C");
            System.out.println("___________________________________________________________");
            wlaczOgrzewanie();
            //zwiększa aktualną temperaturę o 1 stopień i wyświetla ją na konsoli,
            // dopóki aktualna temperatura jest mniejsza niż ustawiona temperatura, po czym zostaje wyłączone ogrzewanie.
            while (aktualnaTemperatura < ustawionaTemperatura) {
                aktualnaTemperatura++;
                System.out.println("Aktualna temperatura wynosi: " + aktualnaTemperatura + "°C");
                TimeUnit.SECONDS.sleep(1);
            }
            wylaczOgrzewanie();
            //Jeśli aktualna temperatura jest wyższa niż ustawiona przez użytkownika,
            // to zostaje wyświetlona informacja o temperaturze i włączone jest chłodzenie.
        } else if (aktualnaTemperatura > ustawionaTemperatura) {
            System.out.println("___________________________________________________________");
            System.out.println("Aktualna temperatura wynosi: "+aktualnaTemperatura+ "°C przed chlodzeniem");
            System.out.println("Pożądana przez użytkownika temperatura: "+ustawionaTemperatura+"°C");
            System.out.println("___________________________________________________________");
            wlaczChlodzenie();
            //zmniejsza aktualną temperaturę o 1 stopień i wyświetla ją na konsoli,
            // dopóki aktualna temperatura jest większa niż ustawiona temperatura, po czym zostaje wyłączone chlodzenie.
            while (aktualnaTemperatura > ustawionaTemperatura) {
                aktualnaTemperatura--;
                System.out.println("Aktualna temperatura wynosi: " + aktualnaTemperatura + "°C");
                TimeUnit.SECONDS.sleep(1);
            }
            wylaczChlodzenie();
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Termostat termostat = new Termostat();

        for (;;) {  //uzycie nieskonczonej petli, aby dzialalo do momentu odpowiedniej temperatury
            termostat.sprawdzTemperature();
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
