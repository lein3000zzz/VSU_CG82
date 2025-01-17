package main.java.com.cgvsu.fileVertexRemover;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class MainObsolete {

    public static void main(String[] args) {
//        Path inputFilePath = Path.of("src\\main\\java\\3DModels\\CaracalCube\\caracal_cube.obj");
//        Path outputFilePath = Path.of("src\\main\\java\\3DModels\\CaracalCube\\caracal_cube_copy.obj");
        List<Integer> verticesToDelete;

        String deleteVerticesFilePath = "src\\main\\java\\vertices_to_delete.txt";  // Файл с вершинами(опционально)
        try {
            verticesToDelete = VertexRemover.readVerticesFromFile(deleteVerticesFilePath);
            System.out.println(verticesToDelete.toString().substring(1, verticesToDelete.size() - 1));
        } catch (IOException e) {
            System.out.println("Couldnt load vertices_to_delete.txt, using default values");
//            verticesToDelete = List.of(1, 2, 3); // Индексы вершин по умолчанию(просто список)
        }

//        try {
//            Reader input = new FileReader(inputFilePath.toString());
//            Writer output = new FileWriter(outputFilePath.toString());
//            VertexRemover.processModel(input, output, verticesToDelete, true, false, true, true);
//            System.out.println("File saved at " + outputFilePath);
//        } catch (IOException e) {
//            System.out.println("An error occured: " + e.getMessage());
//        }
    }
}
