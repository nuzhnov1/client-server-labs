package CSLabs.Lab2.MenuBar;

public enum StateFormat {
    JSON, XML, BIN;

    public static StateFormat parse(String extension) {
        try {
            return StateFormat.valueOf(extension.toUpperCase());
        }
        catch (IllegalArgumentException ignored) {
            throw new IllegalArgumentException("недопустимый формат данных файла состояния");
        }
    }

    public static boolean isFormat(String extension) {
        try { parse(extension); return true; }
        catch (IllegalArgumentException ignored) { return false; }
    }
}
