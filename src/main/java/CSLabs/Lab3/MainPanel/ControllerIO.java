package CSLabs.Lab3.MainPanel;

import CSLabs.Lab3.Figures.*;
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
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@SuppressWarnings({"DuplicatedCode", "unused", "Convert2Diamond"})
class ControllerIO {
    // Data members:

    private final FiguresMapper figuresMapper = new FiguresMapper();

    // Constructors:

    public ControllerIO() {}

    // Control methods:

    /**
     * Writes figures with images to the output stream in zip format
     * @param stream output  stream
     * @param format format of state file (JSON, XML, BIN etc.)
     * @param container figures and image map
     * @throws IOException it is thrown when an I/O error occurs
     */
    public void writeFiguresToZIP(OutputStream stream, StateFormat format, FiguresContainer container)
            throws IOException
    {
        List<? extends Figure> figures = container.getFigures();
        HashMap<String, ImageIcon> imageMap = container.getImageMap();

        try (ZipOutputStream zipStream = new ZipOutputStream(stream)) {
            for (String imageName : imageMap.keySet()) {
                ZipEntry imageFile = new ZipEntry(imageName);
                ImageIcon image = imageMap.get(imageName);

                zipStream.putNextEntry(imageFile);
                writeImageToStream(zipStream, imageName, image);
                zipStream.flush();
                zipStream.closeEntry();
            }

            ZipEntry stateFile = new ZipEntry("state" + "." + format.name().toLowerCase());

            zipStream.putNextEntry(stateFile);
            writeFiguresWithoutImagesToStream(zipStream, format, figures);
            zipStream.flush();
            zipStream.closeEntry();
        }
    }

    /**
     * Writes a list of figures to the output stream in a specified format without images
     * @param stream output stream
     * @param format format of state file (JSON, XML, BIN etc.)
     * @param figures list of figures
     * @throws IllegalArgumentException is thrown when an invalid status format has been received
     * @throws IOException it is thrown when an I/O error occurs
     */
    private void writeFiguresWithoutImagesToStream(OutputStream stream, StateFormat format,
                                                   List<? extends Figure> figures)
            throws IllegalArgumentException, IOException
    {
        try {
            FiguresDTOSet figuresDTO = figuresMapper.ToDTO(figures);

            if (format != StateFormat.BIN) {
                ObjectMapper mapper = switch (format) {
                    case JSON -> new JsonMapper();
                    case XML -> new XmlMapper();
                    default -> throw new IllegalArgumentException("недопустимый формат данных файла состояния");
                };

                mapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
                mapper.writeValue(stream, figuresDTO);
            }
            else {
                ObjectOutputStream oos = new ObjectOutputStream(stream);
                oos.writeObject(figuresDTO);
            }
        }
        catch (IOException ignored) {
            throw new IOException("не удалось записать данные о фигурах в поток вывода");
        }
    }

    /**
     * Writes an image with the given name to the output stream
     * @param stream output stream
     * @param imageName name image with extension
     * @param image image
     * @throws IOException it is thrown when an I/O error occurs
     */
    private void writeImageToStream(OutputStream stream, String imageName, ImageIcon image) throws IOException {
        try {
            String extension = FilenameUtils.getExtension(imageName);
            BufferedImage bufferedImage = new BufferedImage(
                    image.getIconWidth(), image.getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D graphics2D = bufferedImage.createGraphics();

            graphics2D.drawImage(image.getImage(), 0, 0, null);
            graphics2D.dispose();
            ImageIO.write(bufferedImage, extension, stream);

            stream.flush();
        }
        catch (IOException error) {
            throw new IOException(String.format("не удалось записать изображение \"%s\" в поток вывода", imageName));
        }
    }

    /**
     * Reads figures with images from the input stream in zip format
     * @param stream input stream
     * @throws IOException it is thrown when an I/O error occurs
     */
    public FiguresContainer readFiguresFromZIP(InputStream stream) throws IOException {
        ArrayList<Figure> figures = new ArrayList<Figure>();
        HashMap<String, ImageIcon> imageMap = new HashMap<String, ImageIcon>();

        try (ZipInputStream zipStream = new ZipInputStream(stream)) {
            ZipEntry entry;

            while ((entry = zipStream.getNextEntry()) != null) {
                String filename = entry.getName();
                String extension = FilenameUtils.getExtension(filename);

                if (StateFormat.isFormat(extension)) {
                    figures.addAll(readFiguresWithoutImagesFromStream(
                            zipStream,
                            imageMap, StateFormat.parse(extension)
                    ));
                }
                else {
                    ImageIcon image = readImageFromStream(zipStream, filename);
                    imageMap.put(filename, image);
                }

                zipStream.closeEntry();
            }
        }

        return new FiguresContainer(figures, imageMap);
    }

    /**
     * Reads a list of figures from the input stream in a specified format without images
     * @param stream input stream
     * @param format format of state file (JSON, XML, BIN etc.)
     * @return list of figures
     * @throws IllegalArgumentException is thrown when an invalid status format has been received
     * @throws IOException it is thrown when an I/O error occurs
     */
    private List<? extends Figure> readFiguresWithoutImagesFromStream(InputStream stream,
                                                                      HashMap<String, ImageIcon> imageMap,
                                                                      StateFormat format)
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
                FiguresDTOSet figuresDTO = mapper.readValue(stream, FiguresDTOSet.class);

                return figuresMapper.toFiguresList(figuresDTO, imageMap);
            }
            else {
                ObjectInputStream ois = new ObjectInputStream(stream);
                FiguresDTOSet figuresDTO = (FiguresDTOSet) ois.readObject();

                return figuresMapper.toFiguresList(figuresDTO, imageMap);
            }
        }
        catch (IOException | ClassNotFoundException ignored) {
            throw new IOException("не удалось восстановить данные о фигурах из потока ввода");
        }
    }

    private ImageIcon readImageFromStream(InputStream stream, String imageName) throws IOException {
        try {
            BufferedImage bufferedImage = ImageIO.read(stream);
            ImageIcon image = new ImageIcon();

            image.setImage(bufferedImage.getScaledInstance(
                    bufferedImage.getWidth(),
                    bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            ));

            return image;
        }
        catch (IOException ignored) {
            throw new IOException(String.format(
                    "не удалось восстановить изображение \"%s\" из потока ввода", imageName
            ));
        }
    }
}
