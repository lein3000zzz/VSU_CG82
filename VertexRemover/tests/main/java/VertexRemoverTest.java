package main.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

class VertexRemoverTest {

    @Test
    void processModelOnlyUsedRemain() {
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
            VertexRemover.processModel(input, output, List.of(2), false, true,true, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expectedOutput, output.toString().trim());
    }

    @Test
    void processModelKeepHangingPolygons() {
        String separator = System.lineSeparator();

        String inputModel = """
                v 1.0 1.0 1.0
                v 2.0 2.0 2.0
                v 3.0 3.0 3.0
                v 4.0 4.0 4.0
                v 5.0 5.0 5.0
                f 1 2 3
                f 1 3 4 5
                """;
        String expectedOutput = (
                "v 3.0 3.0 3.0" + separator +
                "v 4.0 4.0 4.0" + separator +
                "v 5.0 5.0 5.0" + separator +
                "f 1" + separator +
                "f 1 2 3").trim();

        Reader input = new StringReader(inputModel);
        Writer output = new StringWriter();

        try {
            VertexRemover.processModel(input, output, List.of(1, 2), true, false, true, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expectedOutput, output.toString().trim());
    }

    @Test
    void processModelKeepHangingAndCleanUpObsoleteTextures() {
        String separator = System.lineSeparator();

        String inputModel = """
                v 1.0 1.0 1.0
                v 2.0 2.0 2.0
                v 3.0 3.0 3.0
                v 4.0 4.0 4.0
                v 5.0 5.0 5.0
                vt 1.0 2.0
                vt 3.0 4.0
                vt 5.0 6.0
                vt 5.0 6.0
                vt 5.0 6.0
                vt 5.0 6.0
                vt 5.0 6.0
                vt 66.0 66.0
                f 1/2 2/2 3/1
                f 1 3 4 5
                f 6666 777 7777
                """;
        String expectedOutput = (
                "v 1.0 1.0 1.0" + separator +
                        "v 3.0 3.0 3.0" + separator +
                        "v 4.0 4.0 4.0" + separator +
                        "v 5.0 5.0 5.0" + separator +
                        "vt 1.0 2.0" + separator +
                        "vt 3.0 4.0" + separator +
                        "vt 5.0 6.0" + separator +
                        "vt 5.0 6.0" + separator +
                        "vt 5.0 6.0" + separator +
                        "vt 5.0 6.0" + separator +
                        "vt 5.0 6.0" + separator +
                        "vt 66.0 66.0" + separator +
                        "f 1/2 2/1" + separator + // третья вершина стала первой
                        "f 1 2 3 4" + separator + // Порядок вершин изменился на -1 для всех вершин после 2
                        "f 6666 777 7777").trim();
        Reader input = new StringReader(inputModel);
        Writer output = new StringWriter();

        try {
            VertexRemover.processModel(input, output, List.of(2), true, false, false, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expectedOutput, output.toString().trim());
    }

    @Test
    void processModelRemoveAll() {
        String separator = System.lineSeparator();

        String inputModel = """
                v 1.0 1.0 1.0
                v 2.0 2.0 2.0
                v 3.0 3.0 3.0
                vt 0.1 0.2
                vt 0.3 0.4
                vn 0.0 0.0 1.0
                vn 1.0 0.0 0.0
                f 1/1/1 2/2/2 3/1/1
                f 2/2/2 3/1/1 1/1/1
                """;
        String expectedOutput = (
                "").trim();

        Reader input = new StringReader(inputModel);
        Writer output = new StringWriter();

        try {
            VertexRemover.processModel(input, output, List.of(1, 2, 3), false, true, true, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expectedOutput, output.toString().trim());
    }

    @Test
    void processModelKeepVtAndVn() {
        String separator = System.lineSeparator();

        String inputModel = """
                v 1.0 1.0 1.0
                v 2.0 2.0 2.0
                v 3.0 3.0 3.0
                vt 0.1 0.2
                vt 0.3 0.4
                vn 0.0 0.0 1.0
                vn 1.0 0.0 0.0
                f 1/1/1 2/2/2 3/1/1
                f 2/2/2 3/1/1 1/1/1
                """;
        String expectedOutput = (
                "vt 0.1 0.2" + separator +
                        "vt 0.3 0.4" + separator +
                        "vn 0.0 0.0 1.0" + separator +
                        "vn 1.0 0.0 0.0").trim();

        Reader input = new StringReader(inputModel);
        Writer output = new StringWriter();

        try {
            VertexRemover.processModel(input, output, List.of(1, 2, 3), false, true, false, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expectedOutput, output.toString().trim());
    }
    @Test
    void processModelKeepVt() {
        String separator = System.lineSeparator();

        String inputModel = """
                v 1.0 1.0 1.0
                v 2.0 2.0 2.0
                v 3.0 3.0 3.0
                vt 0.1 0.2
                vt 0.3 0.4
                vn 0.0 0.0 1.0
                vn 1.0 0.0 0.0
                f 1/1/1 2/2/2 3/1/1
                f 2/2/2 3/1/1 1/1/1
                """;
        String expectedOutput = (
                "vt 0.1 0.2" + separator +
                        "vt 0.3 0.4").trim();

        Reader input = new StringReader(inputModel);
        Writer output = new StringWriter();

        try {
            VertexRemover.processModel(input, output, List.of(1, 2, 3), false, true, true, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expectedOutput, output.toString().trim());
    }
    @Test
    void processModelKeepVn() {
        String separator = System.lineSeparator();

        String inputModel = """
                v 1.0 1.0 1.0
                v 2.0 2.0 2.0
                v 3.0 3.0 3.0
                vt 0.1 0.2
                vt 0.3 0.4
                vn 0.0 0.0 1.0
                vn 1.0 0.0 0.0
                f 1/1/1 2/2/2 3/1/1
                f 2/2/2 3/1/1 1/1/1
                """;
        String expectedOutput = (
                "vn 0.0 0.0 1.0" + separator +
                        "vn 1.0 0.0 0.0").trim();

        Reader input = new StringReader(inputModel);
        Writer output = new StringWriter();

        try {
            VertexRemover.processModel(input, output, List.of(1, 2, 3), false, true, false, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expectedOutput, output.toString().trim());
    }

    @Test
    void processModelMixed() {
        String separator = System.lineSeparator();

        String inputModel = """
                v 1.0 1.0 1.0
                v 2.0 2.0 2.0
                v 3.0 3.0 3.0
                vt 0.1 0.2
                vt 0.3 0.4
                vt 0.5 0.6
                vn 0.0 0.0 5.0
                vn 1.0 0.0 0.0
                vn 0.0 1.0 0.0
                f 1//1 2//2 3//3
                f 1/1/1 2/2/2 3/3/3
                """;
        String expectedOutput = (
                "v 2.0 2.0 2.0" + separator +
                "vt 0.3 0.4" + separator +
                "vn 1.0 0.0 0.0" + separator +
                "f 1//1" + separator +
                        "f 1/1/1").trim();

        Reader input = new StringReader(inputModel);
        Writer output = new StringWriter();

        try {
            VertexRemover.processModel(input, output, List.of(1, 3), true, false, true, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expectedOutput, output.toString().trim());
    }
}