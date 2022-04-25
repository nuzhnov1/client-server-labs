package CSLabs.Lab2.MenuBar;

import CSLabs.Lab2.Main;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class TextChooserItemMouseListener implements MouseListener {
    @Override
    public void mouseReleased(MouseEvent e) {
        Main mainFrame = Main.getInstance();
        MenuBar menuBar = MenuBar.getInstance();

        if (SwingUtilities.isLeftMouseButton(e)) {
            String selectedText = JOptionPane.showInputDialog(
                    mainFrame,
                    "Отображаемый текст:",
                    "Введите отображаемый текст",
                    JOptionPane.INFORMATION_MESSAGE
            );

            menuBar.setImagedText(selectedText);
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Ввод текст для отображения при щелчке правой кнопкой мыши.",
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
