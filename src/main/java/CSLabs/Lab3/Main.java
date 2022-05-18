package CSLabs.Lab3;

import CSLabs.Lab3.MainPanel.MainPanel;
import CSLabs.Lab3.MenuBar.MenuBar;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    // Constants:

    public static final int FPS = 60;
    private static Main instance = null;

    // Data members:

    private static String[] s_args;

    // Constructors:

    private Main() {
        setTitle("Лабораторная работа №2. Нужнов А.Н., Рымарь Р.А.");
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

    public static String[] getArgs() { return s_args;}

    // Entry point:

    public static void main(String[] args) throws InterruptedException {
        s_args = args;
        Main main = getInstance();

        while (main.isShowing()) {
            main.repaint();
            Thread.sleep(1000 / FPS);
        }
    }
}
