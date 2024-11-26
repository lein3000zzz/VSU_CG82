package main.java;

import main.java.com.cgvsu.math.Vector2f;
import main.java.com.cgvsu.math.Vector3f;
import main.java.com.cgvsu.model.Model;
import main.java.com.cgvsu.model.Polygon;

import java.io.*;
import java.util.*;

public class VertexRemoverNextGen {
    public static void processModel(Model model, List<Integer> verticesToDelete, boolean keepHangingFaces, boolean cleanHangingPolygonsAfterwards, boolean cleanUpUnusedNormals, boolean cleanUpUnusedTextures) throws IOException {
        List<Vector3f> vertices = model.vertices;
        ArrayList<Vector2f> textureVertices = model.textureVertices;
        ArrayList<Vector3f> normals = model.normals;
        List<Polygon> faces = model.polygons;

        Map<Integer, Integer> vertexIndexMapping = new HashMap<>();

        ArrayList<Vector3f> newVertices = removeVertices(vertices, verticesToDelete, vertexIndexMapping);
        ArrayList<Polygon> newFaces = updateFaces(faces, verticesToDelete, vertexIndexMapping, keepHangingFaces);

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


        model.setVertices(newVertices);
        model.setNormals(normals);
        model.setTextureVertices(textureVertices);
        model.setPolygons(newFaces);
    }


    private static ArrayList<Vector3f> removeVertices(List<Vector3f> vertices, List<Integer> verticesToDelete, Map<Integer, Integer> indexMapping) {
        Set<Integer> verticesSet = new HashSet<>(verticesToDelete);
        ArrayList<Vector3f> newVertices = new ArrayList<>();
        int newIndex = 0;

        for (int i = 0; i < vertices.size(); i++) {
            if (!verticesSet.contains(i)) { // если ее не надо удалять, то мы добавляем в новые вершины.
                newVertices.add(vertices.get(i));
                indexMapping.put(i, newIndex);
                newIndex++;
            }
        }
        return newVertices;
    }

    private static ArrayList<Polygon> updateFaces(List<Polygon> faces, List<Integer> verticesToDelete, Map<Integer, Integer> vertexIndexMapping, boolean keepHangingFaces) {
        Set<Integer> verticesSet = new HashSet<>(verticesToDelete); // Удаляемые вершины
        ArrayList<Polygon> newFaces = new ArrayList<>();
        for (Polygon face : faces) {
            boolean containsDeletedVertex = false;
            Polygon newFace = new Polygon();

            List<Integer> faceVertices = face.getVertexIndices();
            List<Integer> faceTextures = face.getTextureVertexIndices();
            List<Integer> faceNormals = face.getNormalIndices();

            ArrayList<Integer> newFaceVertices = new ArrayList<>();
            ArrayList<Integer> newFaceTextures = new ArrayList<>();
            ArrayList<Integer> newFaceNormals = new ArrayList<>();

            for (int j = 0; j < faceVertices.size(); j++) {

                // Индекс вершины
                int vertexIndex = faceVertices.get(j);

                // Проверяем, удалена ли вершина
                if (verticesSet.contains(vertexIndex)) {
                    containsDeletedVertex = true;
                    if (!keepHangingFaces)
                        break; // Удаляем весь полигон
                    continue; // Пропускаем удалённую вершину
                }

                newFaceVertices.add(vertexIndexMapping.getOrDefault(vertexIndex, vertexIndex));   // Индекс вершины

                if (!faceTextures.isEmpty()) {
                    newFaceTextures.add(faceTextures.get(j)); // добавляем текстуру, если существует
                }

                if (!faceNormals.isEmpty()) {
                    newFaceNormals.add(faceNormals.get(j));  // Добавляем нормаль, если существует
                }

                newFace.setVertexIndices(newFaceVertices);
                newFace.setNormalIndices(newFaceNormals);
                newFace.setTextureVertexIndices(newFaceTextures);
            }
            // Добавляем полигон только если в нем остались вершины
            if (!newFaceVertices.isEmpty() && (!containsDeletedVertex || keepHangingFaces)) {
                newFaces.add(newFace);
            }
        }
        return newFaces;
    }

    private static Set<Integer> collectInitiallyUsedIndices(List<Polygon> faces, int componentIndex) {
        Set<Integer> usedIndices = new HashSet<>();
        for (Polygon face : faces) {
            ArrayList<Integer> faceVertices = face.getVertexIndices();
            for (int j = 0; j < faceVertices.size(); j++) {
                if (componentIndex == 1 && !face.getTextureVertexIndices().isEmpty()) {
                    usedIndices.add(face.getTextureVertexIndices().get(j));
                }
                else if (componentIndex == 2 && !face.getNormalIndices().isEmpty()) {
                    usedIndices.add(face.getNormalIndices().get(j));
                }
            }
        }
        return usedIndices;
    }

    private static <T> void removeObsoleteData(
            List<T> data,
            Map<Integer, Integer> indexMapping,
            List<Polygon> faces,
            int componentIndex,
            Set<Integer> initiallyUsedIndices
    ) {
        // Собираем текущие используемые индексы
        Set<Integer> currentlyUsedIndices = new HashSet<>();
        for (Polygon face : faces) {
            ArrayList<Integer> faceVertices = face.getVertexIndices();
            for (int j = 0; j < faceVertices.size(); j++) {
                if (componentIndex == 1 && !face.getTextureVertexIndices().isEmpty()) {
                    currentlyUsedIndices.add(face.getTextureVertexIndices().get(j));
                }
                else if (componentIndex == 2 && !face.getNormalIndices().isEmpty()) {
                    currentlyUsedIndices.add(face.getNormalIndices().get(j));
                }
            }
        }

        // Удаляем только те, которые стали неиспользуемыми
        List<T> newData = new ArrayList<>();
        int newIndex = 0;
        for (int i = 0; i < data.size(); i++) {
            if (currentlyUsedIndices.contains(i) || !initiallyUsedIndices.contains(i)) {
                newData.add(data.get(i));
                indexMapping.put(i, newIndex++);
            }
        }

        data.clear();
        data.addAll(newData);

        // Обновляем индексы в полигонах
        for (int i = 0; i < faces.size(); i++) {
            Polygon face = faces.get(i);
            Polygon newFace = new Polygon();

            List<Integer> faceVertices = face.getVertexIndices();
            List<Integer> faceTextures = face.getTextureVertexIndices();
            List<Integer> faceNormals = face.getNormalIndices();
            ArrayList<Integer> newFaceVertices = new ArrayList<>();
            ArrayList<Integer> newFaceTextures = new ArrayList<>();
            ArrayList<Integer> newFaceNormals = new ArrayList<>();


            for (int j = 0; j < faceVertices.size(); j++) {

                newFaceVertices.add(faceVertices.get(j));

                if (!faceTextures.isEmpty()) {
                    int textureIndex = faceTextures.get(j);
                    if (componentIndex == 1) {
                        newFaceTextures.add(indexMapping.getOrDefault(textureIndex, textureIndex));
                    } else {
                        newFaceTextures.add(textureIndex);
                    }
                }

                if (!faceNormals.isEmpty()) {
                    int normalIndex = faceNormals.get(j);
                    if (componentIndex == 2) {
                        newFaceNormals.add(indexMapping.getOrDefault(normalIndex, normalIndex));
                    } else {
                        newFaceNormals.add(normalIndex);
                    }
                }
            }

            newFace.setVertexIndices(newFaceVertices);
            newFace.setTextureVertexIndices(newFaceTextures);
            newFace.setNormalIndices(newFaceNormals);

            // Заменяем старую строку полигона на обновленную
            faces.set(i, newFace);
        }
    }


    private static ArrayList<Polygon> cleanFaces(ArrayList<Polygon> faces, Set<Integer> validVertexIndices, Set<Integer> validTextureIndices, Set<Integer> validNormalIndices) {
        ArrayList<Polygon> newFaces = new ArrayList<>();
        for (Polygon face : faces) {
//            String[] faceVertices = face.split(" ");
            boolean isHangingFace = false;
            Polygon newFace = new Polygon();

            ArrayList<Integer> faceVertices = face.getVertexIndices();
            ArrayList<Integer> faceTextures = face.getTextureVertexIndices();
            ArrayList<Integer> faceNormals = face.getNormalIndices();

            ArrayList<Integer> newFaceVertices = new ArrayList<>();
            ArrayList<Integer> newFaceTextures = new ArrayList<>();
            ArrayList<Integer> newFaceNormals = new ArrayList<>();
            for (int j = 0; j < faceVertices.size(); j++) {
//                String[] vertexData = faceVertices[j].split("/");

                int vertexIndex = faceVertices.get(j);
                if (!validVertexIndices.contains(vertexIndex)) {
                    isHangingFace = true;
                    break; // Если вершина невалидна, полигон висячий
                }

                // Проверяем текстурную координату
                if (!faceTextures.isEmpty()) {
                    int textureIndex = faceTextures.get(j);
                    if (!validTextureIndices.contains(textureIndex)) {
                        isHangingFace = true;
                        break;
                    }
                }

                // Проверяем нормаль
                if (!faceNormals.isEmpty()) {
                    int normalIndex = faceNormals.get(j);
                    if (!validNormalIndices.contains(normalIndex)) {
                        isHangingFace = true;
                        break;
                    }
                }
                // Формируем новую строку вершины
                newFaceVertices.add(faceVertices.get(j));
                if (!faceNormals.isEmpty())
                    newFaceTextures.add(faceTextures.get(j));
                if (!faceNormals.isEmpty())
                    newFaceNormals.add(faceNormals.get(j));
            }

            newFace.setVertexIndices(newFaceVertices);
            newFace.setTextureVertexIndices(newFaceTextures);
            newFace.setNormalIndices(newFaceNormals);

            // Добавляем только невисячие полигоны
            if (!newFaceVertices.isEmpty() && !isHangingFace) {
                newFaces.add(newFace);
            }
        }
        return newFaces;
    }

//    private static Set<Integer> collectValidIndices(List<Vector3f> data) {
//        Set<Integer> validIndices = new HashSet<>();
//        for (int i = 0; i < data.size(); i++) {
//            validIndices.add(i); // Существовавшие индексы
//        }
//        return validIndices;
//    }

    private static Set<Integer> collectValidIndices(List<?> data) {
        Set<Integer> validIndices = new HashSet<>();
        for (int i = 0; i < data.size(); i++) {
            validIndices.add(i); // Существовавшие индексы
        }
        return validIndices;
    }

//    private static void writeModel(Writer output, List<String> vertices, List<String> textureVertices, List<String> normals, List<String> faces) throws IOException {
//        try (BufferedWriter writer = new BufferedWriter(output)) {
//            for (String vertex : vertices) {
//                writer.write(vertex);
//                writer.newLine();
//            }
//            for (String textureVertex : textureVertices) {
//                writer.write(textureVertex);
//                writer.newLine();
//            }
//            for (String normal : normals) {
//                writer.write(normal);
//                writer.newLine();
//            }
//            for (String face : faces) {
//                writer.write(face);
//                writer.newLine();
//            }
//        }
//    }

    private static <T> void removeAllUnusedData(List<T> data, Map<Integer, Integer> indexMapping, List<Polygon> faces, int componentIndex) { //Удаляем все текстурные вершины и нормали, которые не используются
        Set<Integer> currentlyUsedIndices = new HashSet<>();
        for (Polygon face : faces) {
            ArrayList<Integer> faceVertices = face.getVertexIndices();
            for (int j = 0; j < faceVertices.size(); j++) {
                if (componentIndex == 1 && !face.getTextureVertexIndices().isEmpty()) {
                    currentlyUsedIndices.add(face.getTextureVertexIndices().get(j));
                }
                else if (componentIndex == 2 && !face.getNormalIndices().isEmpty()) {
                    currentlyUsedIndices.add(face.getNormalIndices().get(j));
                }
            }
        }

        // Удаляем только те, которые стали неиспользуемыми
        List<T> newData = new ArrayList<>();
        int newIndex = 0;
        for (int i = 0; i < data.size(); i++) {
            if (currentlyUsedIndices.contains(i)) {
                newData.add(data.get(i));
                indexMapping.put(i, newIndex++);
            }
        }

        data.clear();
        data.addAll(newData);

        // Обновляем индексы в полигонах
        for (int i = 0; i < faces.size(); i++) {
            Polygon face = faces.get(i);
            Polygon newFace = new Polygon();

            List<Integer> faceVertices = face.getVertexIndices();
            List<Integer> faceTextures = face.getTextureVertexIndices();
            List<Integer> faceNormals = face.getNormalIndices();

            ArrayList<Integer> newFaceVertices = new ArrayList<>();
            ArrayList<Integer> newFaceTextures = new ArrayList<>();
            ArrayList<Integer> newFaceNormals = new ArrayList<>();

            for (int j = 0; j < faceVertices.size(); j++) {

                newFaceVertices.add(faceVertices.get(j));

                if (!faceTextures.isEmpty()) {
                    int textureIndex = faceTextures.get(j);
                    if (componentIndex == 1) {
                        newFaceTextures.add(indexMapping.getOrDefault(textureIndex, textureIndex));
                    } else {
                        newFaceTextures.add(textureIndex);
                    }
                }

                if (!faceNormals.isEmpty()) {
                    int normalIndex = faceNormals.get(j);
                    if (componentIndex == 2) {
                        newFaceNormals.add(indexMapping.getOrDefault(normalIndex, normalIndex));
                    } else {
                        newFaceNormals.add(normalIndex);
                    }
                }
            }

            newFace.setVertexIndices(newFaceVertices);
            newFace.setTextureVertexIndices(newFaceTextures);
            newFace.setNormalIndices(newFaceNormals);

            // Заменяем старую строку полигона на обновленную
            faces.set(i, newFace);
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
