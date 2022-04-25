package CSLabs.Lab2.Figures;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings({"unused"})
public class FiguresMapper {
    public static class FiguresDTOList extends ArrayList<FigureDTO> {}


    public FiguresMapper() {}


    public LoadedImage toLoadedImage(LoadedImageDTO imageDTO, HashMap<String, ImageIcon> imageMap)
            throws NullPointerException
    {
        String imageName = imageDTO.getImageName();
        ImageIcon imageIcon = imageMap.get(imageName);

        if (imageIcon == null)
            throw new NullPointerException(String.format("изображение \"%s\" не найдено", imageName));

        int width = imageDTO.getWidth();
        int height = imageDTO.getHeight();
        double centerX = imageDTO.getCenterX();
        double centerY = imageDTO.getCenterY();

        LoadedImage image = new LoadedImage(imageName, imageIcon, centerX, centerY, width, height);

        image.setVelocity(imageDTO.getVelocityX(), imageDTO.getVelocityY());
        image.setMove(imageDTO.isMove());

        return image;
    }

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

    public List<? extends Figure> toFiguresList(FiguresDTOList figuresDTO,
                                                HashMap<String, ImageIcon> imageMap)
            throws NullPointerException
    {
        return figuresDTO.stream().map(figureDTO -> {
            if (figureDTO instanceof LoadedImageDTO)
                return toLoadedImage((LoadedImageDTO) figureDTO, imageMap);
            else
                return toTextImage((TextImageDTO) figureDTO);
        }).toList();
    }


    public LoadedImageDTO ToDTO(LoadedImage image) {
        return new LoadedImageDTO(
                image.getCenterX(), image.getCenterY(),
                image.getVelocityX(), image.getVelocityY(),
                image.isMove(), image.getImageName(),
                image.getWidth(), image.getHeight()
        );
    }

    public TextImageDTO ToDTO(TextImage textImage) {
        return new TextImageDTO(
                textImage.getCenterX(), textImage.getCenterY(),
                textImage.getVelocityX(), textImage.getVelocityY(),
                textImage.isMove(), textImage.getText(),
                textImage.getInitX(), textImage.getInitY()
        );
    }

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
