package net.bobdb;

import com.google.gson.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Callable;

@Command(name = "products", mixinStandardHelpOptions = true, version = "checksum 4.0",
        description = "Do some stuff with the products API ")
class ProductsCLI implements Callable<Integer> {

    @Spec
    CommandSpec spec;

    @Option(names = {"-a", "--all"}, description = "list all products")
    private Boolean listAll = true;

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
    public Integer call() throws IOException, InterruptedException {

        String responseString = "";

        if (listAll) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/products"))
                    .GET()
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseString = response.body();
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

        if (id==-1) {

        }

        if (useInventory) {

        }



        if (count > -1) {

        }

        if (start>0 && end>0) {

        }

        if (getDescription) {

        }

        if (isPretty) {

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