package sk.pa3kc;

import java.io.File;
import java.nio.IntBuffer;
import java.util.Arrays;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import sk.pa3kc.geom.Triangle3D;
import sk.pa3kc.matrix.MatrixMath;
import sk.pa3kc.pojo.Vertex;
import sk.pa3kc.util.ObjFile;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * This class is a simple quick starting guide. This is mainly a java conversion
 * of the
 * <a href=http://www.glfw.org/docs/latest/quick.html>Getting started guide</a>
 * from the official GLFW3 homepage.
 *
 * @author Heiko Brumme
 */
public class Tmp {

    /**
     * This error callback will simply print the error to
     * <code>System.err</code>.
     */
    private static GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);

    /**
     * This key callback will check if ESC is pressed and will close the window
     * if it is pressed.
     */
    private static GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true);
            }
        }
    };

    /**
     * The main function will create a 640x480 window and renders a rotating
     * triangle until the window gets closed.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) return;

        final File file = new File(args[0]);
        final ObjFile objFile;

        try {
            objFile = new ObjFile(file);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return;
        }

        Program.world = new World(1);
        Program.world.getPlayers()[0] = new Player(new Vertex(0f, 0f, 300f, 1f));
        Program.world.getMesh().addAll(Arrays.asList(objFile.getFaces()));

        long window;

        /* Set the error callback */
        glfwSetErrorCallback(errorCallback);

        /* Initialize GLFW */
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        /* Create window */
        window = glfwCreateWindow(640, 480, "Simple example", NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        /* Center the window on screen */
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidMode.width() - 640) / 2, (vidMode.height() - 480) / 2);

        /* Create OpenGL context */
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        /* Enable vertical synchronization */
        glfwSwapInterval(1);

        /* Set the key callback */
        glfwSetKeyCallback(window, keyCallback);

        /* Declare buffers for using inside the loop */
        IntBuffer width = MemoryUtil.memAllocInt(1);
        IntBuffer height = MemoryUtil.memAllocInt(1);

        /* Loop until window gets closed */
        while (!glfwWindowShouldClose(window)) {
            float ratio;

            /* Get width and height to calcualte the ratio */
            glfwGetFramebufferSize(window, width, height);
            ratio = width.get() / (float) height.get();

            /* Rewind buffers for next get */
            width.rewind();
            height.rewind();

            /* Set viewport and clear screen */
            glViewport(0, 0, width.get(), height.get());
            glClear(GL_COLOR_BUFFER_BIT);

            /* Set ortographic projection */
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(-ratio, ratio, -1f, 1f, 1f, -1f);
            glMatrixMode(GL_MODELVIEW);

            /* Rotate matrix */
            glLoadIdentity();
            glRotatef(27.5f, 1f, 0f, 0f);
            glRotatef((float) glfwGetTime() * 50f, 0f, 1f, 0f);

            glColor3f(1f, 1f, 1f);

            for (final Triangle3D face : objFile.getFaces()) {
                glBegin(GL_LINES);
                for (final Vertex vertex : face.vertecies) {
                    final float[][] tmp = vertex.cloneAllValues();
                    MatrixMath.normalize(tmp);
                    // glColor3f(1f, 0f, 0f);
                    glVertex3f(tmp[0][0], tmp[1][0], tmp[2][0]);
                    // glColor3f(0f, 1f, 0f);
                    // glVertex2f(0.6f, -0.4f);
                    // glColor3f(0f, 0f, 1f);
                    // glVertex2f(0f, 0.6f);
                }
                glEnd();
            }

            /* Swap buffers and poll Events */
            glfwSwapBuffers(window);
            glfwPollEvents();

            /* Flip buffers for next loop */
            width.flip();
            height.flip();
        }

        /* Free buffers */
        MemoryUtil.memFree(width);
        MemoryUtil.memFree(height);

        /* Release window and its callbacks */
        glfwDestroyWindow(window);
        keyCallback.free();

        /* Terminate GLFW and release the error callback */
        glfwTerminate();
        errorCallback.free();
    }
}

class Coord {
    public final float x;
    public final float y;

    public Coord(float x, float y) {
        this.x = x;
        this.y = y;
    }
}

class TmpTriangle {
    private Coord v1;
    private Coord v2;
    private Coord v3;

    public TmpTriangle(Coord v1, Coord v2, Coord v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public void drawGL() {
        glBegin(GL_TRIANGLES);
        glVertex2f(-0.6f, -0.4f);
        glVertex2f(0.6f, -0.4f);
        glVertex2f(0f, 0.6f);
        glEnd();
    }
}
