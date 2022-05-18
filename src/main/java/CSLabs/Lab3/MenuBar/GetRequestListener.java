package CSLabs.Lab3.MenuBar;

import CSLabs.Lab3.Figures.FiguresContainer;
import CSLabs.Lab3.Main;
import CSLabs.Lab3.MainPanel.MainPanel;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

public class GetRequestListener implements MouseListener {
    private static int itemIndex = 0;

    @Override
    public void mouseReleased(MouseEvent e) {
        Main mainFrame = Main.getInstance();
        MenuBar menuBar = MenuBar.getInstance();

        if (SwingUtilities.isLeftMouseButton(e)) {
            try {
                itemIndex = Integer.parseInt(JOptionPane.showInputDialog(
                        mainFrame,
                        "Выберите индекс объекта",
                        "Выбор индекса",
                        JOptionPane.INFORMATION_MESSAGE
                ));

                if (itemIndex < 0)
                    throw new IllegalArgumentException();
            }
            catch (IllegalArgumentException error) {
                JOptionPane.showMessageDialog(
                        mainFrame,
                        "Недопустимое значение индекса",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            try {
                menuBar.client.work(GetRequestListener::GetFigure);
            }
            catch (Exception error) {
                JOptionPane.showMessageDialog(
                        mainFrame,
                        "Ошибка при выполнении запроса",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Получить фигуру от сервера по индексу.",
                    "Помощь",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    private static void GetFigure(InputStream is, OutputStream os) {
        Main mainFrame = Main.getInstance();
        CSLabs.Lab3.MainPanel.Controller controller = MainPanel.getInstance().getController();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

        try {
            bw.write("GetFigure\n");
            bw.flush();
            controller.readFigureFromStream(is, os, itemIndex);
        }
        catch (Exception error) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Ошибка при получении объекта",
                    "Сообщнение от сервера",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
