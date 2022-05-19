package CSLabs.Lab4.MainPanel;

import CSLabs.Lab4.Figures.Figure;
import CSLabs.Lab4.MenuBar.MenuBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

@SuppressWarnings({"unused", "DuplicatedCode"})
public class MainPanel extends JPanel {
    // Constants:

    private static MainPanel instance = null;

    // Data members:

    private final Controller controller = new Controller(this);

    // Constructors:

    private MainPanel() {
        setBackground(new Color(0, 128, 128));
        setBorder(BorderFactory.createLineBorder(new Color(128, 0, 128), 3, true));
        setEnabled(true);
        setVisible(true);
        setFocusable(true);
        setLayout(null);

        addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                MenuBar menuBar = MenuBar.getInstance();
                File imageFile = menuBar.getImageFile();
                String imagedText = menuBar.getImagedText();

                int initX = e.getX();
                int initY = e.getY();

                if (SwingUtilities.isLeftMouseButton(e))
                    controller.addImage(imageFile, initX, initY, 100, 100);

                if (SwingUtilities.isRightMouseButton(e)) {
                    controller.addImagedText(imagedText, initX, initY);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) {
                boolean isMove = controller.isMove();

                if (e.getKeyCode() == KeyEvent.VK_S)
                    controller.setMove(!isMove);

                if (e.getKeyCode() == KeyEvent.VK_D) {
                    controller.removeAllFigures();
                    controller.setMove(true);
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
        });

        controller.startMoveControl();
    }

    public static MainPanel getInstance() {
        if (instance == null)
            instance = new MainPanel();

        return instance;
    }

    // Override methods:

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (var component : getComponents())
            component.paint(g);
    }

    @Override
    public void remove(Component component) {
        controller.removeFigure((Figure) component);
        super.remove(component);
    }

    // Getters:

    public Controller getController() { return controller; }
}
