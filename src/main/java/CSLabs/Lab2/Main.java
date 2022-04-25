package CSLabs.Lab2;

import javax.swing.*;
import java.awt.*;

import CSLabs.Lab2.MainPanel.MainPanel;
import CSLabs.Lab2.MenuBar.MenuBar;

public class Main extends JFrame {
    // Constants:

    public static final int FPS = 60;
    private static Main instance = null;

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


    public static void main(String[] args) throws InterruptedException {
        Main main = getInstance();

        while (main.isShowing()) {
            main.repaint();
            Thread.sleep(1000 / FPS);
        }
    }
}
