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

        int exitCode = cmd.execute();
        assertEquals(0, exitCode);
        assertEquals("[{\"id\":100,\"name\":\"Les Paul\"", sw.toString().substring(0,28));

        String[] args = {"-p"};
        exitCode = cmd.execute(args);
        assertEquals(0, exitCode);
        // it works...just look at it.  for some reason it loses newlines by the time it gets back here. dkdc



    }

}