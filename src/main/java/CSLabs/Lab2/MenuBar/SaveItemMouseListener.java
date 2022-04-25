package CSLabs.Lab2.MenuBar;

import CSLabs.Lab2.Main;
import CSLabs.Lab2.MainPanel.Controller;
import CSLabs.Lab2.MainPanel.MainPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

class SaveItemMouseListener implements MouseListener {
    @Override
    public void mouseReleased(MouseEvent e) {
        Main mainFrame = Main.getInstance();
        MenuBar menuBar = MenuBar.getInstance();

        if (SwingUtilities.isLeftMouseButton(e)) {
            JFileChooser fileChooser = new JFileChooser(menuBar.getCurrentDir());

            fileChooser.setDialogTitle("Сохранение состояния");
            fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("zip","zip"));

            int returnVal = fileChooser.showSaveDialog(mainFrame);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                MainPanel mainPanel = MainPanel.getInstance();
                Controller panelController = mainPanel.getController();
                File selectedFile = fileChooser.getSelectedFile();
                File selectedDir = selectedFile.getParentFile();

                menuBar.setCurrentDir(selectedDir);

                try {
                    panelController.saveStateToFile(selectedFile);
                }
                catch (Exception error) {
                    JOptionPane.showMessageDialog(
                            mainFrame,
                            String.format("Ошибка: %s", error.getMessage()),
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
                        "Ошибка при сохранении состояния!",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    """
                    Сохранение текущего состояние объектов программы в zip-архив, состоящий из
                    файла состояния и изображений.
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
