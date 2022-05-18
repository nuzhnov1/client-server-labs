package CSLabs.Lab3.Figures;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings({"unused", "DuplicatedCode", "Convert2Diamond"})
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
     * Converts an imageMap in DTO form to a regular imageMap
     * Encoding of images is Base64
     * @param imageMapDTO imageMap in DTO form (HashMap that maps the image name to encoded image)
     * @return A HashMap that maps the image name to the image itself
     * @throws IllegalArgumentException It is thrown out when a one of the image could not be decoded
     */
    public HashMap<String, ImageIcon> toImageMap(HashMap<String, String> imageMapDTO) throws IllegalArgumentException
    {
        HashMap<String, ImageIcon> imageMap = new HashMap<String, ImageIcon>();

        for (String imageName : imageMapDTO.keySet()) {
            try {
                String serializedImage = imageMapDTO.get(imageName);
                byte[] bytesArray = Base64.getDecoder().decode(serializedImage);
                ImageIcon image = new ImageIcon(bytesArray);

                imageMap.put(imageName, image);
            }
            catch (IllegalArgumentException error) {
                throw new IllegalArgumentException(
                        String.format("не удалось декодировать изображение \"%s\"", imageName)
                );
            }
        }

        return imageMap;
    }

    /**
     * Converting a list of figures in DTO form to a regular figures
     * @param figuresDTO DTO form of figures
     * @param imageMap Array of matching the image name with the image itself
     * @return List of figures
     */
    public List<? extends Figure> toFiguresList(FiguresDTOSet figuresDTO,
                                                HashMap<String, ImageIcon> imageMap)
    {
        return figuresDTO.stream().map(figureDTO -> {
            if (figureDTO instanceof LoadedImageDTO)
                return toLoadedImage((LoadedImageDTO) figureDTO, imageMap);
            else
                return toTextImage((TextImageDTO) figureDTO);
        }).toList();
    }

    public FiguresContainer toFiguresContainer(FiguresContainerDTO figuresContainerDTO) {
        HashMap<String, ImageIcon> imageMap = toImageMap(figuresContainerDTO.getImageMapDTO());
        List<? extends Figure> figuresList = toFiguresList(
                figuresContainerDTO.getFiguresDTO(),
                imageMap
        );

        return new FiguresContainer(figuresList, imageMap);
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
     * Converts an imageMap to imageMap in DTO form, which maps the image name to encoded image.
     * Encoding of images is Base64
     * @param imageMap imageMap, which maps the image name to image itself
     * @return A HashMap that maps the image name to encoded image
     * @throws IllegalArgumentException It is thrown out when a one of the image could not be decoded
     * @throws IOException It is thrown out when an image encoding error occurs
     */
    public HashMap<String, String> ToDTO(HashMap<String, ImageIcon> imageMap)
            throws IllegalArgumentException, IOException
    {
        HashMap<String, String> imageMapDTO = new HashMap<String, String>();

        for (String imageName : imageMap.keySet()) {
            try {
                ImageIcon image = imageMap.get(imageName);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                String serializedImage;

                oos.writeObject(image);
                serializedImage = Base64.getEncoder().encodeToString(baos.toByteArray());
                imageMapDTO.put(imageName, serializedImage);
            }
            catch (IOException error) {
                throw new IOException(String.format("ошибка при кодировании изображения \"%s\"", imageName));
            }
        }

        return imageMapDTO;
    }

    /**
     * Converting list of {@link Figure} instances to list of these objects in DTO form
     * @param figures list of {@link Figure} instances
     * @return {@link java.util.ArrayList} of figures objects in DTO form
     */
    public FiguresDTOSet ToDTO(List<? extends Figure> figures) {
        FiguresDTOSet result = new FiguresDTOSet();

        figures.forEach(figure -> {
            if (figure instanceof LoadedImage)
                result.add(ToDTO((LoadedImage) figure));
            else
                result.add(ToDTO((TextImage) figure));
        });

        return result;
    }

    public FiguresContainerDTO ToDTO(FiguresContainer container) throws IOException {
        return new FiguresContainerDTO(
                ToDTO(container.getFigures()),
                ToDTO(container.getImageMap())
        );
    }
}
