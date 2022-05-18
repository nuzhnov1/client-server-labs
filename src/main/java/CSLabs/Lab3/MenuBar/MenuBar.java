package CSLabs.Lab3.MenuBar;

import CSLabs.Lab3.Network.TCPClient;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;

@SuppressWarnings({"unused"})
public class MenuBar extends JMenuBar {
    // Constants:

    private static MenuBar instance = null;

    // Data members:

    final Controller controller = new Controller();
    TCPClient client;

    StateFormat format = StateFormat.JSON;
    File currentDir = new File(Path.of("").toAbsolutePath().toString());
    File imageFile = new File("src/main/resources/images/Nut.png");
    String imagedText = "Справедливо";

    JMenuItem connectionItem;
    JMenuItem sizeRequestItem;
    JMenuItem getRequestItem;
    JMenuItem clearItem;
    JMenuItem closeItem;

    // Constructors:

    private MenuBar() {
        JMenu mainMenu = new JMenu("Меню");
        JMenu settingsMenu = new JMenu("Настройки");
        JMenu connectionMenu = new JMenu("Подключение к серверу");
        JMenu helpMenu = new JMenu("Помощь");

        JMenuItem saveItem = new JMenuItem("Сохранить текущее состояние");
        JMenuItem restoreItem = new JMenuItem("Восстановить состояние");
        JCheckBox serverActiveItem = new JCheckBox("Работа сервера", false);
        JMenuItem imageChooserItem = new JMenuItem("Выбрать изображение");
        JMenuItem textChooserItem = new JMenuItem("Выбрать отображаемый текст");
        JMenuItem formatItem = new JMenuItem("Формат файла состояния");
        connectionItem = new JMenuItem("Подключиться к серверу");
        sizeRequestItem = new JMenuItem("Получить размер вектора объектов");
        getRequestItem = new JMenuItem("Получить объект по номеру");
        clearItem = new JMenuItem("Очистить вектор объектов");
        closeItem = new JMenuItem("Закрыть соединение");

        saveItem.addMouseListener(new SaveItemMouseListener());
        restoreItem.addMouseListener(new RestoreItemMouseListener());
        serverActiveItem.addItemListener(e -> {
            if (controller.isServerActive())
                controller.suspendServer();
            else if (!controller.isServerRun())
                controller.startServer();
            else if (!controller.isServerDead())
                controller.activateServer();
        });
        imageChooserItem.addMouseListener(new ImageChooserItemMouseListener());
        textChooserItem.addMouseListener(new TextChooserItemMouseListener());
        helpMenu.addMouseListener(new HelpMenuMouseListener());
        formatItem.addMouseListener(new FormatChooserItemMouseListener());
        connectionItem.addMouseListener(new ConnectionListener());
        sizeRequestItem.addMouseListener(new SizeRequestListener());
        getRequestItem.addMouseListener(new GetRequestListener());
        clearItem.addMouseListener(new ClearListener());
        closeItem.addMouseListener(new CloseListener());

        sizeRequestItem.setEnabled(false);
        getRequestItem.setEnabled(false);
        clearItem.setEnabled(false);
        closeItem.setEnabled(false);

        mainMenu.add(saveItem);
        mainMenu.add(restoreItem);
        mainMenu.add(serverActiveItem);
        settingsMenu.add(imageChooserItem);
        settingsMenu.add(textChooserItem);
        settingsMenu.add(formatItem);
        connectionMenu.add(connectionItem);
        connectionMenu.add(sizeRequestItem);
        connectionMenu.add(getRequestItem);
        connectionMenu.add(clearItem);
        connectionMenu.add(closeItem);

        add(mainMenu);
        add(settingsMenu);
        add(connectionMenu);
        add(helpMenu);
    }

    public static MenuBar getInstance() {
        if (instance == null)
            instance = new MenuBar();

        return instance;
    }

    // Getters:

    public File getImageFile() { return imageFile; }
    public File getCurrentDir() { return currentDir; }
    public String getImagedText() { return imagedText; }
    public StateFormat getFormat() { return format; }
}
