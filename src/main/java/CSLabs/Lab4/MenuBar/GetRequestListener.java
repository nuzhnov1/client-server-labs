package CSLabs.Lab4.MenuBar;

import CSLabs.Lab4.Main;
import CSLabs.Lab4.MainPanel.MainPanel;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

@SuppressWarnings("DuplicatedCode")
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
                getFigure(menuBar.clientSocket);
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

    private void getFigure(DatagramSocket clientSocket) {
        Main mainFrame = Main.getInstance();
        CSLabs.Lab4.MainPanel.Controller controller = MainPanel.getInstance().getController();

        byte[] sendingBuffer = "GetFigure".getBytes();
        DatagramPacket sendingPacket = new DatagramPacket(sendingBuffer, sendingBuffer.length);

        try {
            clientSocket.send(sendingPacket);
            controller.readFigureFromStream(clientSocket, itemIndex);
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
