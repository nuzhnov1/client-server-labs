package CSLabs.Lab4.MenuBar;

import CSLabs.Lab4.Main;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

@SuppressWarnings("DuplicatedCode")
public class ClearListener implements MouseListener {
    @Override
    public void mouseReleased(MouseEvent e) {
        Main mainFrame = Main.getInstance();
        MenuBar menuBar = MenuBar.getInstance();

        if (SwingUtilities.isLeftMouseButton(e)) {
            try {
                clear(menuBar.clientSocket);
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

    private void clear(DatagramSocket clientSocket) throws IOException {
        Main mainFrame = Main.getInstance();

        byte[] sendingBuffer = "Clear".getBytes();
        DatagramPacket sendingPacket = new DatagramPacket(sendingBuffer, sendingBuffer.length);
        DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);

        clientSocket.send(sendingPacket);
        clientSocket.receive(receivedPacket);
        String status = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

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
