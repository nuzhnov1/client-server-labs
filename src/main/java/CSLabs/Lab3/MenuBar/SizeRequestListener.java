package CSLabs.Lab3.MenuBar;

import CSLabs.Lab3.Main;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

public class SizeRequestListener implements MouseListener {
    @Override
    public void mouseReleased(MouseEvent e) {
        Main mainFrame = Main.getInstance();
        MenuBar menuBar = MenuBar.getInstance();

        if (SwingUtilities.isLeftMouseButton(e)) {
            try {
                menuBar.client.work(SizeRequestListener::getSize);
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
                    "Получить количество фигур на сервере.",
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

    private static void getSize(InputStream is, OutputStream os) throws IOException {
        Main mainFrame = Main.getInstance();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

        bw.write("GetSize\n");
        bw.flush();
        int count = br.read();

        JOptionPane.showMessageDialog(
                mainFrame,
                String.format("Количество фигур на сервере: %d.", count),
                "Сообщение от сервера",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
