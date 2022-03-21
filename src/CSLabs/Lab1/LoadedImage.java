package CSLabs.Lab1;

import javax.swing.*;
import java.awt.*;
import java.awt.Container;

public class LoadedImage extends AbstractFigure {
    private final ImageIcon image;
    private final double BOUNCE = -1.0;

    public LoadedImage(Container parent, String filename, int centerX, int centerY) {
        super(parent);

        image = new ImageIcon(filename);
        setSize(image.getIconWidth(), image.getIconHeight());
        setCenter(centerX, centerY);

        velocityX = (Math.random() * 2 - 1) * 100;
        velocityY = (Math.random() * 2 - 1) * 100;
    }

    public LoadedImage(Container parent, String filename, int centerX, int centerY, int width, int height) {
        super(parent);

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

    private void checkWalls() {
        int imageX1 = getX();
        int imageY1 = getY();
        int imageX2 = imageX1 + getWidth();
        int imageY2 = imageY1 + getHeight();

        int canvasX1 = parent.getX();
        int canvasY1 = parent.getY();
        int canvasX2 = canvasX1 + parent.getWidth();
        int canvasY2 = canvasY1 + parent.getHeight();

        if (imageX2 > canvasX2) {
            setCenterX(canvasX2 - getWidth() / 2.0);
            velocityX *= BOUNCE;
        }
        else if (imageX1 < canvasX1) {
            setCenterX(canvasX1 + getWidth() / 2.0);
            velocityX *= BOUNCE;
        }

        if (imageY2 > canvasY2) {
            setCenterY(canvasY2 - getHeight() / 2.0);
            velocityY *= BOUNCE;
        }
        else if (imageY1 < canvasY1) {
            setCenterY(canvasY1 + getHeight() / 2.0);
            velocityY *= BOUNCE;
        }
    }
}
