package net.bobdb;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "products", mixinStandardHelpOptions = true, version = "checksum 4.0",
        description = "Do some stuff with the products API ")
class ProductsCLI implements Callable<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsCLI.class);

    @Spec
    CommandSpec spec;

    @Option(names = {"-a", "--all"}, description = "list all products")
    private Boolean listAll = false;

    @Option(names = {"-i", "--inventory"}, description = "add an inventory count to result")
    private Boolean useInventory = true;

    @Option(names = {"-n"}, description = "Count of results")
    private Integer count = -1;

    @Option(names = {"-p", "--pretty"}, description = "Return pretty results")
    private Boolean isPretty = false;

    @Option(names = {"-d", "--describe"}, description = "Get a possibly AI gen description")
    private Boolean getDescription = false;


    @Option( names = {"-s", "--start"}, description = "Range Start")
    private Integer start = -1;

    @Option( names = {"-e", "--end"}, description = "Range End")
    private Integer end = -1;

    @Parameters(index = "0", description = "The product ID", defaultValue = "-1")
    private Integer id;

    private static final String API_KEY="{key}}";

    @Override
    public Integer call() throws Exception {

        String responseString = "";
        List<Product> products = new ArrayList<>();

        if (listAll) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/products"))
                    .GET()
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseString = response.body();
            Gson gson = new Gson(); // Or use new GsonBuilder().create();
            Type listOfProductObject = new TypeToken<ArrayList<Product>>() {}.getType();
            products = gson.fromJson(responseString, listOfProductObject);

            for(Product p : products) {
                HttpRequest inventoryRequest = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/inventory/" + p.getId()))
                        .GET()
                        .build();
                var inventoryResponse = client.send(inventoryRequest, HttpResponse.BodyHandlers.ofString());
                if (inventoryResponse.statusCode()!=200) {
                    LOGGER.info("modelId " + p.getId() + " not found in inventory");
                }
                var inventoryItem = gson.fromJson(inventoryResponse.body(), InventoryItem.class);
                int q = inventoryItem.quantity();
                p.setQuantity(q);
            }

        } else {
            if (id>0) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/products/" + id))
                        .GET()
                        .build();
                HttpClient client = HttpClient.newHttpClient();
                var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                responseString = response.body();
                if (useInventory) {

                }
            } else {
                throw new InvalidPropertiesFormatException("id has to be > 0. id=" + id );
            }
        }






        if (count > -1) {

        }

        if (start>0 && end>0) {

        }

        if (getDescription) {

        }

        if (isPretty) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = JsonParser.parseString(responseString);
            String prettyResponse = gson.toJson(je);
            spec.commandLine().getOut().printf(prettyResponse);
            System.out.println(prettyResponse);
        } else {
            spec.commandLine().getOut().printf(responseString);
            System.out.println(responseString);
        }

        return 0;
    }

    // this example implements Callable, so parsing, error handling and handling user
    // requests for usage help or version help can be done with one line of code.
    public static void main(String... args) {
        int exitCode = new CommandLine(new ProductsCLI()).execute(args);
        System.exit(exitCode);
    }
}