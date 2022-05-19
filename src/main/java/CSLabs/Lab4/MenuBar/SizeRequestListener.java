package CSLabs.Lab4.MenuBar;

import CSLabs.Lab4.Main;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

@SuppressWarnings("DuplicatedCode")
public class SizeRequestListener implements MouseListener {
    @Override
    public void mouseReleased(MouseEvent e) {
        Main mainFrame = Main.getInstance();
        MenuBar menuBar = MenuBar.getInstance();

        if (SwingUtilities.isLeftMouseButton(e)) {
            try {
                getSize(menuBar.clientSocket);
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

    private void getSize(DatagramSocket clientSocket) throws IOException {
        Main mainFrame = Main.getInstance();

        byte[] sendingBuffer = "GetSize".getBytes();
        DatagramPacket sendingPacket = new DatagramPacket(sendingBuffer, sendingBuffer.length);
        DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);

        clientSocket.send(sendingPacket);
        clientSocket.receive(receivedPacket);
        int count = Integer.parseInt(new String(receivedPacket.getData(), 0, receivedPacket.getLength()));

        JOptionPane.showMessageDialog(
                mainFrame,
                String.format("Количество фигур на сервере: %d.", count),
                "Сообщение от сервера",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
