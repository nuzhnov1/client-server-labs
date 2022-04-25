package CSLabs.Lab2.MainPanel;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.HashMap;

@SuppressWarnings({"unused", "Convert2Diamond"})
public class MainPanelMapper {
    public MainPanelMapper() {}


    public HashMap<String, ImageIcon> toImageMap(HashMap<String, String> imageMapDTO)
            throws IllegalArgumentException
    {
        HashMap<String, ImageIcon> imageMap = new HashMap<String, ImageIcon>();

        imageMapDTO.forEach((imageName, serializedImage) -> {
            try {
                byte[] bytesArray = Base64.getDecoder().decode(serializedImage);
                ImageIcon image = new ImageIcon(bytesArray);

                imageMap.put(imageName, image);
            }
            catch (IllegalArgumentException error) {
                throw new IllegalArgumentException(
                        String.format("не удалось декодировать изображение \"%s\"", imageName)
                );
            }
        });

        return imageMap;
    }

    public HashMap<String, String> ToDTO(HashMap<String, ImageIcon> imageMap) throws IllegalArgumentException {
        HashMap<String, String> imageMapDTO = new HashMap<String, String>();

        imageMap.forEach((imageName, image) -> {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                String serializedImage;

                oos.writeObject(image);
                serializedImage = Base64.getEncoder().encodeToString(baos.toByteArray());
                imageMapDTO.put(imageName, serializedImage);
            }
            catch (IOException error) {
                throw new IllegalArgumentException(
                        String.format("ошибка при сериализации изображения \"%s\"", imageName)
                );
            }
        });

        return imageMapDTO;
    }
}
