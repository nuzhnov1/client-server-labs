package CSLabs.Lab2.MenuBar;

import CSLabs.Lab2.Main;
import CSLabs.Lab2.MainPanel.MainPanel;
import CSLabs.Lab2.MainPanel.Controller;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;

class RestoreItemMouseListener implements MouseListener {
    @Override
    public void mouseReleased(MouseEvent e) {
        Main mainFrame = Main.getInstance();
        MenuBar menuBar = MenuBar.getInstance();

        if (SwingUtilities.isLeftMouseButton(e)) {
            JFileChooser fileChooser = new JFileChooser(menuBar.getCurrentDir());

            fileChooser.setDialogTitle("Выбор файла для восстановления состояния");
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
                        "Ошибка при восстановлении состояния!",
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

    private void readFromBinaryStream(FileInputStream stream) {
//        try (var is = new ObjectInputStream(stream)) {
//            var figures = mainPanel.getComponents();
//            HashMap<String, ImageIcon> imageMapper;
//
//            Arrays.asList(figures).forEach(f -> mainPanel.remove(f));
//            //mainPanel.setImageMapper((HashMap<String, ImageIcon>) is.readObject());
//            figures = (Component[])is.readObject();
//            Arrays.asList(figures).forEach(f -> mainPanel.add(f));
//        }
//        catch (ClassNotFoundException error) {
//            JOptionPane.showMessageDialog(
//                    mainPanel,
//                    "Не удалось восстановить данные из двоичного файла: неизвестный тип данных",
//                    "Ошибка",
//                    JOptionPane.ERROR_MESSAGE
//            );
//        }
//        catch (InvalidClassException | StreamCorruptedException | OptionalDataException error) {
//            JOptionPane.showMessageDialog(
//                    mainPanel,
//                    "Не удалось восстановить данные из двоичного файла: ошибка при десериализации",
//                    "Ошибка",
//                    JOptionPane.ERROR_MESSAGE
//            );
//        } catch (IOException error) {
//            JOptionPane.showMessageDialog(
//                    mainPanel,
//                    "Не удалось прочитать данные из двоичного файла",
//                    "Ошибка",
//                    JOptionPane.ERROR_MESSAGE
//            );
//        }
    }

    private void readFromXMLStream(FileInputStream stream) {
//        try {
//            var xmlMapper = new XmlMapper();
//
//            mainPanel.removeAll();
//            List<Object> lst = xmlMapper.readValue(stream, new TypeReference<List<Object>>(){});
//            List<ProxyLoadedImage> p = xmlMapper.convertValue(lst, new TypeReference<List<ProxyLoadedImage>>() {});
//            setFigures(p);
//        }
//        catch (JsonProcessingException error) {
//            JOptionPane.showMessageDialog(
//                    mainPanel,
//                    "Не удалось восстановить данные в формате XML",
//                    "Ошибка",
//                    JOptionPane.ERROR_MESSAGE
//            );
//        }
//        catch (IOException error) {
//            JOptionPane.showMessageDialog(
//                    mainPanel,
//                    "Не удалось восстановить данные из файла XML",
//                    "Ошибка",
//                    JOptionPane.ERROR_MESSAGE
//            );
//        }
    }

    private void readFromJsonStream(FileInputStream stream) {
//        try {
//            var jsonMapper = new JsonMapper();
//
//            mainPanel.removeAll();
//            List<Object> lst = jsonMapper.readValue(stream, new TypeReference<List<Object>>(){});
//            List<ProxyLoadedImage> p = jsonMapper.convertValue(lst, new TypeReference<List<ProxyLoadedImage>>() {});
//            setFigures(p);
//        }
//        catch (JsonProcessingException error) {
//            JOptionPane.showMessageDialog(
//                    mainPanel,
//                    "Не удалось восстановить данные в формате JSON",
//                    "Ошибка",
//                    JOptionPane.ERROR_MESSAGE
//            );
//        }
//        catch (IOException error) {
//            JOptionPane.showMessageDialog(
//                    mainPanel,
//                    "Не удалось восстановить данные из файла JSON",
//                    "Ошибка",
//                    JOptionPane.ERROR_MESSAGE
//            );
//        }
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
