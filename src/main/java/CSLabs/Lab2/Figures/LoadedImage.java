package CSLabs.Lab2.Figures;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("unused")
public class LoadedImage extends Figure {
    private final String imageName;
    private final ImageIcon image;


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


    public String getImageName() { return imageName; }
    public ImageIcon getImage() { return image; }


    @Override
    public void paint(Graphics g) {
        g.drawImage(image.getImage(), getX(), getY(), getWidth(), getHeight(), null);
    }

    @Override
    public void move(double time) {
        if (!isMove)
            return;

        checkWalls();
        setCenter(
                centerX + time * velocityX,
                centerY + time * velocityY
        );
    }
}
