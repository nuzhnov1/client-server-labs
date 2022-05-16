package CSLabs.Lab3.MainPanel;

import CSLabs.Lab3.Figures.FiguresDTOList;
import CSLabs.Lab3.MenuBar.StateFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@SuppressWarnings("DuplicatedCode")
class ControllerIO {
    // Constructors:

    public ControllerIO() {}

    // Control methods:

    public void writeStateToStream(ZipOutputStream zipStream, StateFormat format, FiguresDTOList figuresDTO)
            throws IllegalArgumentException, IOException
    {
        try {
            if (format != StateFormat.BIN) {
                ObjectMapper mapper = switch (format) {
                    case JSON -> new JsonMapper();
                    case XML -> new XmlMapper();
                    default -> throw new IllegalArgumentException("недопустимый формат данных файла состояния");
                };

                mapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
                mapper.writeValue(zipStream, figuresDTO);
            }
            else {
                ObjectOutputStream oos = new ObjectOutputStream(zipStream);
                oos.writeObject(figuresDTO);
            }
        }
        catch (IOException ignored) {
            throw new IOException("не удалось записать данные в файл состояния");
        }
    }

    public void writeImageToStream(ZipOutputStream zipStream, String imageName, ImageIcon image) throws IOException
    {
        try {
            ZipEntry imageFile = new ZipEntry(imageName);
            zipStream.putNextEntry(imageFile);

            String extension = FilenameUtils.getExtension(imageName);
            BufferedImage bufferedImage = new BufferedImage(
                    image.getIconWidth(), image.getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D graphics2D = bufferedImage.createGraphics();

            graphics2D.drawImage(image.getImage(), 0, 0, null);
            graphics2D.dispose();
            ImageIO.write(bufferedImage, extension, zipStream);

            zipStream.flush();
            zipStream.closeEntry();
        }
        catch (IOException error) {
            throw new IOException(String.format("не удалось добавить в архив изображение \"%s\"", imageName));
        }
    }

    public FiguresDTOList readStateFromStream(ZipInputStream zipStream, StateFormat format)
            throws IOException, IllegalArgumentException
    {
        try {
            if (format != StateFormat.BIN) {
                ObjectMapper mapper = switch (format) {
                    case JSON -> new JsonMapper();
                    case XML -> new XmlMapper();
                    default -> throw new IllegalArgumentException("недопустимый формат данных файла состояния");
                };

                mapper.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
                return mapper.readValue(zipStream, FiguresDTOList.class);
            }
            else {
                ObjectInputStream ois = new ObjectInputStream(zipStream);
                return (FiguresDTOList) ois.readObject();
            }
        }
        catch (IOException | ClassNotFoundException ignored) {
            throw new IOException("не удалось восстановить данные из файла состояния");
        }
    }

    public ImageIcon readImageFromStream(ZipInputStream zipStream, String imageName) throws IOException {
        try {
            BufferedImage bufferedImage = ImageIO.read(zipStream);
            ImageIcon image = new ImageIcon();

            image.setImage(bufferedImage.getScaledInstance(
                    bufferedImage.getWidth(),
                    bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            ));

            return image;
        }
        catch (IOException ignored) {
            throw new IOException(String.format("не удалось извлечь из архива изображение \"%s\"", imageName));
        }
    }
}
