package CSLabs.Lab3.MenuBar;

import CSLabs.Lab3.Main;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@SuppressWarnings("DuplicatedCode")
class TextChooserItemMouseListener implements MouseListener {
    @Override
    public void mouseReleased(MouseEvent e) {
        Main mainFrame = Main.getInstance();
        MenuBar menuBar = MenuBar.getInstance();

        if (SwingUtilities.isLeftMouseButton(e)) {
            String selectedText = JOptionPane.showInputDialog(
                    mainFrame,
                    "Введите отображаемый текст:",
                    "Выбор отображаемого текста",
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (selectedText != null)
                menuBar.imagedText = selectedText;
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Выбор текста для отображения при щелчке правой кнопкой мыши.",
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
