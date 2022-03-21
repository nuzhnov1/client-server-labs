package CSLabs.Lab1;

import java.awt.*;
import java.awt.Container;

public class TextImage extends AbstractFigure {
    private final String text;

    TextImage(Container parent, String text, int centerX, int centerY) {
        super(parent);

        this.text = text;
        setCenter(centerX, centerY);
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

        velocityX = (Math.random() * 2 - 1) * 100;
        velocityY = (Math.random() * 2 - 1) * 100;

        setCenterX(centerX + time * velocityX);
        setCenterY(centerY + time * velocityY);
    }

    private void checkLeaveBorder() {

    }
}
