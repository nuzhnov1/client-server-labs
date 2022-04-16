package CSLabs.Lab1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class AbstractFigure extends JComponent implements MouseListener, IMovable
{
    protected final double BOUNCE = -1.0;

    protected double centerX, centerY;
    protected double velocityX, velocityY;
    protected boolean isMove;

    public AbstractFigure() {
        isMove = true;

        setVisible(true);
        setEnabled(true);
        addMouseListener(this);
    }


    public double getCenterX() { return centerX; }
    public double getCenterY() { return centerY; }
    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }


    public void setCenterX(double centerX) {
        this.centerX = centerX;
        setLocation((int)Math.round(centerX - getWidth() / 2.0), getY());
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
        setLocation(getX(), (int)Math.round(centerY - getHeight() / 2.0));
    }

    public void setCenter(double centerX, double centerY) {
        setCenterX(centerX);
        setCenterY(centerY);
    }

    public void setWidth(double width) { setSize((int)Math.round(width), getHeight()); }
    public void setHeight(double height) { setSize(getWidth(), (int)Math.round(height)); }

    public void setVelocityX(double velocityX) { this.velocityX = velocityX; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }
    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void setMove(boolean isMove) { this.isMove = isMove; }


    public boolean isBelongs(double x, double y) {
        double x1 = getX(), y1 = getY();
        double x2 = x1 + getWidth(), y2 = y1 + getHeight();

        return ((x >= x1 && x <= x2) && (y >= y1 && y <= y2));
    }

    public boolean isMove() { return isMove; }


    protected void checkWalls() {
        Container parent = getParent();

        if (parent == null)
            return;

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


    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton(mouseEvent))
            setMove(!isMove);

        if (SwingUtilities.isRightMouseButton(mouseEvent)) {
            Container parent = getParent();

            if (parent != null)
                parent.remove(this);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}
    @Override
    public void mouseExited(MouseEvent mouseEvent) {}
}
