package main.java;

import main.java.com.cgvsu.model.Model;
import main.java.com.cgvsu.objreader.ObjReader;
import main.java.com.cgvsu.objwriter.ObjWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
//        Path fileName1 = Path.of("src\\main\\java\\3DModels\\CaracalCube\\caracal_cube.obj");
//        String fileContent1 = Files.readString(fileName1);
//
//        System.out.println("Loading model ...");
//        Model model1 = ObjReader.read(fileContent1);
//
//        System.out.println("Vertices: " + model1.vertices.size());
//        System.out.println("Texture vertices: " + model1.textureVertices.size());
//        System.out.println("Normals: " + model1.normals.size());
//        System.out.println("Polygons: " + model1.polygons.size());



        Path fileName = Path.of("src\\main\\java\\3DModels\\CaracalCube\\caracal_cube.obj");
        String fileContent = Files.readString(fileName);

        System.out.println("Loading model ...");
        Model model = ObjReader.read(fileContent);

        System.out.println("Vertices: " + model.vertices.size());
        System.out.println("Texture vertices: " + model.textureVertices.size());
        System.out.println("Normals: " + model.normals.size());
        System.out.println("Polygons: " + model.polygons.size());

        PolygonRemover.processModelAndCleanPolygons(model, List.of(), true, true, true);

        ObjWriter writer = new ObjWriter();
        writer.write(model, "C:\\Users\\Vladislav\\3D Objects\\CorrectedCubeWithRemovedVerticesPolygonRemover4.obj");

        System.out.println("Vertices: " + model.vertices.size());
        System.out.println("Texture vertices: " + model.textureVertices.size());
        System.out.println("Normals: " + model.normals.size());
        System.out.println("Polygons: " + model.polygons.size());
    }
}
