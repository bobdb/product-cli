package net.bobdb;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
class CheckSumTest {


    @Test
    void mainTest() {
        CheckSum app = new CheckSum();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        int exitCode = cmd.execute("hello.txt");
        assertEquals(0, exitCode);
        assertEquals("99f86e1be6de5ae82b3f23977b65ebe43cb8359b37b6cf0ab8e6c2eaa0c99193",sw.toString().trim());

    }
}