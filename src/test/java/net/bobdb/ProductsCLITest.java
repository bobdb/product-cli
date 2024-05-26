package net.bobdb;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeAll;
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

        int exitCode = -1;
        String[] args = {""};
        String expected = "";

        // products
        System.out.println("products");
        exitCode = cmd.execute();
        assertEquals(0, exitCode);
        Type listOfProductObject = new TypeToken<ArrayList<Product>>() {}.getType();
        Gson gson = new Gson();
        List<Product> products = gson.fromJson(sw.toString(), listOfProductObject);
        assertEquals(72, products.size());   // TODO  should redirect to --help
    }

    @Test
    void testPrettyPrint() {
        int exitCode = -1;
        String[] args = {""};
        String expected = "";

        System.out.println("products -p");
        args = new String[]{"-p"};
        exitCode = cmd.execute(args);
        assertEquals(0, exitCode); // it works...just look at console.
    }

    @Test
    void testFindAll() {
        int exitCode = -1;
        String[] args = {""};
        String expected = "";

        System.out.println("products -a");
        args = new String[]{"-a"};
        exitCode = cmd.execute(args);
        assertEquals(0, exitCode);

        Type listOfProductObject = new TypeToken<ArrayList<Product>>() {}.getType();
        Gson gson = new Gson();
        List<Product> products = gson.fromJson(sw.toString(), listOfProductObject);
        assertEquals(72, products.size());
    }

    @Test
    void testFindN() {
        int exitCode = -1;
        String[] args = {""};
        String expected = "";
        System.out.println("products -p -l=3");
        args = new String[]{"-p", "-l","3"};
        exitCode = cmd.execute(args);
        assertEquals(0, exitCode);
    }

    @Test
    void testFindSingle() {
        int exitCode = -1;
        String[] args = {""};
        String expected = "";

        System.out.println("products 100");
        args = new String[]{"100"};
        exitCode = cmd.execute(args);
        assertEquals(0,exitCode);
        expected = """
                        [{"id":100,"name":"Les Paul","description":"","manufacturer":"Gibson","year":"1960","price":"10000.00","quantity":10}]
                """;
        assertEquals(expected.trim(),sw.toString());
    }

    @Test
    void testFindWithoutHittingInventoryService() {
        int exitCode = -1;
        String[] args = {""};
        String expected = "";

        System.out.println("products -i=false 100");
        args = new String[]{"-i=false","100"};
        exitCode = cmd.execute(args);
        assertEquals(0,exitCode);
        expected = """
                        [{"id":100,"name":"Les Paul","description":"","manufacturer":"Gibson","year":"1960","price":"10000.00"}]
                   """;
        assertEquals(expected.trim(),sw.toString());
    }

}