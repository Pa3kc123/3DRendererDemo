package sk.pa3kc.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSlider;

import sk.pa3kc.enums.UpdateMode;
import sk.pa3kc.inter.Updatable;

public class MySlider extends JSlider {
    private static final long serialVersionUID = 1L;
    private final int defaultValue;
    private final Updatable parent;
    private final UpdateMode plane;

    public MySlider(int min, int max, int def, Updatable parent, UpdateMode plane) {
        super(min, max, def);
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3)
                    setValue(defaultValue);
            }
        });
        this.defaultValue = def;
        this.parent = parent;
        this.plane = plane;
    }

    public int getDefault() { return this.defaultValue; }

    @Override
    public void setValue(int n) {
        super.setValue(n);
        // this.parent.update(this.plane);
    }
}
