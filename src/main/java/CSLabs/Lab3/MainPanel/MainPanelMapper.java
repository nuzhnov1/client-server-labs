package CSLabs.Lab3.MainPanel;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.HashMap;

@SuppressWarnings({"unused", "Convert2Diamond", "DuplicatedCode"})
public class MainPanelMapper {
    public MainPanelMapper() {}


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
}
