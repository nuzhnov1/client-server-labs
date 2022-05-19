package CSLabs.Lab4.Figures;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("unused")
public class LoadedImage extends Figure {
    // Data members

    private final String imageName; // Unique image name
    private final ImageIcon image;

    // Constructors:

    public LoadedImage(String imageName, ImageIcon image, double centerX, double centerY) {
        this.imageName = imageName;
        this.image = image;

        setSize(image.getIconWidth(), image.getIconHeight());
        setCenter(centerX, centerY);

        velocityX = Utilities.randomRange(-MAX_SPEED, MAX_SPEED);
        velocityY = Utilities.randomRange(-MAX_SPEED, MAX_SPEED);
    }

    public LoadedImage(String imageName, ImageIcon image, double centerX, double centerY, int width, int height) {
        this.imageName = imageName;
        this.image = image;

        setSize(width, height);
        setCenter(centerX, centerY);

        velocityX = Utilities.randomRange(-MAX_SPEED, MAX_SPEED);
        velocityY = Utilities.randomRange(-MAX_SPEED, MAX_SPEED);
    }

    // Overridden methods:

    @Override
    public void paint(Graphics g) {
        g.drawImage(image.getImage(), getX(), getY(), getWidth(), getHeight(), null);
    }

    @Override
    public void move(double time) {
        if (!isMove)
            return;

        checkWalls();
        setCenter(centerX + time * velocityX, centerY + time * velocityY);
    }

    // Getters:

    public String getImageName() { return imageName; }
    public ImageIcon getImage() { return image; }
}
