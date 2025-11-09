public class Main {
    public static void main(String[] args) {

        MeteoService meteoService = new MeteoService();
        System.out.println("Start meteo service program!");
        meteoService.sendRequset("55.753883", "37.621626", 5);

    }
}