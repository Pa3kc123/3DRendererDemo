package sk.pa3kc.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sk.pa3kc.Program;
import sk.pa3kc.geom.Box;
import sk.pa3kc.geom.Path3D;
import sk.pa3kc.geom.Triangle;
import sk.pa3kc.util.Vertex;
import sk.pa3kc.inter.Drawable;

public class MyPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private MyFrame parent = null;

    //region Redering objects
    private final int boxCount;
    private final int triangleCount;
    private final int path3dCount;
    private final Drawable[][] shapes;
    //endregion

    //region Switches
    private final boolean drawBoxes = true;
    private final boolean drawTriangles = false;
    private final boolean drawPaths = false;
    //endregion

    public MyPanel() {
        super();

        final List<Box> boxList = new ArrayList<Box>(); {
            boxList.add(new Box(new Vertex(-200, -200, -200), 400));

            // //Level 1
            // boxList.add(new Box(new Vertex(-300, 100, -300), 200));
            // boxList.add(new Box(new Vertex(-100, 100, -300), 200));
            // boxList.add(new Box(new Vertex(100, 100, -300), 200));

            // boxList.add(new Box(new Vertex(-300, 100, -100), 200));
            // boxList.add(new Box(new Vertex(-100, 100, -100), 200));
            // boxList.add(new Box(new Vertex(100, 100, -100), 200));

            // boxList.add(new Box(new Vertex(-300, 100, 100), 200));
            // boxList.add(new Box(new Vertex(-100, 100, 100), 200));
            // boxList.add(new Box(new Vertex(100, 100, 100), 200));

            // //Level 2
            // boxList.add(new Box(new Vertex(-200, -100, -200), 200));
            // boxList.add(new Box(new Vertex(0, -100, -200), 200));

            // boxList.add(new Box(new Vertex(-200, -100, 0), 200));
            // boxList.add(new Box(new Vertex(0, -100, 0), 200));

            // //Level 3
            // boxList.add(new Box(new Vertex(-100, -300, -100), 200));

            this.boxCount = boxList.size();
        }

        final List<Triangle> triangleList = new ArrayList<Triangle>(); {
            // Vertex v21 = new Vertex(-200, 200, -200);
            // Vertex v22 = new Vertex(0, 0, 0);
            // Vertex v23 = new Vertex(-200, 200, -200);

            // Vertex v31 = new Vertex(200, 200, -200);
            // Vertex v32 = new Vertex(0, 0, 0);
            // Vertex v33 = new Vertex(-200, 200, -200);

            // triangleList.add(new Triangle(v21, v22, v23, Color.WHITE));
            // triangleList.add(new Triangle(v31, v32, v33, Color.WHITE));

            this.triangleCount = triangleList.size();
        }

        final List<Path3D> path3dList = new ArrayList<Path3D>(); {
            Vertex vx1 = new Vertex(-200, 0, 0);
            Vertex vx2 = new Vertex(200, 0, 0);
            Color cx = Color.GREEN;

            Vertex vy1 = new Vertex(0, -200, 0);
            Vertex vy2 = new Vertex(0, 200, 0);
            Color cy = Color.RED;

            Vertex vz1 = new Vertex(0, 0, -200);
            Vertex vz2 = new Vertex(0, 0, 200);
            Color cz = Color.BLUE;

            path3dList.add(new Path3D(vx1, vx2, cx, "X"));
            path3dList.add(new Path3D(vy1, vy2, cy, "Y"));
            path3dList.add(new Path3D(vz1, vz2, cz, "Z"));

            this.path3dCount = path3dList.size();
        }

        this.shapes = new ArrayList<Drawable[]>() {
            private static final long serialVersionUID = 1L;

            {
                super.add(boxList.toArray(new Drawable[0]));
                super.add(triangleList.toArray(new Drawable[0]));
                super.add(path3dList.toArray(new Drawable[0]));
            }
        }.toArray(new Drawable[0][]);

        super.setOpaque(true);
        super.setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.parent == null)
            this.parent = (MyFrame) SwingUtilities.getAncestorOfClass(MyFrame.class, this);

        g.translate(this.getWidth() / 2, this.getHeight() / 2);
        g.setColor(Color.WHITE);

        if (this.drawBoxes)
        for (int i = 0; i < this.boxCount; i++)
            this.shapes[0][i].draw(g);

        if (this.drawTriangles)
        for (int i = 0; i < this.triangleCount; i++)
            this.shapes[1][i].draw(g);

        if (this.drawPaths)
        for (int i = 0; i < this.path3dCount; i++)
            this.shapes[2][i].draw(g);

        g.setFont(new Font(g.getFont().getName(), g.getFont().getStyle(), 15));
        int stringX = (int)-(super.getWidth() / 2);
        int stringY = (int)-(super.getHeight() / 2) + g.getFont().getSize();
        g.drawString("FPS: ".concat(String.valueOf(Program.uiThread.getFrameCount())), stringX, stringY);

        stringY += g.getFont().getSize();
        g.drawString("UPS: ".concat(String.valueOf(Program.uiThread.getUpdateCount())), stringX, stringY);

        stringY += g.getFont().getSize();
        g.drawString("toggled: ".concat(String.valueOf(Program.toggled)), stringX, stringY);

        if (Program.OS_NAME.contains("linux"))
            Toolkit.getDefaultToolkit().sync();
    }
}
