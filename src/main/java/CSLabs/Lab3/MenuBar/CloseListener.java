package CSLabs.Lab3.MenuBar;

import CSLabs.Lab3.Main;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

public class CloseListener implements MouseListener {
    @Override
    public void mouseReleased(MouseEvent e) {
        Main mainFrame = Main.getInstance();
        MenuBar menuBar = MenuBar.getInstance();

        if (SwingUtilities.isLeftMouseButton(e)) {
            try {
                menuBar.client.work(CloseListener::Close);
                menuBar.client.close();
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
                    "Закрыть соединение с сервером.",
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

    private static void Close(InputStream is, OutputStream os) throws IOException {
        MenuBar menuBar = MenuBar.getInstance();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

        bw.write("Close\n"); bw.flush();

        menuBar.sizeRequestItem.setEnabled(false);
        menuBar.getRequestItem.setEnabled(false);
        menuBar.clearItem.setEnabled(false);
        menuBar.closeItem.setEnabled(false);
        menuBar.connectionItem.setEnabled(true);
    }
}
