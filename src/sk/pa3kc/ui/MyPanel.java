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
import sk.pa3kc.util.Vertex;
import sk.pa3kc.inter.Drawable;

public class MyPanel extends JPanel implements Runnable {
    private static final long serialVersionUID = 1L;

    private MyFrame parent = null;

    //region Redering objects
    private final int boxCount;
    private final int path3dCount;
    private final Drawable[][] shapes;
    //endregion

    //region Switches
    private final boolean drawBoxes = true;
    private final boolean drawPaths = false;
    //endregion

    public boolean fpsThreadRunning = false;
    public Thread fpsThread = new Thread(this);

    public MyPanel() {
        super();

        final List<Drawable> boxList = new ArrayList<Drawable>(); {
            // boxList.add(new Box(new Vertex(-100, -100, -100), 200));

            //Level 1
            boxList.add(new Box(new Vertex(-300, 100, -300), 200));
            boxList.add(new Box(new Vertex(-100, 100, -300), 200));
            boxList.add(new Box(new Vertex(100, 100, -300), 200));

            boxList.add(new Box(new Vertex(-300, 100, -100), 200));
            boxList.add(new Box(new Vertex(-100, 100, -100), 200));
            boxList.add(new Box(new Vertex(100, 100, -100), 200));

            boxList.add(new Box(new Vertex(-300, 100, 100), 200));
            boxList.add(new Box(new Vertex(-100, 100, 100), 200));
            boxList.add(new Box(new Vertex(100, 100, 100), 200));

            //Level 2
            boxList.add(new Box(new Vertex(-200, -100, -200), 200));
            boxList.add(new Box(new Vertex(0, -100, -200), 200));

            boxList.add(new Box(new Vertex(-200, -100, 0), 200));
            boxList.add(new Box(new Vertex(0, -100, 0), 200));

            //Level 3
            boxList.add(new Box(new Vertex(-100, -300, -100), 200));

            this.boxCount = boxList.size();
        }

        final List<Drawable> path3dList = new ArrayList<Drawable>(); {
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
                super.add(path3dList.toArray(new Drawable[0]));
            }
        }.toArray(new Drawable[0][]);

        super.setOpaque(true);
        super.setBackground(Color.BLACK);

        this.fpsThread.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.parent == null)
            this.parent = (MyFrame) SwingUtilities.getAncestorOfClass(MyFrame.class, this);

        g.translate(this.getWidth() / 2, this.getHeight() / 2);
        g.setColor(Color.WHITE);

        if (this.drawBoxes == true)
        for (int i = 0; i < boxCount; i++)
            this.shapes[0][i].draw(g, this.parent.rotationMatrix, this.parent.distance);

        if (this.drawPaths == true)
        for (int i = 0; i < path3dCount; i++)
            this.shapes[1][i].draw(g, this.parent.rotationMatrix, this.parent.distance);

        g.setFont(new Font(g.getFont().getName(), g.getFont().getStyle(), 15));
        int stringX = (int)-(super.getWidth() / 2);
        int stringY = (int)-(super.getHeight() / 2) + g.getFont().getSize();
        g.drawString("FPS: ".concat(String.valueOf(this.framesPerSecond)), stringX, stringY);

        stringY += g.getFont().getSize();
        g.drawString("Updates: ".concat(String.valueOf(this.updatesPerSecond)), stringX, stringY);

        stringY += g.getFont().getSize();
        g.drawString("toggled: ".concat(String.valueOf(Program.toggled)), stringX, stringY);

        if (Program.OS_NAME.contains("linux") == true)
            Toolkit.getDefaultToolkit().sync();

        this.frameCount++;
    }

    private int framesPerSecond = 0;
    private int updatesPerSecond = 0;
    private int frameCount = 0;

    @Override
    public void run() {
        this.fpsThreadRunning = true;
        while (this.fpsThreadRunning == true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            this.framesPerSecond = this.frameCount;
            this.updatesPerSecond = this.parent.updateCount;

            this.frameCount = 0;
            this.parent.updateCount = 0;
        }
    }
}
