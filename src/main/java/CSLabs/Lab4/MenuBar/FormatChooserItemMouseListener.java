package CSLabs.Lab4.MenuBar;

import CSLabs.Lab4.Main;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@SuppressWarnings("DuplicatedCode")
public
class FormatChooserItemMouseListener implements MouseListener {
    @Override
    public void mouseReleased(MouseEvent e) {
        Main mainFrame = Main.getInstance();
        MenuBar menuBar = MenuBar.getInstance();

        if (SwingUtilities.isLeftMouseButton(e)) {
            StateFormat selectedFormat = (StateFormat) JOptionPane.showInputDialog(
                    mainFrame,
                    "Выберите формат файла состояния:",
                    "Выбор формата файла состояния",
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    StateFormat.values(),
                    menuBar.format
            );

            if (selectedFormat != null)
                menuBar.format = selectedFormat;
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    """
                    Выбор формата файла состояния (файл state), который будет помещён в zip-архив,
                    при сохранении состояния программы.
                    """,
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
