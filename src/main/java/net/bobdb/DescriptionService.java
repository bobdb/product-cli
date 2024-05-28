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

    public static final String TOKEN = "";
    public static final String URL = "";


    static String getDescription(Product p) throws IOException, InterruptedException {
//        String prompt = "Describe a " + p.year + " " + p.manufacturer + " " + p.name;
//        List<GPTMessage> messages = new ArrayList<GPTMessage>();
//        messages.add(new GPTMessage("user", prompt));
//        GPTRequest g = new GPTRequest("gpt-3.5-turbo", messages, 0.7 );
//
//        Gson gson = new Gson();
//
//        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL))
//                .header("Content-Type","application/json")
//                .header("Authorization", "Bearer " + TOKEN)
//                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(g)))
//                .build();
//
//        HttpClient client = HttpClient.newHttpClient();
//        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        if (response.statusCode()==200) {
//            GPTResponse gptResponse = gson.fromJson(response.body(),GPTResponse.class);
//            System.out.println(gptResponse);
//        } else {
//            System.out.println(response.statusCode());
//        }
//        return response.body();
        return  "{description}";
    }
}
