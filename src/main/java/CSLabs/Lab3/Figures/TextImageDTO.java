package CSLabs.Lab3.Figures;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@SuppressWarnings("unused")
@JsonTypeName(value = "text")
public class TextImageDTO extends FigureDTO {
    // Data members:

    private String text;
    private double initX;
    private double initY;

    // Constructors:

    public TextImageDTO(
            @JsonProperty(value = "centerX") double centerX,
            @JsonProperty(value = "centerY") double centerY,
            @JsonProperty(value = "velocityX") double velocityX,
            @JsonProperty(value = "velocityY") double velocityY,
            @JsonProperty(value = "isMove") boolean isMove,
            @JsonProperty(value = "text") String text,
            @JsonProperty(value = "initX") double initX,
            @JsonProperty(value = "initY") double initY)
    {
        super(centerX, centerY, velocityX, velocityY, isMove);

        this.text = text;
        this.initX = initX;
        this.initY = initY;
    }

    // Getters:

    public String getText() { return text; }
    public double getInitX() { return initX; }
    public double getInitY() { return initY; }

    // Setters:

    public void setText(String text) { this.text = text; }
    public void setInitX(double initX) { this.initX = initX; }
    public void setInitY(double initY) { this.initY = initY; }
}
