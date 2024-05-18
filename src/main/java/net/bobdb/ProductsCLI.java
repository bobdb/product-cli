package net.bobdb;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

import java.util.concurrent.Callable;

@Command(name = "products", mixinStandardHelpOptions = true, version = "checksum 4.0",
        description = "Do some stuff with the products API ")
class ProductsCLI implements Callable<Integer> {

    @Spec
    CommandSpec spec;

    @Option(names = {"-a", "--all"}, description = "list all products")
    private boolean listAll  = true;

    @Option(names = {"-l", "--limit"}, description = "list all products")
    private int limit = 5;

    @Override
    public Integer call() throws Exception {

        return 0;
    }

    // this example implements Callable, so parsing, error handling and handling user
    // requests for usage help or version help can be done with one line of code.
    public static void main(String... args) {
        int exitCode = new CommandLine(new ProductsCLI()).execute(args);
        System.exit(exitCode);
    }
}