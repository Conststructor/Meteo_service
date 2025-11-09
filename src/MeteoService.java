import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MeteoService {
    private static final String head = "X-Yandex-Weather-Key";
    private static final String API_KEY = "c0850acb-fe81-4cfd-abbf-a32266ee36a6";
    private static String API_URL = "https://api.weather.yandex.ru/v2/forecast";

    private OkHttpClient client = new OkHttpClient();

    public void sendRequset(String lat, String lon, int days) {
        try {
            // создание запроса
            API_URL = API_URL + "?lat=" + lat + "&lon=" + lon + "&limit=" + Integer.toString(days);
            Request request = new Request.Builder().url(API_URL).get().header(head, API_KEY).build();
            Response response = client.newCall(request).execute();
            // вовзрат полного JSON ответа
            if (response.isSuccessful()) {
                String answer = response.body().string();
                System.out.println("Полный ответ сервиса \"как есть\" :\n" + answer);
                // выделение из пролного ответа отдельно температуры
                int StartIndex = answer.indexOf("temp") + 6;
                int EndIndex = StartIndex + 2;
                String Temperature = answer.substring(StartIndex, EndIndex).replace(',', ' ').trim();
                System.out.println("Температура сейчас : " + Temperature + " C");

                // вычисление средней температуры по прогнозу
                String buffer = answer;
                int midTemp = 0;

                while (buffer.contains("{\"day\":")) {
                    StartIndex = buffer.indexOf("\"day\"");
                    buffer = buffer.substring(StartIndex);          //обрезаем строку до первого блока day
                    StartIndex = buffer.indexOf("temp_avg");
                    buffer = buffer.substring(StartIndex);          //обрезаем строку до первого блока temp_avg
                    StartIndex = buffer.indexOf("temp_avg") + 10;
                    EndIndex = StartIndex + 2;
                    midTemp += Integer.parseInt(buffer.substring(StartIndex, EndIndex).replace(',', ' ').trim());
                }
                midTemp = midTemp / days;
                System.out.println("Средняя дневная температура на " + days + " суток : " + midTemp + " C");
            } else
                System.out.println("Error : " + response.code());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
