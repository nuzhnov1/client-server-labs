package CSLabs.Lab2.MainPanel;

import CSLabs.Lab2.Figures.*;
import CSLabs.Lab2.Main;
import CSLabs.Lab2.MenuBar.MenuBar;
import CSLabs.Lab2.MenuBar.MenuBar.StateFormat;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.TypeFactory;
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
    private final MainPanel mainPanel;
    private final FiguresMapper figuresMapper;
    private final HashMap<String, ImageIcon> imageMap;
    private final HashSet<File> imageFilesSet;


    Controller(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        figuresMapper = new FiguresMapper();
        imageMap = new HashMap<String, ImageIcon>();
        imageFilesSet = new HashSet<File>();
    }


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

        if (imageFilesSet.contains(imageFile)) {
            ImageIcon image = imageMap.get(imageName);
            mainPanel.add(new LoadedImage(imageName, image, centerX, centerY, width, height));
        }
        else {
            String imagePath = imageFile.getAbsolutePath();
            ImageIcon image = new ImageIcon(imagePath);

            if (imageMap.containsKey(imageName)) {
                String imageNameWithoutExtension = FilenameUtils.getBaseName(imageName);
                String extension = FilenameUtils.getExtension(imageName);
                String newImageName = imageName;

                for (long i = 0; imageMap.containsKey(newImageName); i++)
                    newImageName = imageNameWithoutExtension + i + "." + extension;

                imageMap.put(newImageName, image);
                imageFilesSet.add(imageFile);
                mainPanel.add(new LoadedImage(newImageName, image, centerX, centerY, width, height));
            }
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

    public void saveStateToStream(OutputStream stream, StateFormat format)
            throws IOException, RuntimeException
    {
        ZipOutputStream zipStream = new ZipOutputStream(stream);
        ZipEntry stateFile = new ZipEntry("state" + "." + format.name().toLowerCase());

        try { zipStream.putNextEntry(stateFile); }
        catch (IOException error) { throw new IOException("не удалось создать zip-архив с данными"); }

        List<? extends Figure> figures = getFigures();
        FiguresMapper.FiguresDTOList figuresDTO = figuresMapper.ToDTO(figures);

        if (format != StateFormat.BIN) {
            ObjectMapper mapper = switch (format) {
                case JSON -> new JsonMapper();
                case XML -> new XmlMapper();
                default -> throw new IllegalArgumentException("недопустимый формат данных файла состояния");
            };

            try {
                mapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
                mapper.writeValue(zipStream, figuresDTO);
            }
            catch (JacksonException error) {
                throw new IOException(
                        "не удалось сохранить состояние в файл: ошибка при сериализации данных"
                );
            }
            catch (IOException ignored) {
                throw new IOException("не удалось записать данные в файл состояния");
            }
        }
        else {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(zipStream);
                oos.writeObject(figuresDTO);
            }
            catch (IOException | SecurityException ignored) {
                throw new IOException("не удалось записать данные в файл состояния");
            }
        }

        zipStream.flush();
        zipStream.closeEntry();

        imageMap.forEach((imageName, image) -> {
            try {
                ZipEntry imageFile = new ZipEntry(imageName);
                zipStream.putNextEntry(imageFile);

                String extension = FilenameUtils.getExtension(imageName);
                BufferedImage bi = new BufferedImage(
                        image.getIconWidth(), image.getIconHeight(),
                        BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D graphics2D = bi.createGraphics();

                graphics2D.drawImage(image.getImage(), 0, 0, null);
                graphics2D.dispose();
                ImageIO.write(bi, extension, zipStream);
                zipStream.flush();
                zipStream.closeEntry();
            }
            catch (IOException error) {
                throw new RuntimeException(
                        String.format("не удалось добавить в архив изображение \"%s\"", imageName)
                );
            }
        });

        zipStream.close();
    }

    public void saveStateToFile(File file) throws IOException, RuntimeException {
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

    public void restoreStateFromStream(InputStream stream)
            throws IOException, RuntimeException
    {
        ZipInputStream zipStream = new ZipInputStream(stream);
        FiguresMapper.FiguresDTOList figuresDTO = null;

        imageMap.clear();
        imageFilesSet.clear();
        mainPanel.removeAll();

        try {
            ZipEntry entry;

            while ((entry = zipStream.getNextEntry()) != null) {
                String filename = entry.getName();
                String extension = FilenameUtils.getExtension(filename);
                StateFormat format = null;
                boolean isStateFile;

                try {
                    format = StateFormat.valueOf(extension.toUpperCase());
                    isStateFile = true;
                }
                catch (IllegalArgumentException ignored) {
                    isStateFile = false;
                }

                if (isStateFile) {
                    try {
                        if (format != StateFormat.BIN) {
                            ObjectMapper mapper = switch (format) {
                                case JSON -> new JsonMapper();
                                case XML -> new XmlMapper();
                                default -> throw new IllegalArgumentException(
                                        "недопустимый формат данных файла состояния"
                                );
                            };

                            mapper.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
                            figuresDTO = mapper.readValue(zipStream, FiguresMapper.FiguresDTOList.class);
                        }
                        else {
                            ObjectInputStream ois = new ObjectInputStream(zipStream);
                            figuresDTO = (FiguresMapper.FiguresDTOList) ois.readObject();
                        }
                    }
                    catch (JacksonException error) {
                        throw new IOException(
                                "не удалось восстановить состояние из файла: ошибка при десериализации данных"
                        );
                    }
                    catch (IOException | ClassNotFoundException ignored) {
                        throw new IOException("не удалось восстановить данные из файла состояния");
                    }
                }
                else {
                    try {
                        BufferedImage bi = ImageIO.read(zipStream);
                        ImageIcon image = new ImageIcon();

                        image.setImage(bi.getScaledInstance(
                                bi.getWidth(), bi.getHeight(),
                                BufferedImage.TYPE_INT_ARGB)
                        );

                        imageMap.put(filename, image);
                    }
                    catch (IOException ignored) {
                        throw new IOException(
                                String.format("не удалось извлечь из архива изображение \"%s\"", filename)
                        );
                    }
                }

                zipStream.closeEntry();
            }
        }
        catch (IOException error) {
            throw new IOException("не удалось создать zip-архив с данными");
        }

        zipStream.close();

        List<? extends Figure> figures = figuresMapper.toFiguresList(figuresDTO, imageMap);
        figures.forEach(mainPanel::add);
    }

    public void restoreStateFromFile(File file) throws IOException, RuntimeException {
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


    private List<? extends Figure> getFigures() {
        return Arrays.stream(mainPanel.getComponents())
                .filter(c -> c instanceof Figure)
                .map(c -> (Figure) c)
                .toList();
    }
}
