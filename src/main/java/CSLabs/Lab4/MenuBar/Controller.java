package CSLabs.Lab4.MenuBar;

import CSLabs.Lab4.Main;
import CSLabs.Lab4.MainPanel.MainPanel;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@SuppressWarnings({"unused", "DuplicatedCode"})
public class Controller {
    // Data members:

    private DatagramSocket serverSocket;

    // Constructors:

    public Controller() {}

    // Start methods:

    public void startServer() {
        try {
            serverSocket = new DatagramSocket(Main.getLocalServerPort(), InetAddress.getLocalHost());
            serverSocket.connect(InetAddress.getLocalHost(), Main.getRemoteClientPort());
            Thread serverThread = new Thread(() -> {
                try {
                    serverControl(serverSocket);
                }
                catch (IOException ignored) {}
            });

            serverThread.start();
        }
        catch (Exception error) {
            JOptionPane.showMessageDialog(
                    Main.getInstance(),
                    String.format("Ошибка при создании сервера. Сообщение ошибки: %s.", error.getMessage()),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Control methods:

    public static void serverControl(DatagramSocket serverSocket) throws IOException {
        CSLabs.Lab4.MainPanel.Controller controller = MainPanel.getInstance().getController();
        MenuBar menuBar = MenuBar.getInstance();

        byte[] sendingBuffer;
        DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);

        while (true) {
            serverSocket.receive(receivedPacket);
            String message = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

            switch (message) {
                case "GetSize" -> {
                    sendingBuffer = String.format("%d", controller.getSize()).getBytes();
                    serverSocket.send(new DatagramPacket(sendingBuffer, sendingBuffer.length));
                }
                case "GetFigure" -> controller.writeFigureToNetwork(serverSocket, menuBar.format);
                case "Clear" -> {
                    sendingBuffer = "Ok".getBytes();
                    serverSocket.send(new DatagramPacket(sendingBuffer, sendingBuffer.length));
                    controller.removeAllFigures();
                }
                default -> {}
            }
        }
    }

    public void activateServer() { startServer(); }
    public void suspendServer() { serverSocket.close(); }

    // Getters:

    public boolean isServerActive() { return serverSocket != null && serverSocket.isConnected(); }
    public boolean isServerSuspend() { return !serverSocket.isConnected(); }
}
