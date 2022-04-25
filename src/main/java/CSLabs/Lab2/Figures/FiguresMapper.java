package CSLabs.Lab2.Figures;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings({"unused"})
public class FiguresMapper {
    // Constructors:

    public FiguresMapper() {}

    // Methods for converting DTO objects into regular objects:

    /**
     * Converting an image in DTO form to a regular image
     * @param imageDTO DTO form of {@link LoadedImage}
     * @param imageMap Array of matching the image name with the image itself
     * @return {@link LoadedImage} instance from imageMap
     */
    public LoadedImage toLoadedImage(LoadedImageDTO imageDTO, HashMap<String, ImageIcon> imageMap) {
        String imageName = imageDTO.getImageName();
        ImageIcon imageIcon = imageMap.get(imageName);

        if (imageIcon == null)
            throw new RuntimeException(String.format("изображение \"%s\" не найдено", imageName));

        int width = imageDTO.getWidth();
        int height = imageDTO.getHeight();
        double centerX = imageDTO.getCenterX();
        double centerY = imageDTO.getCenterY();
        LoadedImage image = new LoadedImage(imageName, imageIcon, centerX, centerY, width, height);

        image.setVelocity(imageDTO.getVelocityX(), imageDTO.getVelocityY());
        image.setMove(imageDTO.isMove());

        return image;
    }

    /**
     * Converting an imaged text in DTO form to a regular imaged text
     * @param textImageDTO DTO form of {@link TextImage}
     * @return {@link TextImage} instance
     */
    public TextImage toTextImage(TextImageDTO textImageDTO) {
        TextImage textImage = new TextImage(
                textImageDTO.getText(),
                textImageDTO.getInitX(), textImageDTO.getInitY()
        );

        textImage.setCenter(textImageDTO.getCenterX(), textImageDTO.getCenterY());
        textImage.setVelocity(textImageDTO.getVelocityX(), textImageDTO.getVelocityY());
        textImage.setMove(textImageDTO.isMove());

        return textImage;
    }

    /**
     * Converting a list of figures in DTO form to a regular figures
     * @param figuresDTO DTO form of figures
     * @param imageMap Array of matching the image name with the image itself
     * @return List of figures
     */
    public List<? extends Figure> toFiguresList(List<? extends FigureDTO> figuresDTO,
                                                HashMap<String, ImageIcon> imageMap)
    {
        return figuresDTO.stream().map(figureDTO -> {
            if (figureDTO instanceof LoadedImageDTO)
                return toLoadedImage((LoadedImageDTO) figureDTO, imageMap);
            else
                return toTextImage((TextImageDTO) figureDTO);
        }).toList();
    }

    // Methods for converting regular figures to DTO form of these figures:

    /**
     * Converting {@link LoadedImage} instance to DTO form
     * @param image converted image
     * @return DTO form of image
     */
    public LoadedImageDTO ToDTO(LoadedImage image) {
        return new LoadedImageDTO(
                image.getCenterX(), image.getCenterY(),
                image.getVelocityX(), image.getVelocityY(),
                image.isMove(), image.getImageName(),
                image.getWidth(), image.getHeight()
        );
    }

    /**
     * Converting {@link TextImage} instance to DTO form
     * @param textImage converted imaged text
     * @return DTO form of imaged text
     */
    public TextImageDTO ToDTO(TextImage textImage) {
        return new TextImageDTO(
                textImage.getCenterX(), textImage.getCenterY(),
                textImage.getVelocityX(), textImage.getVelocityY(),
                textImage.isMove(), textImage.getText(),
                textImage.getInitX(), textImage.getInitY()
        );
    }

    /**
     * Converting list of {@link Figure} instances to list of these objects in DTO form
     * @param figures list of {@link Figure} instances
     * @return {@link java.util.ArrayList} of figures objects in DTO form
     */
    public FiguresDTOList ToDTO(List<? extends Figure> figures) {
        FiguresDTOList result = new FiguresDTOList();

        figures.forEach(figure -> {
            if (figure instanceof LoadedImage)
                result.add(ToDTO((LoadedImage) figure));
            else
                result.add(ToDTO((TextImage) figure));
        });

        return result;
    }
}
