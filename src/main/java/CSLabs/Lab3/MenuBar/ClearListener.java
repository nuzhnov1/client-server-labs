package CSLabs.Lab3.MenuBar;

import CSLabs.Lab3.Main;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

public class ClearListener implements MouseListener {
    @Override
    public void mouseReleased(MouseEvent e) {
        Main mainFrame = Main.getInstance();
        MenuBar menuBar = MenuBar.getInstance();

        if (SwingUtilities.isLeftMouseButton(e)) {
            try {
                menuBar.client.work(ClearListener::Clear);
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
                    "Очистить фигуры на сервере.",
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

    private static void Clear(InputStream is, OutputStream os) throws IOException {
        Main mainFrame = Main.getInstance();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

        bw.write("Clear\n");
        bw.flush();
        String status = br.readLine();

        if (status.equals("Ok")) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Фигуры успешно удалены",
                    "Сообщение от сервера",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
        else {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Ошибка при удалении фигур",
                    "Сообщение от сервера",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
