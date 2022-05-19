package CSLabs.Lab4;

import CSLabs.Lab4.MainPanel.MainPanel;
import CSLabs.Lab4.MenuBar.MenuBar;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    // Constants:

    public static final int FPS = 60;

    // Static members:

    private static Main instance = null;

    // Data members:

    private static int localServerPort;
    private static int localClientPort;
    private static int remoteServerPort;
    private static int remoteClientPort;

    // Constructors:

    private Main() {
        setTitle("Лабораторная работа №4. Нужнов А.Н., Рымарь Р.А.");
        setSize(1024, 768);
        setLocation(100, 100);
        setResizable(true);
        setLayout(new BorderLayout());
        setExtendedState(NORMAL);
        setVisible(true);
        setAutoRequestFocus(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(MenuBar.getInstance(), BorderLayout.NORTH);
        add(MainPanel.getInstance(), BorderLayout.CENTER);
    }

    public static Main getInstance() {
        if (instance == null)
            instance = new Main();

        return instance;
    }

    // Getters:

    public static int getLocalServerPort() { return localServerPort; }
    public static int getLocalClientPort() { return localClientPort; }
    public static int getRemoteServerPort() { return remoteServerPort; }
    public static int getRemoteClientPort() { return remoteClientPort; }

    // Entry point:

    public static void main(String[] args) throws InterruptedException {
        Main mainFrame = getInstance();
        int len = args.length;

        localServerPort = Integer.parseInt(args[len - 4]);
        localClientPort = Integer.parseInt(args[len - 3]);
        remoteServerPort = Integer.parseInt(args[len - 2]);
        remoteClientPort = Integer.parseInt(args[len - 1]);

        while (mainFrame.isShowing()) {
            mainFrame.repaint();
            Thread.sleep(1000 / FPS);
        }
    }
}
