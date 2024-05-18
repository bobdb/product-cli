package net.bobdb;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class ProductsCLITest {

    @Test
    void mainTest() {
        ProductsCLI app = new ProductsCLI();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        int exitCode = cmd.execute("--all --limit 10");
        assertEquals(0, exitCode);
        assertEquals("[{}]",sw.toString().trim());

    }

}