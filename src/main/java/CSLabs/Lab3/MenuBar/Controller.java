package CSLabs.Lab3.MenuBar;

import CSLabs.Lab3.Main;
import CSLabs.Lab3.MainPanel.MainPanel;
import CSLabs.Lab3.Network.TCPServer;

import javax.swing.*;
import java.io.*;

@SuppressWarnings("unused")
public class Controller {
    // Data members:

    private TCPServer server;

    // Constructors:

    public Controller() {
        String[] args = Main.getArgs();
        int port = (args.length > 4) ? Integer.parseInt(args[4]) : 5782;

        try {
            server = new TCPServer(Controller::serverControl, port);
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

    // Start methods:

    void startServer() {
        if (server == null)
            return;

        Thread serverThread = new Thread(() -> {
            try {
                server.run();
            }
            catch (Exception error) {
                JOptionPane.showMessageDialog(
                        Main.getInstance(),
                        String.format("Ошибка при запуске сервера. Сообщение ошибки: %s.", error.getMessage()),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        serverThread.start();
    }

    // Control methods:

    public static void serverControl(InputStream is, OutputStream os) throws IOException {
        CSLabs.Lab3.MainPanel.Controller controller = MainPanel.getInstance().getController();
        MenuBar menuBar = MenuBar.getInstance();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

        while (true) {
            String message = br.readLine();

            if (message == null)
                return;

            switch (message) {
                case "GetSize":
                    bw.write(controller.getSize());
                    bw.flush();
                    break;
                case "GetFigure": {
                    controller.writeFigureToNetwork(is, os, menuBar.format);
                    break;
                }
                case "Clear": {
                    bw.write("Ok\n");
                    bw.flush();
                    controller.removeAllFigures();

                    break;
                }
                case "Close": return;
                default: {}
            }
        }
    }

    public void activateServer() { server.activate(); }
    public void suspendServer() { server.pause(); }

    // Getters:

    public boolean isServerRun() { return server.isRun(); }
    public boolean isServerActive() { return server.isActive(); }
    public boolean isServerSuspend() { return server.isPause(); }
    public boolean isServerDead() { return server.isDead(); }
}
