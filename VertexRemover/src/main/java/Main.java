package main.java;

import com.cgvsu.model.Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String inputFilePath = "src\\main\\java\\3DModels\\Faceform\\WrapHead.obj";
        String outputFilePath = "src\\main\\java\\3DModels\\Faceform\\WrapHead_edited.obj";
        List<Integer> verticesToDelete;

        String deleteVerticesFilePath = "src\\main\\java\\vertices_to_delete.txt";  // Файл с вершинами(опционально)
        try {
            verticesToDelete = VertexRemover.readVerticesFromFile(deleteVerticesFilePath);
        } catch (IOException e) {
            System.out.println("Couldnt load vertices_to_delete.txt, using default values");
            verticesToDelete = List.of(1, 2, 3); // Индексы вершин по умолчанию(просто список)
        }

        try {
            Reader input = new FileReader(inputFilePath);
            Writer output = new FileWriter(outputFilePath);
            VertexRemover.processModel(input, output, verticesToDelete, true, false, true, true);
            System.out.println("File saved at " + outputFilePath);
        } catch (IOException e) {
            System.out.println("An error occured: " + e.getMessage());
        }
    }
}
