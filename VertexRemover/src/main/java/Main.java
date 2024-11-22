import com.cgvsu.model.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {

        Path fileName = Path.of("src\\main\\java\\3DModels\\Faceform\\WrapHead.obj");
//        Path fileName = Path.of(System.getProperty("user.dir"));
        System.out.println(fileName);
        String fileContent = Files.readString(fileName);

        System.out.println("Loading model ...");
        Model model = com.cgvsu.objreader.ObjReader.read(fileContent);

        System.out.println("Vertices: " + model.vertices.size());
        System.out.println("Texture vertices: " + model.textureVertices.size());
        System.out.println("Normals: " + model.normals.size());
        System.out.println("Polygons: " + model.polygons.size());
    }
}
