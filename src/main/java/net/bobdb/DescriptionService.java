package net.bobdb;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class DescriptionService {


    public static final String KEY = System.getenv("OPENAPI_KEY");
    public static final String URL = "https://api.openai.com/v1/chat/completions";


    static String getDescription(Product p) throws IOException, InterruptedException {

        String prompt = "Describe a " + p.year + " " + p.manufacturer + " " + p.name + " in less than 100 words";

        List<GPTMessage> messages = new ArrayList<GPTMessage>();
        messages.add(new GPTMessage("user", prompt));
        GPTRequest g = new GPTRequest("gpt-4o", messages, 0.7 );

        Gson gson = new Gson();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL))
                .header("Content-Type","application/json")
                .header("Authorization", "Bearer " + KEY)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(g)))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode()==200) {
            GPTResponse gptResponse = gson.fromJson(response.body(),GPTResponse.class);
            return gptResponse.getChoices()[0].message.content();
        } else {
            System.out.println("Something weird:" +  response.statusCode() + " " + response.body());
            return "";
        }

    }
}
