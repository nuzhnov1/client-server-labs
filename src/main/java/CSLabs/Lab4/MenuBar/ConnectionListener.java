package CSLabs.Lab4.MenuBar;

import CSLabs.Lab4.Main;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

@SuppressWarnings("DuplicatedCode")
public class ConnectionListener implements MouseListener {
    @Override
    public void mouseReleased(MouseEvent e) {
        Main mainFrame = Main.getInstance();
        MenuBar menuBar = MenuBar.getInstance();

        if (SwingUtilities.isLeftMouseButton(e)) {
            try {
                menuBar.clientSocket = new DatagramSocket(Main.getLocalClientPort(), InetAddress.getLocalHost());
                menuBar.clientSocket.connect(InetAddress.getLocalHost(), Main.getRemoteServerPort());

                menuBar.connectionItem.setEnabled(false);
                menuBar.sizeRequestItem.setEnabled(true);
                menuBar.getRequestItem.setEnabled(true);
                menuBar.clearItem.setEnabled(true);
                menuBar.closeItem.setEnabled(true);
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
