package CSLabs.Lab2.MenuBar;

import CSLabs.Lab2.Main;
import CSLabs.Lab2.MainPanel.Controller;
import CSLabs.Lab2.MainPanel.MainPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

class RestoreItemMouseListener implements MouseListener {
    @Override
    public void mouseReleased(MouseEvent e) {
        Main mainFrame = Main.getInstance();
        MenuBar menuBar = MenuBar.getInstance();

        if (SwingUtilities.isLeftMouseButton(e)) {
            JFileChooser fileChooser = new JFileChooser(menuBar.getCurrentDir());

            fileChooser.setDialogTitle("Выбор zip-архива для восстановления состояния");
            fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("zip","zip"));

            int returnVal = fileChooser.showOpenDialog(mainFrame);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                MainPanel mainPanel = MainPanel.getInstance();
                Controller panelController = mainPanel.getController();
                File selectedFile = fileChooser.getSelectedFile();
                File selectedDir = selectedFile.getParentFile();

                menuBar.setCurrentDir(selectedDir);

                try {
                    panelController.restoreStateFromFile(selectedFile);
                }
                catch (Exception error) {
                    JOptionPane.showMessageDialog(
                            mainFrame,
                            "Сообщение об ошибке: " + error.getMessage(),
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
            else if (returnVal == JFileChooser.CANCEL_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                if (selectedFile != null) {
                    File selectedDir = selectedFile.getParentFile();
                    menuBar.setCurrentDir(selectedDir);
                }
            }
            else {
                JOptionPane.showMessageDialog(
                        mainFrame,
                        "Ошибка при восстановлении состояния",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    """
                    Восстановление состояния объектов программы из zip-архива,
                    генерируемого данной программой при сохранении.
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
