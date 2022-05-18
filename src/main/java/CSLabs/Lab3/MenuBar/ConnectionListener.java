package CSLabs.Lab3.MenuBar;

import CSLabs.Lab3.Main;
import CSLabs.Lab3.Network.TCPClient;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.InetAddress;

public class ConnectionListener implements MouseListener {
    @Override
    public void mouseReleased(MouseEvent e) {
        Main mainFrame = Main.getInstance();
        MenuBar menuBar = MenuBar.getInstance();

        if (SwingUtilities.isLeftMouseButton(e)) {
            try {
                int port = Integer.parseInt(JOptionPane.showInputDialog(
                        mainFrame,
                        "Порт:",
                        "Параметры подключения",
                        JOptionPane.INFORMATION_MESSAGE
                ));

                if (port < 0 || port > 65535)
                    throw new IllegalArgumentException();

                menuBar.client = new TCPClient();
                menuBar.client.connect(InetAddress.getLocalHost(), port);

                menuBar.connectionItem.setEnabled(false);
                menuBar.sizeRequestItem.setEnabled(true);
                menuBar.getRequestItem.setEnabled(true);
                menuBar.clearItem.setEnabled(true);
                menuBar.closeItem.setEnabled(true);
            }
            catch (IllegalArgumentException error) {
                JOptionPane.showMessageDialog(
                        mainFrame,
                        "Недопустимое значение порта",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            catch (IOException error) {
                JOptionPane.showMessageDialog(
                        mainFrame,
                        "Не удалось подключится к серверу",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Выбор параметров подключения.",
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
}
