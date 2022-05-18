package CSLabs.Lab3.Figures;

import javax.swing.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings({"unused", "Convert2Diamond", "PatternVariableCanBeUsed"})
public class FiguresContainer {
    // Data members:

    private final HashSet<Figure> figures;
    private final HashMap<String, ImageIcon> imageMap;

    // Constructors:

    public FiguresContainer() {
        figures = new HashSet<Figure>();
        imageMap = new HashMap<String, ImageIcon>();
    }

    public FiguresContainer(List<? extends Figure> figuresList) {
        figures = new HashSet<Figure>(figuresList);
        imageMap = new HashMap<String, ImageIcon>();

        figures.forEach((figure -> {
            if (figure instanceof LoadedImage) {
                LoadedImage loadedImage = (LoadedImage) figure;

                if (!imageMap.containsKey(loadedImage.getImageName()))
                    imageMap.put(loadedImage.getImageName(), loadedImage.getImage());
            }
        }));
    }

    public FiguresContainer(List<? extends Figure> figuresList, HashMap<String, ImageIcon> nameToImageMap) {
        figures = new HashSet<Figure>(figuresList);
        imageMap = nameToImageMap;
    }

    // Main methods:

    public void addLoadedImage(LoadedImage loadedImage) {
        if (!imageMap.containsKey(loadedImage.getImageName()))
            imageMap.put(loadedImage.getImageName(), loadedImage.getImage());

        figures.add(loadedImage);
    }

    public void addTextImage(TextImage textImage) { figures.add(textImage); }

    public void remove(Figure figure) { figures.remove(figure); }

    public void clear() {
        figures.clear();
        imageMap.clear();
    }

    // Getters:

    public List<? extends Figure> getFigures() { return figures.stream().toList(); }
    public HashMap<String, ImageIcon> getImageMap() { return imageMap; }
    public boolean isContainsImage(String imageName) { return imageMap.containsKey(imageName); }
}
