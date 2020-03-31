package sk.pa3kc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sk.pa3kc.geom.Triangle3D;
import sk.pa3kc.mylibrary.util.StreamUtils;
import sk.pa3kc.pojo.Vertex;

public class ObjFile {
    private final float[][] vertices;
    private final float[][] vertexNormals;
    private final float[][] textureCoords;
    private final Triangle3D[] faces;

    public ObjFile(File objFile) throws FileNotFoundException, IOException {
        if (!objFile.exists()) {
            throw new FileNotFoundException();
        }

        final List<float[]> vertices = new ArrayList<float[]>();
        final List<float[]> vertexNormals = new ArrayList<float[]>();
        final List<float[]> textureCoords = new ArrayList<float[]>();
        final List<Triangle3D> faces = new ArrayList<Triangle3D>();

        FileReader fr = null;
        BufferedReader br = null;

        try {
            fr = new FileReader(objFile);
            br = new BufferedReader(fr);

            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.length() == 0) {
                    continue;
                }
                final String[] splits = line.split("\\s+");

                if (line.startsWith("v") || line.startsWith("v") || line.startsWith("v")) {
                    final float[] tmp = new float[3];

                    //Vertex normals
                    if (line.startsWith("vn")) {
                        tmp[0] = Float.parseFloat(splits[1]);
                        tmp[1] = Float.parseFloat(splits[2]);
                        tmp[2] = Float.parseFloat(splits[3]);

                        vertexNormals.add(tmp);
                        continue;
                    }

                    //Texture coords
                    if (line.startsWith("vt")) {
                        tmp[0] = Float.parseFloat(splits[1]);
                        tmp[1] = Float.parseFloat(splits[2]);
                        tmp[2] = Float.parseFloat(splits[3]);

                        textureCoords.add(tmp);
                        continue;
                    }

                    // Vertecies
                    if (line.startsWith("v")) {
                        tmp[0] = Float.parseFloat(splits[1]);
                        tmp[1] = Float.parseFloat(splits[2]);
                        tmp[2] = Float.parseFloat(splits[3]);

                        vertices.add(tmp);
                        continue;
                    }

                    continue;
                }

                //Faces
                if (line.startsWith("f")) {
                    final int[] ids = new int[3];

                    for (int i = 1; i <= 3; i++) {
                        ids[i-1] = Integer.parseInt(splits[i].split("\\/")[0]) - 1;
                    }

                    faces.add(
                        new Triangle3D(
                            new Vertex(vertices.get(ids[0])[0], vertices.get(ids[0])[1], vertices.get(ids[0])[2]),
                            new Vertex(vertices.get(ids[1])[0], vertices.get(ids[1])[1], vertices.get(ids[1])[2]),
                            new Vertex(vertices.get(ids[2])[0], vertices.get(ids[2])[1], vertices.get(ids[2])[2])
                        )
                    );
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            StreamUtils.closeStreams(br, fr);
        }

        this.vertices = vertices.toArray(new float[0][]);
        this.vertexNormals = vertexNormals.toArray(new float[0][]);
        this.textureCoords = textureCoords.toArray(new float[0][]);
        this.faces = faces.toArray(new Triangle3D[0]);
    }

    public float[][] getVertices() {
        return this.vertices;
    }
    public float[][] getVertexNormals() {
        return this.vertexNormals;
    }
    public float[][] getTextureCoords() {
        return this.textureCoords;
    }
    public Triangle3D[] getFaces() {
        return this.faces;
    }
}
