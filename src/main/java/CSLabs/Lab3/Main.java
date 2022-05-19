package CSLabs.Lab3;

import CSLabs.Lab3.MainPanel.MainPanel;
import CSLabs.Lab3.MenuBar.MenuBar;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    // Constants:

    public static final int FPS = 60;

    // Static members:

    private static Main instance = null;

    // Data members:

    private static int localServerPort;

    // Constructors:

    private Main() {
        setTitle("Лабораторная работа №3. Нужнов А.Н., Рымарь Р.А.");
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

    // Entry point:

    public static void main(String[] args) throws InterruptedException {
        int len = args.length;
        localServerPort = Integer.parseInt(args[len - 1]);

        Main main = getInstance();

        while (main.isShowing()) {
            main.repaint();
            Thread.sleep(1000 / FPS);
        }
    }
}
