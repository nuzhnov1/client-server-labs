package CSLabs.Lab2.MainPanel;

import CSLabs.Lab2.Figures.*;
import CSLabs.Lab2.Main;
import CSLabs.Lab2.MenuBar.MenuBar;
import CSLabs.Lab2.MenuBar.MenuBar.StateFormat;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@SuppressWarnings("Convert2Diamond")
public class Controller {
    // Data members:

    private final MainPanel mainPanel;
    private final FiguresMapper figuresMapper;
    private final HashMap<String, ImageIcon> imageMap;
    private final HashSet<File> imageFilesSet;

    // Constructors:

    Controller(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        figuresMapper = new FiguresMapper();
        imageMap = new HashMap<String, ImageIcon>();
        imageFilesSet = new HashSet<File>();
    }

    // Control methods:

    public void moveControl() throws InterruptedException {
        final double dt = 1.0 / Main.FPS;
        final MenuBar menuBar = MenuBar.getInstance();

        while (true) {
            boolean isMove = menuBar.isMove();

            if (isMove) {
                for (Component component : mainPanel.getComponents()) {
                    if (component instanceof Figure)
                        ((Figure) component).move(dt);
                }
            }

            Thread.sleep((long) (dt * 1000));
        }
    }

    public void addImage(File imageFile, int centerX, int centerY, int width, int height) {
        String imageName = imageFile.getName();

        // If image is already exist in images set then get this image and create new LoadedImage
        if (imageFilesSet.contains(imageFile)) {
            ImageIcon image = imageMap.get(imageName);
            mainPanel.add(new LoadedImage(imageName, image, centerX, centerY, width, height));
        }
        else {
            String imagePath = imageFile.getAbsolutePath();
            ImageIcon image = new ImageIcon(imagePath);

            // If image with this name is already exist in imageMap then generate new name for this image
            // and add them to imageMap
            if (imageMap.containsKey(imageName)) {
                String imageNameWithoutExtension = FilenameUtils.getBaseName(imageName);
                String extension = FilenameUtils.getExtension(imageName);
                String newImageName = imageName;

                // Generate an image name based on this one - add numbers to it until
                // there is no such name in imageMap
                for (long i = 0; imageMap.containsKey(newImageName); i++)
                    newImageName = imageNameWithoutExtension + i + "." + extension;

                // Put this image with new name to imageMap
                imageMap.put(newImageName, image);
                imageFilesSet.add(imageFile);
                mainPanel.add(new LoadedImage(newImageName, image, centerX, centerY, width, height));
            }
            // Otherwise, put image with this name to imageMap
            else {
                imageMap.put(imageName, image);
                imageFilesSet.add(imageFile);
                mainPanel.add(new LoadedImage(imageName, image, centerX, centerY, width, height));
            }
        }
    }

    public void addImagedText(String imagedText, int initX, int initY) {
        mainPanel.add(new TextImage(imagedText, initX, initY));
    }

    public void saveStateToStream(OutputStream stream, StateFormat format) throws IOException {
        try (ZipOutputStream zipStream = new ZipOutputStream(stream)) {
            ZipEntry stateFile = new ZipEntry("state" + "." + format.name().toLowerCase());
            List<? extends Figure> figures = getFigures();
            FiguresDTOList figuresDTO = figuresMapper.ToDTO(figures);

            zipStream.putNextEntry(stateFile);
            writeStateToStream(zipStream, format, figuresDTO);
            zipStream.flush();
            zipStream.closeEntry();

            for (String imageName : imageMap.keySet()) {
                ImageIcon image = imageMap.get(imageName);
                writeImageToStream(zipStream, imageName, image);
            }
        }
    }

    public void restoreStateFromStream(InputStream stream) throws IOException {
        imageMap.clear();
        imageFilesSet.clear();
        mainPanel.removeAll();

        FiguresDTOList figuresDTO = new FiguresDTOList();

        try (ZipInputStream zipStream = new ZipInputStream(stream)) {
            ZipEntry entry;

            while ((entry = zipStream.getNextEntry()) != null) {
                String filename = entry.getName();
                String extension = FilenameUtils.getExtension(filename);

                if (isStateFile(extension))
                    figuresDTO = readStateFromStream(zipStream, parseFormat(extension));
                else {
                    ImageIcon image = readImageFromStream(zipStream, filename);
                    imageMap.put(filename, image);
                }

                zipStream.closeEntry();
            }
        }

        List<? extends Figure> figures = figuresMapper.toFiguresList(figuresDTO, imageMap);
        figures.forEach(mainPanel::add);
    }

    public void saveStateToFile(File file) throws IOException {
        MenuBar menuBar = MenuBar.getInstance();
        StateFormat format = menuBar.getFormat();

        try (FileOutputStream stream = new FileOutputStream(file)) {
            saveStateToStream(stream, format);
        }
        catch (FileNotFoundException ignored) {
            throw new IOException("не удалось открыть файл: данный файл не существует");
        }
        catch (SecurityException ignored) {
            throw new IOException("не удалось открыть файл для записи: отказано в доступе");
        }
    }

    public void restoreStateFromFile(File file) throws IOException {
        try (FileInputStream stream = new FileInputStream(file)) {
            restoreStateFromStream(stream);
        }
        catch (FileNotFoundException ignored) {
            throw new IOException("не удалось открыть файл: данный файл не существует");
        }
        catch (SecurityException ignored) {
            throw new IOException("не удалось открыть файл для записи: отказано в доступе");
        }
    }

    public void removeAllFigures() {
        imageMap.clear();
        imageFilesSet.clear();
        mainPanel.removeAll();
    }

    // Auxiliary methods:

    private List<? extends Figure> getFigures() {
        return Arrays.stream(mainPanel.getComponents())
                .filter(c -> c instanceof Figure)
                .map(c -> (Figure) c)
                .toList();
    }

    private void writeStateToStream(ZipOutputStream zipStream, StateFormat format,
                                    FiguresDTOList figuresDTO)
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

    private void writeImageToStream(ZipOutputStream zipStream, String imageName, ImageIcon image) throws IOException
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

    private FiguresDTOList readStateFromStream(ZipInputStream zipStream, StateFormat format)
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

    private ImageIcon readImageFromStream(ZipInputStream zipStream, String imageName) throws IOException {
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

    private StateFormat parseFormat(String extension) throws IllegalArgumentException {
        try {
            return StateFormat.valueOf(extension.toUpperCase());
        }
        catch (IllegalArgumentException ignored) {
            throw new IllegalArgumentException("недопустимый формат данных файла состояния");
        }
    }

    private boolean isStateFile(String extension) {
        try { parseFormat(extension); return true; }
        catch (IllegalArgumentException ignored) { return false; }
    }
}
