package CSLabs.Lab2.Figures;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@SuppressWarnings("unused")
@JsonTypeName(value = "image")
public class LoadedImageDTO extends FigureDTO {
    // Data members:

    private String imageName;
    private int width;
    private int height;

    // Constructors:

    public LoadedImageDTO(
            @JsonProperty(value = "centerX") double centerX,
            @JsonProperty(value = "centerY") double centerY,
            @JsonProperty(value = "velocityX") double velocityX,
            @JsonProperty(value = "velocityY") double velocityY,
            @JsonProperty(value = "isMove") boolean isMove,
            @JsonProperty(value = "imageName") String imageName,
            @JsonProperty(value = "width") int width,
            @JsonProperty(value = "height") int height)
    {
        super(centerX, centerY, velocityX, velocityY, isMove);

        this.imageName = imageName;
        this.width = width;
        this.height = height;
    }

    // Getters:

    public String getImageName() { return imageName; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    // Setters:

    public void setImageName(String imageName) { this.imageName = imageName; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
}
