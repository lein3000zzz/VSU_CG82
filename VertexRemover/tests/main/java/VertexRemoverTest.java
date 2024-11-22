package main.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

class VertexRemoverTest {

    @Test
    void processModel() {
        String separator = System.lineSeparator();

        String inputModel = """
                v 1.0 1.0 1.0
                v 2.0 2.0 2.0
                v 3.0 3.0 3.0
                v 4.0 4.0 4.0
                f 1 2 3
                f 1 3 4
                """;
        String expectedOutput = (
                "v 1.0 1.0 1.0" + separator +
                "v 3.0 3.0 3.0" + separator +
                "v 4.0 4.0 4.0" + separator +
                "f 1 2 3").trim();

        Reader input = new StringReader(inputModel);
        Writer output = new StringWriter();

        try {
            VertexRemover.processModel(input, output, List.of(2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expectedOutput, output.toString().trim());
    }
}