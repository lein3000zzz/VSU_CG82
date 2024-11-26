package main.java.com.cgvsu.fileVertexRemover;

import java.io.*;
import java.util.*;

public class VertexRemover {

    public static void processModel(Reader input, Writer output, List<Integer> verticesToDelete, boolean keepHangingFaces, boolean cleanHangingPolygonsAfterwards, boolean cleanUpUnusedNormals, boolean cleanUpUnusedTextures) throws IOException {
        List<String> vertices = new ArrayList<>();
        List<String> textureVertices = new ArrayList<>();
        List<String> normals = new ArrayList<>();
        List<String> faces = new ArrayList<>();

        parseModel(input, vertices, textureVertices, normals, faces);
        System.out.println(vertices);
        System.out.println(textureVertices);
        System.out.println(faces);
        Map<Integer, Integer> vertexIndexMapping = new HashMap<>();

        List<String> newVertices = removeVertices(vertices, verticesToDelete, vertexIndexMapping);
        List<String> newFaces = updateFaces(faces, verticesToDelete, vertexIndexMapping, keepHangingFaces);
        System.out.println(newFaces);
        // Отдельно, чтобы было 3 случая: мы оставляем все висячие, мы оставляем только старые висячие, мы не оставляем висячих.
        if (cleanHangingPolygonsAfterwards) {
            Set<Integer> validVertexIndices = collectValidIndices(vertices);
            Set<Integer> validTextureIndices = collectValidIndices(textureVertices);
            Set<Integer> validNormalIndices = collectValidIndices(normals);
            newFaces = cleanFaces(newFaces, validVertexIndices, validTextureIndices, validNormalIndices);
        }

        if (cleanUpUnusedTextures) {
            Set<Integer> initiallyUsedTextureIndices = collectInitiallyUsedIndices(faces, 1);
            Map<Integer, Integer> textureIndexMapping = new HashMap<>();
            //removeAllUnusedData(textureVertices, textureIndexMapping, newFaces, 1);
            removeObsoleteData(textureVertices, textureIndexMapping, newFaces, 1, initiallyUsedTextureIndices);
        }
        if (cleanUpUnusedNormals) {
            Set<Integer> initiallyUsedNormalIndices = collectInitiallyUsedIndices(faces, 2);
            Map<Integer, Integer> normalIndexMapping = new HashMap<>();
            //removeAllUnusedData(normals, normalIndexMapping, newFaces, 2);
            removeObsoleteData(normals, normalIndexMapping, newFaces, 2, initiallyUsedNormalIndices);
        }
        input.close();
        writeModel(output, newVertices, textureVertices, normals, newFaces);
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

    private static List<String> updateFaces(List<String> faces, List<Integer> verticesToDelete, Map<Integer, Integer> vertexIndexMapping, boolean keepHangingFaces) {
        Set<Integer> verticesSet = new HashSet<>(verticesToDelete); // Удаляемые вершины
        List<String> newFaces = new ArrayList<>();

        for (String face : faces) {
            String[] faceVertices = face.split(" "); // Разделяем строку по пробелу
            boolean containsDeletedVertex = false;
            List<String> newFace = new ArrayList<>();

            for (int j = 1; j < faceVertices.length; j++) {
                String[] vertexData = faceVertices[j].split("/");

                try {
                    // Индекс вершины
                    int vertexIndex = Integer.parseInt(vertexData[0]);

                    // Проверяем, удалена ли вершина
                    if (verticesSet.contains(vertexIndex)) {
                        containsDeletedVertex = true;
                        if (!keepHangingFaces)
                            break; // Удаляем весь полигон
                        continue; // Пропускаем удалённую вершину
                    }

                    StringBuilder newVertexData = new StringBuilder();
                    newVertexData.append(vertexIndexMapping.getOrDefault(vertexIndex, vertexIndex)); // Индекс вершины

                    // Добавляем текстурную координату (если она существует)
                    if (vertexData.length > 1) {
                        newVertexData.append("/");
                        if (!vertexData[1].isEmpty()) {
                            newVertexData.append(vertexData[1]);
                        }
                    }

                    // Добавляем нормаль (если она существует)
                    if (vertexData.length > 2) {
                        newVertexData.append("/");
                        if (!vertexData[2].isEmpty()) {
                            newVertexData.append(vertexData[2]);
                        }
                    }

                    newFace.add(newVertexData.toString());
                } catch (NumberFormatException e) {
                    // Если ссылка была невалидной изначально, сохраняем её
                    newFace.add(faceVertices[j]);
                }
            }

            // Добавляем полигон только если в нем остались вершины
            if (!newFace.isEmpty() && (!containsDeletedVertex || keepHangingFaces)) {
                newFaces.add("f " + String.join(" ", newFace));
            }
        }
        return newFaces;
    }

    private static Set<Integer> collectInitiallyUsedIndices(List<String> faces, int componentIndex) {
        Set<Integer> usedIndices = new HashSet<>();
        for (String face : faces) {
            String[] faceVertices = face.split(" ");
            for (int j = 1; j < faceVertices.length; j++) {
                String[] vertexData = faceVertices[j].split("/");
                if (vertexData.length > componentIndex && !vertexData[componentIndex].isEmpty()) {
                    try {
                        usedIndices.add(Integer.parseInt(vertexData[componentIndex]));
                    } catch (Exception ignored) {
                        // Игнорируем неверные данные (число не спарсилось)
                    }
                }
            }
        }
        return usedIndices;
    }

    private static void removeObsoleteData(
            List<String> data,
            Map<Integer, Integer> indexMapping,
            List<String> faces,
            int componentIndex,
            Set<Integer> initiallyUsedIndices
    ) {
        // Собираем текущие используемые индексы
        Set<Integer> currentlyUsedIndices = new HashSet<>();
        for (String face : faces) {
            String[] faceVertices = face.split(" ");
            for (int j = 1; j < faceVertices.length; j++) {
                String[] vertexData = faceVertices[j].split("/");
                if (vertexData.length > componentIndex && !vertexData[componentIndex].isEmpty()) {
                    currentlyUsedIndices.add(Integer.parseInt(vertexData[componentIndex]));
                }
            }
        }

        // Удаляем только те, которые стали неиспользуемыми
        List<String> newData = new ArrayList<>();
        int newIndex = 1;
        for (int i = 0; i < data.size(); i++) {
            int originalIndex = i + 1;
            if (currentlyUsedIndices.contains(originalIndex) || !initiallyUsedIndices.contains(originalIndex)) {
                newData.add(data.get(i));
                indexMapping.put(originalIndex, newIndex++);
            }
        }

        data.clear();
        data.addAll(newData);

        // Обновляем индексы в полигонах
        for (int i = 0; i < faces.size(); i++) {
            String[] faceVertices = faces.get(i).split(" ");
            StringBuilder updatedFace = new StringBuilder("f");

            for (int j = 1; j < faceVertices.length; j++) {
                String[] vertexData = faceVertices[j].split("/");
                StringBuilder updatedVertexData = new StringBuilder();

                updatedVertexData.append(vertexData[0]);

                // Обновляем индекс текстурной координаты
                if (vertexData.length > 1) {
                    updatedVertexData.append("/");
                    if (!vertexData[1].isEmpty()) {
                        int textureIndex = Integer.parseInt(vertexData[1]);
                        if (componentIndex == 1)
                            updatedVertexData.append(indexMapping.getOrDefault(textureIndex, textureIndex));
                        else
                            updatedVertexData.append(textureIndex);
                    }
                }

                // Обновляем индекс нормали
                if (vertexData.length > 2) {
                    updatedVertexData.append("/");
                    if (!vertexData[2].isEmpty()) {
                        int normalIndex = Integer.parseInt(vertexData[2]);
                        if (componentIndex == 2)
                            updatedVertexData.append(indexMapping.getOrDefault(normalIndex, normalIndex));
                        else
                            updatedVertexData.append(normalIndex);
                    }
                }

                updatedFace.append(" ").append(updatedVertexData);
            }

            // Заменяем старую строку полигона на обновленную
            faces.set(i, updatedFace.toString());
        }
    }


    private static List<String> cleanFaces(List<String> faces, Set<Integer> validVertexIndices, Set<Integer> validTextureIndices, Set<Integer> validNormalIndices) {
        List<String> newFaces = new ArrayList<>();

        for (String face : faces) {
            String[] faceVertices = face.split(" ");
            boolean isHangingFace = false;
            List<String> newFace = new ArrayList<>();

            for (int j = 1; j < faceVertices.length; j++) {
                String[] vertexData = faceVertices[j].split("/");

                try {
                    int vertexIndex = Integer.parseInt(vertexData[0]);
                    if (!validVertexIndices.contains(vertexIndex)) {
                        isHangingFace = true;
                        break; // Если вершина невалидна, полигон висячий
                    }

                    // Проверяем текстурную координату
                    if (vertexData.length > 1 && !vertexData[1].isEmpty()) {
                        int textureIndex = Integer.parseInt(vertexData[1]);
                        if (!validTextureIndices.contains(textureIndex)) {
                            isHangingFace = true;
                            break;
                        }
                    }

                    // Проверяем нормаль
                    if (vertexData.length > 2 && !vertexData[2].isEmpty()) {
                        int normalIndex = Integer.parseInt(vertexData[2]);
                        if (!validNormalIndices.contains(normalIndex)) {
                            isHangingFace = true;
                            break;
                        }
                    }

                    // Формируем новую строку вершины
                    newFace.add(String.join("/", vertexData));
                } catch (NumberFormatException e) {
                    isHangingFace = true; // Некорректные данные также делают полигон висячим
                    break;
                }
            }

            // Добавляем только невисячие полигоны
            if (!newFace.isEmpty() && !isHangingFace) {
                newFaces.add("f " + String.join(" ", newFace));
            }
        }

        return newFaces;
    }

    private static Set<Integer> collectValidIndices(List<String> data) {
        Set<Integer> validIndices = new HashSet<>();
        for (int i = 0; i < data.size(); i++) {
            validIndices.add(i + 1); // Индексы в файлах OBJ начинаются с 1
        }
        return validIndices;
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

    private static void removeAllUnusedData(List<String> data, Map<Integer, Integer> indexMapping, List<String> faces, int componentIndex) { //Удаляем все текстурные вершины и нормали, которые не используются
        Set<Integer> usedIndices = new HashSet<>();

        for (String face : faces) {
            String[] faceVertices = face.split(" ");
            for (int j = 1; j < faceVertices.length; j++) {
                String[] vertexData = faceVertices[j].split("/");
                if (vertexData.length > componentIndex && !vertexData[componentIndex].isEmpty()) {
                    usedIndices.add(Integer.parseInt(vertexData[componentIndex]));
                }
            }
        }

        List<String> newData = new ArrayList<>();
        int newIndex = 1;
        for (int i = 0; i < data.size(); i++) {
            if (usedIndices.contains(i + 1)) {
                newData.add(data.get(i));
                indexMapping.put(i + 1, newIndex++);
            }
        }

        data.clear();
        data.addAll(newData);

        // Обновляем индексы в полигонах
        for (int i = 0; i < faces.size(); i++) {
            String[] faceVertices = faces.get(i).split(" ");
            StringBuilder updatedFace = new StringBuilder("f");

            for (int j = 1; j < faceVertices.length; j++) {
                String[] vertexData = faceVertices[j].split("/");
                StringBuilder updatedVertexData = new StringBuilder();

                // Обновляем индекс вершины
                updatedVertexData.append(vertexData[0]);

                // Обновляем индекс текстурной координаты
                if (vertexData.length > 1) {
                    updatedVertexData.append("/");
                    if (!vertexData[1].isEmpty()) {
                        int textureIndex = Integer.parseInt(vertexData[1]);
                        if (componentIndex == 1)
                            updatedVertexData.append(indexMapping.getOrDefault(textureIndex, textureIndex));
                        else
                            updatedVertexData.append(textureIndex);
                    }
                }

                // Обновляем индекс нормали
                if (vertexData.length > 2) {
                    updatedVertexData.append("/");
                    if (!vertexData[2].isEmpty()) {
                        int normalIndex = Integer.parseInt(vertexData[2]);
                        if (componentIndex == 2)
                            updatedVertexData.append(indexMapping.getOrDefault(normalIndex, normalIndex));
                        else
                            updatedVertexData.append(normalIndex);
                    }
                }

                updatedFace.append(" ").append(updatedVertexData);
            }

            // Заменяем старую строку полигона на обновленную
            faces.set(i, updatedFace.toString());
        }
    }

    public static List<Integer> readVerticesFromFile(String filePath) throws IOException {
        List<Integer> verticesToDelete = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                verticesToDelete.add(Integer.parseInt(line.trim()));
            }
        }
        return verticesToDelete;
    }
}
