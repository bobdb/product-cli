package net.bobdb;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class ProductsCLITest {

    CommandLine cmd;
    ProductsCLI app;
    StringWriter sw;

    @BeforeEach
    void startup() {
        app = new ProductsCLI();
        cmd = new CommandLine(app);
        sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));
    }

    @Test
    void testNoArgs() {
        System.out.println("products");
        int exitCode = cmd.execute();
        assertEquals(0, exitCode);
    }

    @Test
    void testPrettyPrint() {
        System.out.println("products -p 1");
        String[] args = new String[]{"-p","1"};
        int exitCode = cmd.execute(args);
        assertEquals(0, exitCode); // it works...just look at console.
    }

    @Test
    void testFindAll() {
        System.out.println("products -a -p");
        String[] args = {"-a", "-p"};
        int exitCode = cmd.execute(args);
        assertEquals(0, exitCode);
        Type listOfProductObject = new TypeToken<ArrayList<Product>>() {}.getType();
        Gson gson = new Gson();
        List<Product> products = gson.fromJson(sw.toString(), listOfProductObject);
        assertEquals(72, products.size());
    }

    @Test
    void testFindN() {
        System.out.println("products -p -l=3");
        String[] args = new String[]{"-p", "-l=3"};
        int exitCode = cmd.execute(args);
        assertEquals(0, exitCode);
    }

    @Test
    void testFindSingle() {
        System.out.println("products 1");
        String[] args = new String[]{"1"};
        int exitCode = cmd.execute(args);
        assertEquals(0,exitCode);
        String expected = """
                        [{"id":1,"name":"Les Paul","description":"","manufacturer":"Gibson","year":"1960","price":"10000.00","quantity":0}]
                """;
        assertEquals(expected.trim(),sw.toString());
    }

    @Test
    void testProductServiceUp() throws IOException, InterruptedException {
        String urlString = "http://localhost:8080/api/products/1";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseString = response.body();
        assertThat(responseString.substring(0,12)).isNotEqualTo("{\"timestamp\"");
    }


    @Test
    void testFindWithoutHittingInventoryService() {
        System.out.println("products -i=false 1");
        String[] args = new String[]{"-i=false","1"};
        int exitCode = cmd.execute(args);
        assertEquals(0,exitCode);
        String expected = """
                        [{"id":1,"name":"Les Paul","description":"","manufacturer":"Gibson","year":"1960","price":"10000.00"}]
                   """;
        assertEquals(expected.trim(),sw.toString());
    }

    @Test
    void testFindAndGetDescription() {

        // Item 1 already has a description
        System.out.println("products -d 1");
        String[] args = new String[]{"-d","1"};
        int exitCode = cmd.execute(args);
        assertEquals(0,exitCode);
        Gson gson = new Gson();
        Type listOfProductObject = new TypeToken<ArrayList<Product>>() {}.getType();
        List<Product> productList = gson.fromJson(sw.toString(), listOfProductObject);
        Product p = productList.get(0);
        assertEquals(p.getId(),1);
        assertEquals(p.getName(),"Les Paul");
        assertEquals(p.getManufacturer(),"Gibson");
        assertEquals(p.getYear(),"1960");
        assertEquals(p.getPrice(),"10000.00");
        assertNotEquals(p.getDescription(),"");
    }

}