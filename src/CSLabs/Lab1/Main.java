package CSLabs.Lab1;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public static final int FPS = 60;

    public Main() {
        setTitle("Лабораторная работа №2. Нужнов А.Н., Рымарь Р.А.");
        setSize(1024, 768);
        setLocation(100, 100);
        setResizable(true);
        setLayout(new BorderLayout());
        setExtendedState(NORMAL);
        setVisible(true);
        setAutoRequestFocus(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        var mainPanel = new MainPanel();
        var menuBar = new JMenuBar();
        var helpMenu = new JMenu("Помощь");

        helpMenu.add(new JMenuItem("Для остановки/возобновления движения всех объектов нажмите клавишу S"));
        helpMenu.add(new JMenuItem("Для удаления всех объектов нажмите клавишу D"));

        menuBar.add(new JMenu("Меню"));
        menuBar.add(helpMenu);

        add(menuBar, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();

        while (main.isShowing()) {
            main.repaint();
            Thread.sleep(1000 / FPS);
        }
    }
}
