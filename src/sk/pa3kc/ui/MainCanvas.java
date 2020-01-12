package sk.pa3kc.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import sk.pa3kc.Program;
import sk.pa3kc.World;
import sk.pa3kc.matrix.MatrixMath;
import sk.pa3kc.pojo.Matrix;
import sk.pa3kc.pojo.Vertex;

public class MainCanvas extends DoubleBufferedCanvas {
    private static final long serialVersionUID = 1L;

    private final int FONT_SIZE = 15;
    private final Font FONT = new Font("Dialog", Font.PLAIN, FONT_SIZE);

    private final World world;

    private float angleX = 0f;
    private float deltaX = 0f;
    private float angleY = 0f;
    private float deltaY = 0f;
    private float angleZ = 0f;
    // private float deltaZ = 0f;

    public MainCanvas(World world) {
        super();

        this.world = world;

        final class MyMouseAdapter implements MouseListener, MouseMotionListener, MouseWheelListener {
            @Override public void mouseWheelMoved(MouseWheelEvent e) {}
            @Override public void mouseDragged(MouseEvent e) {}
            @Override public void mouseMoved(MouseEvent e) {}
            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        }
        final MyMouseAdapter adapter = new MyMouseAdapter();
        super.addMouseListener(adapter);
        super.addMouseMotionListener(adapter);
        super.addMouseWheelListener(adapter);

        final class MyKeyAdapter implements KeyListener {
            @Override public void keyTyped(KeyEvent e) {}
            @Override public void keyPressed(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
        }
        super.addKeyListener(new MyKeyAdapter());

        Program.UI_THREAD.getUpdatables().add(new Runnable() {
            @Override
            public void run() {
                if (!mouse1Pressed) {
                    if (deltaX > 0) {
                        updateXRotation(deltaX -= 1);
                    } else if (deltaX < 0) {
                        updateXRotation(deltaX += 1);
                    }

                    if (deltaY > 0) {
                        updateYRotation(deltaY -= 1);
                    } else if (deltaY < 0) {
                        updateYRotation(deltaY += 1);
                    }
                }
            }
        });
        Program.UI_THREAD.getUpdatables().add(new Runnable() {
            @Override
            public void run() {
                MatrixMath.applyRotationX(Matrix.X_MATRIX.getAllValues(), (angleX / 180f * 3.14159f));
                MatrixMath.applyRotationY(Matrix.Y_MATRIX.getAllValues(), (angleY / 180f * 3.14159f));
                // MatrixMath.applyRotationZ(Matrix.Z_MATRIX.getAllValues(), Matrix.ROTATION_MATRIX.getAllValues(), (angleZ / 180f * 3.14159f));
                MatrixMath.identify(Matrix.ROTATION_MATRIX.getAllValues());
                MatrixMath.multiply(Matrix.X_MATRIX.getAllValues(), Matrix.Y_MATRIX.getAllValues(), Matrix.ROTATION_MATRIX.getAllValues());
            }
        });

        Program.UI_THREAD.getRenderables().add(new Runnable() {
            @Override
            public void run() {
                repaint();
            }
        });

        super.setForeground(Color.WHITE);
        super.setBackground(Color.BLACK);
    }

    public void updateXRotation(float delta) {
        this.angleX += delta;
        if (this.angleX >= 360f) {
            this.angleX -= 360f;
        } else if (this.angleX < 0f) {
            this.angleX += 359;
        }
    }
    public void updateYRotation(float delta) {
        this.angleY += delta;
        if (this.angleY >= 360f) {
            this.angleY -= 360f;
        } else if (this.angleY < 0f) {
            this.angleY += 359;
        }
    }
    public void updateZRotation(float delta) {
        this.angleZ += delta;
        if (this.angleZ >= 360f) {
            this.angleZ -= 360f;
        } else if (this.angleZ < 0f) {
            this.angleZ += 359;
        }
    }

    @Override
    public void paintBuffer(Graphics2D g) {
        world.draw(g);

        g.setFont(FONT);

        final String[] infoArr = new String[] {
            ("FPS: " + Program.UI_THREAD.getFPS()),
            ("UPS: " + Program.UI_THREAD.getUPS()),
            ("AngleX: " + this.angleX),
            ("AngleY: " + this.angleY),
            ("AngleZ: " + this.angleZ)
        };

        for (int i = 0; i < infoArr.length; i++) {
            g.drawString(infoArr[i], 0, (FONT_SIZE * (i+1)));
        }
    }

    private boolean mouse1Pressed = false;
    private boolean mouse2Pressed = false;
    private boolean mouse3Pressed = false;
    private int lastX = 0;
    private int lastY = 0;

    @Override
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        switch (e.getID()) {
            case MouseEvent.MOUSE_PRESSED:
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        if (!this.mouse1Pressed) {
                            this.lastX = e.getX();
                            this.lastY = e.getY();
                            this.mouse1Pressed = true;
                        }
                    break;
                    case MouseEvent.BUTTON2:
                        if (!this.mouse2Pressed) {
                            this.mouse2Pressed = true;
                        }
                    break;
                    case MouseEvent.BUTTON3:
                        if (!this.mouse3Pressed) {
                            this.mouse3Pressed = true;
                        }
                    break;
                }
            break;
            case MouseEvent.MOUSE_RELEASED:
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        if (this.mouse1Pressed) {
                            this.lastX = -1;
                            this.lastY = -1;
                            this.mouse1Pressed = false;
                        }
                    break;
                    case MouseEvent.BUTTON2:
                        if (this.mouse2Pressed) {
                            this.mouse2Pressed = false;
                        }
                    break;
                    case MouseEvent.BUTTON3:
                        if (this.mouse3Pressed) {
                            this.mouse3Pressed = false;
                        }
                    break;
                }
            break;
        }

        super.processMouseEvent(e);
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        if (this.mouse1Pressed) {
            this.deltaX = e.getY() - this.lastY;
            this.updateXRotation(this.deltaX);

            this.deltaY = e.getX() - this.lastX;
            this.updateYRotation(this.deltaY);

            this.lastX = e.getX();
            this.lastY = e.getY();
        }

        super.processMouseMotionEvent(e);
    }

    @Override
    protected void processMouseWheelEvent(MouseWheelEvent e) {
        super.processMouseWheelEvent(e);
        final Vertex playerLocation = Program.world.getPlayers()[0].getLocation();
        playerLocation.setZ(playerLocation.getZ() + (e.getWheelRotation() * 20));
    }
}
