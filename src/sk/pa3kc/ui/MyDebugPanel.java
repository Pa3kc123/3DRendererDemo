package sk.pa3kc.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sk.pa3kc.util.Matrix;

public class MyDebugPanel extends JComponent {
    private static final long serialVersionUID = 1L;
    private final int rowCount;
    private final int colCount;
    private final JLabel titleLabel;
    private final JLabel[] labels;

    public MyDebugPanel(int rowCount, int colCount, String name) {
        super();
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.labels = new JLabel[this.rowCount * this.colCount];

        super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.titleLabel = new JLabel(name);
        this.titleLabel.setHorizontalAlignment(JLabel.CENTER);
        super.add(this.titleLabel);

        Container tmpContainer = new Container();
        tmpContainer.setLayout(new GridLayout(this.rowCount, this.colCount, 5, 5));
        for (int row = 0; row < this.rowCount; row++)
        for (int col = 0; col < this.colCount; col++) {
            JLabel ref = (this.labels[row * this.colCount + col] = new JLabel());
            ref.setHorizontalAlignment(JLabel.CENTER);
            ref.setPreferredSize(new Dimension(100, 50));
            ref.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            ref.setOpaque(true);
            ref.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
            tmpContainer.add(ref);
        }
        super.add(tmpContainer);
    }

    public void updateValues(Matrix updatedMatrix) {
        if (this.rowCount != updatedMatrix.getRowCount() || this.colCount != updatedMatrix.getColCount()) return;

        for (int row = 0; row < this.rowCount; row++)
        for (int col = 0; col < this.colCount; col++) {
            JLabel label = this.labels[row * this.colCount + col];
            label.setBackground(updatedMatrix.getValues()[row][col] > 0 ? Color.GREEN : updatedMatrix.getValues()[row][col] < 0 ? Color.RED : Color.YELLOW);
            label.setText(String.format("%+.2f", updatedMatrix.getValues()[row][col]));
            label.paintImmediately(super.getBounds());
        }
    }

    public static void show(MyDebugPanel[][] panels) {
        if (panels == null || panels.length == 0 || panels[0].length == 0) return;

        JFrame frame = new JFrame();
        JPanel container = (JPanel)frame.getContentPane();
        container.setLayout(new GridLayout(panels.length, panels[0].length, 5, 5));

        for (int row = 0; row < panels.length; row++)
        for (int col = 0; col < panels[row].length; col++) {
            panels[row][col].setBorder(BorderFactory.createLineBorder(Color.CYAN));
            container.add(panels[row][col]);
        }

        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
