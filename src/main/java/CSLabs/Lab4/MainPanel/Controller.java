package CSLabs.Lab4.MainPanel;

import CSLabs.Lab4.Figures.Figure;
import CSLabs.Lab4.Figures.FiguresContainer;
import CSLabs.Lab4.Figures.LoadedImage;
import CSLabs.Lab4.Figures.TextImage;
import CSLabs.Lab4.MenuBar.MenuBar;
import CSLabs.Lab4.Main;
import CSLabs.Lab4.MenuBar.StateFormat;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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

    public void writeFigureToNetwork(DatagramSocket serverSocket, StateFormat format) throws IOException {
        String tempFilePath = MenuBar.getInstance().getCurrentDir()
                .getAbsolutePath() + File.pathSeparator + UUID.randomUUID() + ".zip";

        File tempFile = new File(tempFilePath);
        byte[] sendingBuffer;
        byte[] receivedBuffer = new byte[1024];
        DatagramPacket receivedPacket = new DatagramPacket(receivedBuffer, receivedBuffer.length);

        serverSocket.receive(receivedPacket);
        int index = Integer.parseInt(new String(receivedPacket.getData(), 0, receivedPacket.getLength()));

        try {
            FileOutputStream fos = new FileOutputStream(tempFile);
            Figure figure = container.getFigures().get(index);
            FiguresContainer tempContainer = new FiguresContainer();

            tempContainer.addFigure(figure);
            controllerIO.writeFiguresToZIP(fos, format, tempContainer);
            fos.close();

            sendingBuffer = "Ok".getBytes();
            serverSocket.send(new DatagramPacket(sendingBuffer, sendingBuffer.length));
            sendingBuffer = String.format("%d", tempFile.length()).getBytes();
            serverSocket.send(new DatagramPacket(sendingBuffer, sendingBuffer.length));
        }
        catch (Exception ignored) {
            sendingBuffer = "Error".getBytes();
            serverSocket.send(new DatagramPacket(sendingBuffer, sendingBuffer.length));
            return;
        }

        FileInputStream fis = new FileInputStream(tempFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        int readBytes;

        sendingBuffer = new byte[DATA_LENGTH];

        while ((readBytes = bis.read(sendingBuffer)) != -1) {
            serverSocket.send(new DatagramPacket(sendingBuffer, readBytes));
        }

        bis.close();
        tempFile.delete();
    }

    public void readFigureFromStream(DatagramSocket clientSocket, int index) throws IOException {
        String tempFilePath = MenuBar.getInstance()
                .getCurrentDir().getAbsolutePath() + File.pathSeparator + UUID.randomUUID() + ".zip";

        File tempFile = new File(tempFilePath);
        FileOutputStream fos = new FileOutputStream(tempFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        byte[] sendingBuffer;
        byte[] receivedBuffer = new byte[DATA_LENGTH];
        DatagramPacket receivedPacket = new DatagramPacket(receivedBuffer, receivedBuffer.length);
        int totalReadBytes = 0;
        int readBytes;

        sendingBuffer = String.format("%d", index).getBytes();
        clientSocket.send(new DatagramPacket(sendingBuffer, sendingBuffer.length));

        clientSocket.receive(receivedPacket);
        String status = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
        if (!status.equals("Ok"))
            throw new IOException("Ошибка при получении объекта");

        clientSocket.receive(receivedPacket);
        int size = Integer.parseInt(new String(receivedPacket.getData(), 0, receivedPacket.getLength()));
        while (totalReadBytes < size) {
            clientSocket.receive(receivedPacket);
            readBytes = receivedPacket.getLength();

            if ((readBytes == -1))
                throw new IOException("Ошибка при получении объекта");

            totalReadBytes += readBytes;
            bos.write(receivedPacket.getData(), 0, readBytes); bos.flush();
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
    public void saveStateToFile(File file, StateFormat format) throws IOException {
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
