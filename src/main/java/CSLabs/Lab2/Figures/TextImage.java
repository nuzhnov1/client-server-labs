package CSLabs.Lab2.Figures;

import java.awt.*;

@SuppressWarnings({"unused", "DuplicatedCode"})
public class TextImage extends Figure {
    @SuppressWarnings("FieldCanBeLocal")
    private final int RADIUS = 10;

    private final String text;
    private final double initX, initY;


    public TextImage(String text, double centerX, double centerY) {
        this.text = text;
        initX = centerX;
        initY = centerY;

        setCenter(initX, initY);
    }


    public String getText() { return text; }
    public double getInitX() { return initX; }
    public double getInitY() { return initY; }


    private void checkArea() {
        if (Math.sqrt(Math.pow(centerX - initX, 2) + Math.pow(centerY - initY, 2)) >= RADIUS) {
            double newX = RADIUS / Math.sqrt(1 + Math.pow(centerY - initY, 2) / Math.pow(centerX - initX, 2)) + initX;
            double newY = (newX - initX) * (centerY - initY) / (centerX - initX) + initY;

            setCenter(newX, newY);
        }
    }


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

        velocityX = Utilities.randomRange(-MAX_SPEED, MAX_SPEED);
        velocityY = Utilities.randomRange(-MAX_SPEED, MAX_SPEED);

        setCenter(
                centerX + time * velocityX,
                centerY + time * velocityY
        );
        checkArea();
        checkWalls();
    }
}
