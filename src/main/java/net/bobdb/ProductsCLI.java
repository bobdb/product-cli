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

    @Option(names = {"-l"}, description = "Limit number of results")
    private Integer count = -1;

    @Option(names = {"-p", "--pretty"}, description = "Return pretty results")
    private Boolean isPretty = false;

    @Option(names = {"-d", "--describe"}, description = "Get a possibly AI gen description")
    private Boolean getDescription = false;

    @Parameters(index = "0", description = "The product ID", defaultValue = "-1")
    private Integer id;

    private static final String API_KEY="{key}}";

    @Override
    public Integer call() throws Exception {

        if (id==-1) {
            listAll=true;
        }

        //URL
        String urlString = "http://localhost:8080/api/products";

        if (!listAll) {
            if (id>0){
                urlString = "http://localhost:8080/api/products/" + id;
            } else {
                throw new InvalidPropertiesFormatException("id has to be > 0. id=" + id );
            }
        }

        // Request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseString = response.body();

        Gson gson;
        if (isPretty)
            gson = new GsonBuilder().setPrettyPrinting().create();
        else
            gson = new Gson();

        List<Product> products = new ArrayList<>();
        if (listAll) {
            Type listOfProductObject = new TypeToken<ArrayList<Product>>() {}.getType();
            products = gson.fromJson(responseString, listOfProductObject);
        } else {
            Product p = gson.fromJson(responseString,Product.class);
            products.add(p);
        }

        if (useInventory) {
            for (Product p : products) {
                HttpRequest inventoryRequest = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/inventory/" + p.getId()))
                        .GET()
                        .build();
                var inventoryResponse = client.send(inventoryRequest, HttpResponse.BodyHandlers.ofString());
                if (inventoryResponse.statusCode() != 200) {
                    LOGGER.info("modelId " + p.getId() + " not found in inventory");
                }
                var inventoryItem = gson.fromJson(inventoryResponse.body(), InventoryItem.class);
                int q = inventoryItem.quantity();
                p.setQuantity(q);
            }
        } else {
            for (Product p : products) {
                p.setQuantity(null);
            }
        }

        if (count > 0) {
            products = products.stream().limit(count).toList();
        }


        if (getDescription) {
            for (Product p : products) {
                DescriptionService.getDescription(p);
            }
        }

        // json text output
        JsonElement je = gson.toJsonTree(products);
        String responseJson = gson.toJson(je);
        spec.commandLine().getOut().printf(responseJson);
        System.out.println(responseJson);

        return 0;
    }

    // this example implements Callable, so parsing, error handling and handling user
    // requests for usage help or version help can be done with one line of code.
    public static void main(String... args) {
        int exitCode = new CommandLine(new ProductsCLI()).execute(args);
        System.exit(exitCode);
    }
}