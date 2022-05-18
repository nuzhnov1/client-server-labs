package CSLabs.Lab3.Figures;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@SuppressWarnings({"unused", "DuplicatedCode"})
public abstract class Figure extends JComponent implements MouseListener, IMovable
{
    // Constants:
    
    protected static final double BOUNCE = -1.0;        // Rebound ratio
    protected static final double MAX_SPEED = 100.0;    // Absolute maximum speed

    // Data members:
    
    protected double centerX, centerY;
    protected int width, height;
    protected double velocityX, velocityY;
    protected boolean isMove = true;


    // Constructors:

    public Figure() {
        setVisible(true);
        setEnabled(true);
        addMouseListener(this);
    }

    // Getters:

    public double getCenterX() { return centerX; }
    public double getCenterY() { return centerY; }
    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }

    // Is-getters:

    public boolean isMove() { return isMove; }

    /**
     * Checks whether a point belongs to a given shape
     */
    public boolean isBelongs(int x, int y) {
        int x1 = getX(), y1 = getY();
        int x2 = x1 + getWidth(), y2 = y1 + getHeight();

        return ((x >= x1 && x <= x2) && (y >= y1 && y <= y2));
    }

    // Setters:

    public void setCenterX(double centerX) {
        this.centerX = centerX;
        setLocation((int) Math.round(centerX - getWidth() / 2.0), getY());
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
        setLocation(getX(), (int) Math.round(centerY - getHeight() / 2.0));
    }

    public void setCenter(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;

        setLocation(
                (int) Math.round(centerX - getWidth() / 2.0),
                (int) Math.round(centerY - getHeight() / 2.0)
        );
    }

    public void setVelocityX(double velocityX) { this.velocityX = velocityX; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }
    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void setMove(boolean isMove) { this.isMove = isMove; }


    // Other methods:

    /**
     * Checks whether the figure has collided with any wall of the panel
     * and changes its coordinates in case of a collision
     */
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
            setCenterX((int) Math.round(canvasX2 - getWidth() / 2.0));
            velocityX *= BOUNCE;
        }
        else if (imageX1 < canvasX1) {
            setCenterX((int) Math.round(canvasX1 + getWidth() / 2.0));
            velocityX *= BOUNCE;
        }

        if (imageY2 > canvasY2) {
            setCenterY((int) Math.round(canvasY2 - getHeight() / 2.0));
            velocityY *= BOUNCE;
        }
        else if (imageY1 < canvasY1) {
            setCenterY((int) Math.round(canvasY1 + getHeight() / 2.0));
            velocityY *= BOUNCE;
        }
    }

    // MouseListener methods implementation:

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
