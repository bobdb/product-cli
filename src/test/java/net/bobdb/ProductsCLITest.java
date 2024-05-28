package net.bobdb;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        Type listOfProductObject = new TypeToken<ArrayList<Product>>() {}.getType();
        Gson gson = new Gson();
        List<Product> products = gson.fromJson(sw.toString(), listOfProductObject);
        assertEquals(72, products.size());   // TODO should redirect to --help
    }

    @Test
    void testPrettyPrint() {
        System.out.println("products -p");
        String[] args = new String[]{"-p"};
        int exitCode = cmd.execute(args);
        assertEquals(0, exitCode); // it works...just look at console.
    }

    @Test
    void testFindAll() {
        System.out.println("products -a");
        String[] args = {"-a"};
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
        System.out.println("products 100");
        String[] args = new String[]{"100"};
        int exitCode = cmd.execute(args);
        assertEquals(0,exitCode);
        String expected = """
                        [{"id":100,"name":"Les Paul","description":"","manufacturer":"Gibson","year":"1960","price":"10000.00","quantity":10}]
                """;
        assertEquals(expected.trim(),sw.toString());
    }

    @Test
    void testFindWithoutHittingInventoryService() {
        System.out.println("products -i=false 100");
        String[] args = new String[]{"-i=false","100"};
        int exitCode = cmd.execute(args);
        assertEquals(0,exitCode);
        String expected = """
                        [{"id":100,"name":"Les Paul","description":"","manufacturer":"Gibson","year":"1960","price":"10000.00"}]
                   """;
        assertEquals(expected.trim(),sw.toString());
    }

    @Test
    void testFindAndGetDescription() {
        System.out.println("products -d 100");
        String[] args = new String[]{"-d","100"};
        int exitCode = cmd.execute(args);
        assertEquals(0,exitCode);
        String expected = """
                        [{"id":100,"name":"Les Paul","description":"{description}","manufacturer":"Gibson","year":"1960","price":"10000.00","quantity":10}]
                   """;
        assertEquals(expected.trim(),sw.toString());
    }

}