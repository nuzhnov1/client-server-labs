package CSLabs.Lab1;

import javax.swing.*;
import java.awt.*;

public class LoadedImage extends AbstractFigure {
    private final ImageIcon image;

    public LoadedImage(Container parent, String filename, int centerX, int centerY) {
        image = new ImageIcon(filename);
        setSize(image.getIconWidth(), image.getIconHeight());
        setCenter(centerX, centerY);

        velocityX = (Math.random() * 2 - 1) * 100;
        velocityY = (Math.random() * 2 - 1) * 100;
    }

    public LoadedImage(Container parent, String filename, int centerX, int centerY, int width, int height) {
        image = new ImageIcon(filename);
        setSize(width, height);
        setCenter(centerX, centerY);

        velocityX = (Math.random() * 2 - 1) * 100;
        velocityY = (Math.random() * 2 - 1) * 100;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image.getImage(), getX(), getY(), getWidth(), getHeight(), null);
    }

    @Override
    public void move(double time) {
        if (!isMove)
            return;

        checkWalls();
        setCenterX(centerX + time * velocityX);
        setCenterY(centerY + time * velocityY);
    }
}
