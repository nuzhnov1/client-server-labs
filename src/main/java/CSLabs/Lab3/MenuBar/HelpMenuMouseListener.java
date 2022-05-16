package CSLabs.Lab3.MenuBar;

import CSLabs.Lab3.Main;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class HelpMenuMouseListener implements MouseListener {
    @Override
    public void mousePressed(MouseEvent e) {
        Main mainFrame = Main.getInstance();

        if (SwingUtilities.isLeftMouseButton(e)) {
            JOptionPane.showMessageDialog(
                    mainFrame,
            """
                    Для остановки/возобновления движения всех объектов нажмите клавишу S.
                    Для удаления всех объектов нажмите клавишу D.
                    Для получения дополнительной помощи щёлкните по элементу управления правой
                    кнопкой мыши.
                    """,
                    "Помощь",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
