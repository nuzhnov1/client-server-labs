package CSLabs.Lab2.MenuBar;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;

@SuppressWarnings({"unused"})
public class MenuBar extends JMenuBar {
    private static MenuBar instance = null;

    private StateFormat format;     // Selected format of state files
    private File currentDir;        // Current directory for FileChooser
    private File imageFile;         // Selected image
    private String imagedText;      // Selected text
    private boolean isMove;         // Is all objects are moving in main panel?


    public enum StateFormat {
        JSON,
        XML,
        BIN
    }


    private MenuBar() {
        format = StateFormat.JSON;
        currentDir = new File(Path.of("").toAbsolutePath().toString());
        imageFile = new File("src/main/resources/images/Nut.png");
        imagedText = "Справедливо";
        isMove = true;

        JMenu mainMenu = new JMenu("Меню");
        JMenu settingsMenu = new JMenu("Настройки");
        JMenu helpMenu = new JMenu("Помощь");

        JMenuItem imageChooserItem = new JMenuItem("Выбрать изображение");
        JMenuItem textChooserItem = new JMenuItem("Выбрать отображаемый текст");
        JMenuItem saveItem = new JMenuItem("Сохранить текущее состояние");
        JMenuItem restoreItem = new JMenuItem("Восстановить состояние");
        JMenuItem formatItem = new JMenuItem("Формат сохранения состояния");

        imageChooserItem.addMouseListener(new ImageChooserItemMouseListener());
        imageChooserItem.getAccessibleContext().setAccessibleDescription("Hello!");
        textChooserItem.addMouseListener(new TextChooserItemMouseListener());
        saveItem.addMouseListener(new SaveItemMouseListener());
        restoreItem.addMouseListener(new RestoreItemMouseListener());
        helpMenu.addMouseListener(new HelpMenuMouseListener());
        formatItem.addMouseListener(new FormatChooserItemMouseListener());

        mainMenu.add(imageChooserItem);
        mainMenu.add(textChooserItem);
        mainMenu.add(saveItem);
        mainMenu.add(restoreItem);
        settingsMenu.add(formatItem);

        add(mainMenu);
        add(settingsMenu);
        add(helpMenu);
    }

    public static MenuBar getInstance() {
        if (instance == null)
            instance = new MenuBar();

        return instance;
    }


    public StateFormat getFormat() { return format; }
    public File getCurrentDir() { return currentDir; }
    public File getImageFile() { return imageFile; }
    public String getImagedText() { return imagedText; }
    public boolean isMove() { return isMove; }

    public void setFormat(StateFormat format) { this.format = format; }
    public void setCurrentDir(File currentDir) { this.currentDir = currentDir; }
    public void setImageFile(File imageFile) { this.imageFile = imageFile; }
    public void setImagedText(String imagedText) { this.imagedText = imagedText; }
    public void setMove(boolean isMove) { this.isMove = isMove; }
}
