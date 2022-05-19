package CSLabs.Lab4.MenuBar;

import CSLabs.Lab4.Main;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.DatagramSocket;

public class CloseListener implements MouseListener {
    @Override
    public void mouseReleased(MouseEvent e) {
        Main mainFrame = Main.getInstance();
        MenuBar menuBar = MenuBar.getInstance();

        if (SwingUtilities.isLeftMouseButton(e)) {
            try {
                close(menuBar.clientSocket);
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

    private void close(DatagramSocket clientSocket) {
        MenuBar menuBar = MenuBar.getInstance();

        clientSocket.close();

        menuBar.sizeRequestItem.setEnabled(false);
        menuBar.getRequestItem.setEnabled(false);
        menuBar.clearItem.setEnabled(false);
        menuBar.closeItem.setEnabled(false);
        menuBar.connectionItem.setEnabled(true);
    }
}
