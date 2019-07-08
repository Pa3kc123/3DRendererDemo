package sk.pa3kc;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import sk.pa3kc.ui.MyFrame;

public class Program
{
    public static final String NEWLINE = System.getProperty("line.separator");
    public static final String OS_NAME = System.getProperty("os.name");

    public static Object globalLock = new Object();

    private static Program instance;

    public final int CHOOSEN_GRAPHICS_DEVICE;
    public final GraphicsConfiguration GRAPHICS_DEVICE_CONFIG;
    public final Rectangle GRAPHICS_DEVICE_BOUNDS;

    private static MyFrame mainFrame = null;

    public static int framesPerSecond = 0;
    public static int updatesPerSecond = 0;

    public boolean uiThreadRunning = false;
    public final Thread uiThread = new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            uiThreadRunning = true;
            try
            {
                while (uiThreadRunning == true)
                {
                    Runnable runnable = new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mainFrame.myPanel.repaint();
                        }
                    };
                    SwingUtilities.invokeAndWait(runnable);
                }
            }
            catch (InvocationTargetException ex) { ex.printStackTrace(); }
            catch (InterruptedException ex) { ex.printStackTrace(); }
        }
    });

    private Program(int choosenGraphicsDevice)
    {
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        this.CHOOSEN_GRAPHICS_DEVICE = choosenGraphicsDevice < 0 ? 0 : choosenGraphicsDevice > devices.length - 1 ? devices.length - 1 : choosenGraphicsDevice;
        this.GRAPHICS_DEVICE_CONFIG = devices[CHOOSEN_GRAPHICS_DEVICE].getDefaultConfiguration();
        this.GRAPHICS_DEVICE_BOUNDS = this.GRAPHICS_DEVICE_CONFIG.getBounds();
    }

    public static Program getInst() { return instance; }

    public static <T> T safeCast(Object value, Class<T> type)
    {
        try { return type.cast(value); }
        catch (ClassCastException ignored) { return null; }
    }

    public static void main(String... args)
    {
        System.setProperty("sun.java2d.opengl", "false");
        instance = new Program(args.length != 0 ? Integer.parseInt(args[0]) - 1 : 0);

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                mainFrame = new MyFrame("Test2");
            }
        });
        synchronized (globalLock) {
            try
            {
                globalLock.wait();
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }

            instance.uiThread.start();
        }
    }
    public static void ERROR(Class<?> tag, String message) { System.out.println("ERROR: ".concat(tag.getSimpleName()).concat(": ").concat(message)); }
    public static void DEBUG(Class<?> tag, String message) { System.out.println("DEBUG: ".concat(tag.getSimpleName()).concat(": ").concat(message)); }
    public static void INFO(Class<?> tag, String message) { System.out.println("INFO: ".concat(tag.getSimpleName()).concat(": ").concat(message)); }
    public static int min(int... values)
    {
        int result = Integer.MAX_VALUE;

        for (int value : values)
        if (result > value)
            result = value;

        return result;
    }
    public static int max(int... values)
    {
        int result = Integer.MAX_VALUE;

        for (int value : values)
        if (result < value)
            result = value;

        return result;
    }

    /*@Override
    public void run()
    {
        JFrame frame = new JFrame();
        JPanel container = (JPanel)frame.getContentPane();

        TmpClass clazz = new TmpClass();
        container.add(clazz);
        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation((Program.getInst().GRAPHICS_DEVICE_BOUNDS.width / 2) - (frame.getPreferredSize().width / 2), (Program.getInst().GRAPHICS_DEVICE_BOUNDS.height / 2) - (frame.getPreferredSize().height / 2));
        frame.setVisible(true);

        clazz.start();
    }*/
}

/*class TmpClass extends Canvas implements Runnable
{
    private static final long serialVersionUID = 1L;
    private static final int SCALE = 4;
    private static final double FRAME_LIMIT = 60d;

    private boolean isRunning = false;
    private Thread renderThread = null;

    private BufferedImage image = null;
    private int pixels[] = null;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 500;

    TmpClass()
    {
        super();
        super.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        if (this.image.getColorModel().equals(Program.getInst().GRAPHICS_DEVICE_CONFIG.getColorModel()) == false)
        {
            BufferedImage newImage = Program.getInst().GRAPHICS_DEVICE_CONFIG.createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());

            Graphics2D graphics = newImage.createGraphics();

            graphics.drawImage(this.image, 0, 0, null);
            graphics.dispose();

            this.image = newImage;
        }

        this.pixels = ((DataBufferInt)this.image.getRaster().getDataBuffer()).getData();
    }

    public void start()
    {
        if (this.isRunning == false)
            this.isRunning = true;

        this.renderThread = new Thread(this);
        this.renderThread.start();
    }

    private void stop()
    {

    }

    private void render()
    {
        BufferStrategy strategy = super.getBufferStrategy();

        if (strategy == null)
        {
            super.createBufferStrategy(3);
            return;
        }

        Graphics graphics = strategy.getDrawGraphics();

        for (int i = 0; i < this.pixels.length; i++)
        {
            pixels[i] = 0xFF00FF;
        }

        graphics.drawImage(this.image, 0, 0, this.getWidth() * SCALE, this.getHeight() * SCALE, null);

        graphics.dispose();
        strategy.show();
    }

    private void update()
    {

    }

    private void dispose()
    {
        System.exit(0);
    }

    @Override
    public void run()
    {
        final double nsPerUpdate = 1000000000.0 / FRAME_LIMIT;

        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        int frameCount = 0;
        int updateCount = 0;

        long fpsCounter = System.currentTimeMillis();

        while (this.isRunning == true)
        {
            long currentTime = System.nanoTime();
            long passedTime = currentTime - lastTime;
            lastTime = currentTime;
            unprocessedTime += passedTime;

            if (unprocessedTime >= nsPerUpdate)
            {
                unprocessedTime = 0;
                this.update();
                updateCount++;
            }

            this.render();
            frameCount++;

            if (System.currentTimeMillis() - fpsCounter >= 1000)
            {
                System.out.println("Frames:" + frameCount + " Updates: " + updateCount);
                frameCount = 0;
                updateCount = 0;
                fpsCounter += 1000;
            }
        }
        this.dispose();
    }
}*/
