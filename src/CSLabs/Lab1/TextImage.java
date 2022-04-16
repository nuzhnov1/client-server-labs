package CSLabs.Lab1;

import java.awt.*;

public class TextImage extends AbstractFigure {
    private final int RADIUS = 10;

    private final String text;
    private final int initX, initY;

    TextImage(String text, int centerX, int centerY) {
        this.text = text;
        this.initX = centerX;
        this.initY = centerY;

        setCenter(initX, initY);
    }

    public int getInitX() { return initX; }
    public int getInitY() { return initY; }

    @Override
    public void paint(Graphics g) {
        int stringWidth = g.getFontMetrics().stringWidth(text);
        int stringHeight = g.getFontMetrics().getHeight();

        setSize(stringWidth, stringHeight);
        setCenter(centerX, centerY);
        g.drawString(text, getX(), getY());
    }

    @Override
    public void move(double time) {
        if (!isMove)
            return;

        velocityX = (Math.random() * 2 - 1) * 100;
        velocityY = (Math.random() * 2 - 1) * 100;

        setCenterX(centerX + time * velocityX);
        setCenterY(centerY + time * velocityY);

        checkArea();
        checkWalls();
    }

    private void checkArea() {
        if (Math.sqrt(Math.pow(centerX - initX, 2) + Math.pow(centerY - initY, 2)) >= RADIUS) {
            double newX = RADIUS / Math.sqrt(1 + Math.pow(centerY - initY, 2) / Math.pow(centerX - initX, 2)) + initX;
            double newY = (newX - initX) * (centerY - initY) / (centerX - initX) + initY;

            setCenter(newX, newY);
        }
    }
}
