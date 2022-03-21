package CSLabs.Lab1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainPanel extends JPanel implements MouseListener, KeyListener {
    protected boolean isMove;

    public MainPanel() {
        this.isMove = true;

        setBackground(new Color(0, 128, 128));
        setBorder(BorderFactory.createLineBorder(new Color(128, 0, 128), 3, true));
        setEnabled(true);
        setVisible(true);
        setFocusable(true);
        setLayout(null);

        addMouseListener(this);
        addKeyListener(this);

        Thread moveController = new Thread(() -> {
            try { moveControl(); }
            catch (InterruptedException ignored) {}
        });

        moveController.start();
    }

    public void setMove(boolean isMove) { this.isMove = isMove; }

    public boolean isMove() { return isMove; }

    public void moveControl() throws InterruptedException {
        final double dt = 1.0 / Main.FPS;

        while (true) {
            if (isMove) {
                for (var component : getComponents()) {
                    if (component instanceof LoadedImage)
                        ((LoadedImage)component).move(dt);
                    else if (component instanceof TextImage)
                        ((TextImage)component).move(dt);
                }
            }
            Thread.sleep((long) (dt * 1000));
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (var component : getComponents())
            component.paint(g);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
            add(new LoadedImage(
                    this, "/home/andrey/projects/java/client-server/images/Nut.png", mouseEvent.getX(), mouseEvent.getY(),
                    100, 100
            ));
        }

        if (SwingUtilities.isRightMouseButton(mouseEvent)) {
            add(new TextImage(this, "Справедливо", mouseEvent.getX(), mouseEvent.getY()));
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}
    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getExtendedKeyCode() == KeyEvent.VK_S)
            setMove(!isMove);

        if (e.getExtendedKeyCode() == KeyEvent.VK_D) {
            removeAll();
            setMove(true);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}
}
