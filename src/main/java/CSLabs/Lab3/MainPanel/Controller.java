package CSLabs.Lab3.MainPanel;

import CSLabs.Lab3.Figures.Figure;
import CSLabs.Lab3.Figures.FiguresContainer;
import CSLabs.Lab3.Figures.LoadedImage;
import CSLabs.Lab3.Figures.TextImage;
import CSLabs.Lab3.Main;
import CSLabs.Lab3.MenuBar.MenuBar;
import CSLabs.Lab3.MenuBar.StateFormat;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.UUID;

@SuppressWarnings({"DuplicatedCode"})
public class Controller {
    // Constants:

    private static final int DATA_LENGTH = 8192;

    // Data members:

    private final MainPanel mainPanel;
    private final FiguresContainer container = new FiguresContainer();
    private final ControllerIO controllerIO = new ControllerIO();

    private volatile boolean isMove = true;

    // Constructors:

    Controller(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    // Start methods:

    void startMoveControl() {
        Thread moveController = new Thread(() -> {
            try { moveControl(); }
            catch (InterruptedException ignored) {}
        });

        moveController.start();
    }

    // Control methods:

    private void moveControl() throws InterruptedException {
        final double dt = 1.0 / Main.FPS;

        while (true) {
            if (isMove) {
                for (Component component : mainPanel.getComponents()) {
                    if (component instanceof Figure)
                        ((Figure) component).move(dt);
                }
            }

            Thread.sleep((long) (dt * 1000));
        }
    }

    public int getSize() { return container.getFigures().size(); }

    public void writeFigureToNetwork(InputStream is, OutputStream os, StateFormat format) throws IOException {
        String tempFilePath = MenuBar.getInstance().getCurrentDir()
                .getAbsolutePath() + File.pathSeparator + UUID.randomUUID() + ".zip";

        File tempFile = new File(tempFilePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

        int index = Integer.parseInt(br.readLine());

        try {
            FileOutputStream fos = new FileOutputStream(tempFile);
            Figure figure = container.getFigures().get(index);
            FiguresContainer tempContainer = new FiguresContainer();

            tempContainer.addFigure(figure);
            controllerIO.writeFiguresToZIP(fos, format, tempContainer);
            fos.close();

            bw.write("Ok\n"); bw.flush();
            bw.write(String.format("%d\n", tempFile.length())); bw.flush();
        }
        catch (Exception ignored) {
            bw.write("Error\n"); bw.flush();
            return;
        }

        FileInputStream fis = new FileInputStream(tempFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        byte[] bytes = new byte[DATA_LENGTH];
        int readBytes;

        while ((readBytes = bis.read(bytes)) != -1) {
            bos.write(bytes, 0, readBytes); bos.flush();
        }

        bis.close();
        tempFile.delete();
    }

    public void readFigureFromStream(InputStream is, OutputStream os, int index) throws IOException {
        String tempFilePath = MenuBar.getInstance()
                .getCurrentDir().getAbsolutePath() + File.pathSeparator + UUID.randomUUID() + ".zip";

        File tempFile = new File(tempFilePath);
        FileOutputStream fos = new FileOutputStream(tempFile);
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        byte[] bytes = new byte[DATA_LENGTH];
        int totalReadBytes = 0;
        int readBytes;

        bw.write(String.format("%d\n", index)); bw.flush();

        String status = br.readLine();
        if (!status.equals("Ok"))
            throw new IOException("Ошибка при получении объекта");

        int size = Integer.parseInt(br.readLine());
        while (totalReadBytes < size) {
            readBytes = bis.read(bytes, 0, Math.min(DATA_LENGTH, size));

            if ((readBytes == -1))
                throw new IOException("Ошибка при получении объекта");

            totalReadBytes += readBytes;
            bos.write(bytes, 0, readBytes); bos.flush();
        }

        bos.close();

        FileInputStream inputStream = new FileInputStream(tempFile);
        FiguresContainer resultContainer = controllerIO.readFiguresFromZIP(inputStream);
        inputStream.close();
        tempFile.delete();

        addFiguresFromContainer(resultContainer);
    }

    private void addFigure(Figure figure) {
        if (figure instanceof LoadedImage)
            addImage((LoadedImage) figure);
        else
            addImagedText((TextImage) figure);
    }

    private void addImage(LoadedImage loadedImage) {
        container.addLoadedImage(loadedImage);
        mainPanel.add(loadedImage);
    }

    public void addImage(File imageFile, int centerX, int centerY, int width, int height) {
        String imageName = imageFile.getName();
        ImageIcon imageIcon = new ImageIcon(imageFile.getAbsolutePath());
        LoadedImage loadedImage = new LoadedImage(imageName, imageIcon, centerX, centerY, width, height);

        addImage(loadedImage);
    }

    private void addImagedText(TextImage textImage) {
        container.addTextImage(textImage);
        mainPanel.add(textImage);
    }

    public void addImagedText(String imagedText, int initX, int initY) {
        TextImage textImage = new TextImage(imagedText, initX, initY);
        addImagedText(textImage);
    }

    public void addFiguresFromContainer(FiguresContainer container) {
        container.getFigures().forEach(this::addFigure);
    }

    public void removeFigure(Figure figure) { container.remove(figure); }

    public void removeAllFigures() {
        container.clear();
        mainPanel.removeAll();
    }

    public void setMove(boolean isMove) { this.isMove = isMove; }

    /**
     * Creates a zip archive and writes figures there
     * @param file new zip archive
     * @throws IOException it is thrown when an I/O error occurs
     */
    public void saveStateToFile(File file) throws IOException {
        MenuBar menuBar = MenuBar.getInstance();
        StateFormat format = menuBar.getFormat();

        try (FileOutputStream stream = new FileOutputStream(file)) {
            controllerIO.writeFiguresToZIP(stream, format, container);
        }
        catch (FileNotFoundException ignored) {
            throw new IOException("не удалось открыть файл: данный файл не существует");
        }
        catch (SecurityException ignored) {
            throw new IOException("не удалось открыть файл для записи: отказано в доступе");
        }
    }

    /**
     * Opens the zip archive and reads figures from there
     * @param file exists zip archive
     * @throws IOException it is thrown when an I/O error occurs
     */
    public void restoreStateFromFile(File file) throws IOException {
        try (FileInputStream stream = new FileInputStream(file)) {
            FiguresContainer container = controllerIO.readFiguresFromZIP(stream);
            removeAllFigures();
            addFiguresFromContainer(container);
        }
        catch (FileNotFoundException ignored) {
            throw new IOException("не удалось открыть файл: данный файл не существует");
        }
        catch (SecurityException ignored) {
            throw new IOException("не удалось открыть файл для записи: отказано в доступе");
        }
    }

    // Getters:

    public boolean isMove() { return isMove; }
}
