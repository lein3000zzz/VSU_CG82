package main.java;

import java.io.*;
import java.util.*;

public class VertexRemover {
    public static void main(String[] args) {
        String inputFilePath = "src\\main\\java\\3DModels\\Faceform\\WrapHead.obj";
        String outputFilePath = "src\\main\\java\\3DModels\\Faceform\\WrapHead_edited.obj";
        List<Integer> verticesToDelete;


        String deleteVerticesFilePath = "src\\main\\java\\vertices_to_delete.txt";  // Файл с вершинами
        try {
            verticesToDelete = readVerticesFromFile(deleteVerticesFilePath);
        } catch (IOException e) {
            System.out.println("Couldnt load vertices_to_delete.txt, using default values");
            verticesToDelete = List.of(1, 2, 3); // Индексы вершин по умолчанию(просто список)
        }

        try {
            Reader input = new FileReader(inputFilePath);
            Writer output = new FileWriter(outputFilePath);
            processModel(input, output, verticesToDelete);
            System.out.println("File saved at " + outputFilePath);
        } catch (IOException e) {
            System.out.println("An error occured: " + e.getMessage());
        }
    }

    public static void processModel(Reader input, Writer output, List<Integer> verticesToDelete) throws IOException {
        List<String> vertices = new ArrayList<>();
        List<String> textureVertices = new ArrayList<>();
        List<String> normals = new ArrayList<>();
        List<String> faces = new ArrayList<>();

        parseModel(input, vertices, textureVertices, normals, faces);

        Map<Integer, Integer> indexMapping = new HashMap<>();
        List<String> newVertices = removeVertices(vertices, verticesToDelete, indexMapping);

        List<String> newFaces = updateFaces(faces, verticesToDelete, indexMapping);

        writeModel(output, newVertices, textureVertices, normals, newFaces);
    }

//    public static void processModel(String inputFilePath, String outputFilePath, List<Integer> verticesToDelete) throws IOException {
//        try (Reader input = new FileReader(inputFilePath); Writer output = new FileWriter(outputFilePath)) {
//
//            List<String> vertices = new ArrayList<>();
//            List<String> textureVertices = new ArrayList<>();
//            List<String> normals = new ArrayList<>();
//            List<String> faces = new ArrayList<>();
//
//            parseModel(input, vertices, textureVertices, normals, faces);
//
//            Map<Integer, Integer> indexMapping = new HashMap<>();
//            List<String> newVertices = removeVertices(vertices, verticesToDelete, indexMapping);
//
//            List<String> newFaces = updateFaces(faces, verticesToDelete, indexMapping);
//
//            writeModel(output, newVertices, textureVertices, normals, newFaces);
//        }
//    }

    private static List<Integer> readVerticesFromFile(String filePath) throws IOException {
        List<Integer> verticesToDelete = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                verticesToDelete.add(Integer.parseInt(line.trim()));
            }
        }
        return verticesToDelete;
    }

    private static void parseModel(Reader input, List<String> vertices, List<String> textureVertices, List<String> normals, List<String> faces) throws IOException {
        try (BufferedReader reader = new BufferedReader(input)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    vertices.add(line);
                } else if (line.startsWith("vt ")) {
                    textureVertices.add(line);
                } else if (line.startsWith("vn ")) {
                    normals.add(line);
                } else if (line.startsWith("f ")) {
                    faces.add(line);
                }
            }
        }
    }

    private static List<String> removeVertices(List<String> vertices, List<Integer> verticesToDelete, Map<Integer, Integer> indexMapping) {
        Set<Integer> verticesSet = new HashSet<>(verticesToDelete);
        List<String> newVertices = new ArrayList<>();
        int newIndex = 1;

        for (int i = 0; i < vertices.size(); i++) {
            if (!verticesSet.contains(i + 1)) { // если ее не надо удалять, то мы добавляем в новые вершины.
                newVertices.add(vertices.get(i));
                indexMapping.put(i + 1, newIndex);
                newIndex++;
            }
        }
        return newVertices;
    }

    private static List<String> updateFaces(List<String> faces, List<Integer> verticesToDelete, Map<Integer, Integer> indexMapping) {
        Set<Integer> verticesSet = new HashSet<>(verticesToDelete);
        List<String> newFaces = new ArrayList<>();

        for (String face : faces) {
            String[] faceVertices = face.split(" ");
            boolean skipFace = false;
            List<String> newFace = new ArrayList<>();

            for (int j = 1; j < faceVertices.length; j++) {
                String[] vertexData = faceVertices[j].split("/");

                int vertexIndex = Integer.parseInt(vertexData[0]);
                if (verticesSet.contains(vertexIndex)) {
                    skipFace = true;
                    break;
                }

                StringBuilder newVertexData = new StringBuilder();
                newVertexData.append(indexMapping.get(vertexIndex));

                if (vertexData.length > 1 && !vertexData[1].isEmpty()) { // дописать текстурные координаты
                    newVertexData.append("/").append(vertexData[1]);
                }
                if (vertexData.length > 2 && !vertexData[2].isEmpty()) { // дописать нормали
                    newVertexData.append("/").append(vertexData[2]);
                }

                newFace.add(newVertexData.toString());
            }

            if (!skipFace) {
                newFaces.add("f " + String.join(" ", newFace));
            }
        }
        return newFaces;
    }

    private static void writeModel(Writer output, List<String> vertices, List<String> textureVertices, List<String> normals, List<String> faces) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(output)) {
            for (String vertex : vertices) {
                writer.write(vertex);
                writer.newLine();
            }
            for (String textureVertex : textureVertices) {
                writer.write(textureVertex);
                writer.newLine();
            }
            for (String normal : normals) {
                writer.write(normal);
                writer.newLine();
            }
            for (String face : faces) {
                writer.write(face);
                writer.newLine();
            }
        }
    }
}
