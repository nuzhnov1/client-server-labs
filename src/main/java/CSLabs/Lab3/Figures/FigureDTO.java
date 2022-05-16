package CSLabs.Lab3.Figures;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@SuppressWarnings("unused")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "figure")
@JsonSubTypes({
    @Type(value = LoadedImageDTO.class, name = "image"),
    @Type(value = TextImageDTO.class, name = "text")
})
public class FigureDTO implements Serializable {
    // Data members:

    private double centerX, centerY;
    private double velocityX, velocityY;
    private boolean isMove;

    // Constructors:

    public FigureDTO(
            @JsonProperty(value = "centerX") double centerX,
            @JsonProperty(value = "centerY") double centerY,
            @JsonProperty(value = "velocityX") double velocityX,
            @JsonProperty(value = "velocityY") double velocityY,
            @JsonProperty(value = "isMove") boolean isMove)
    {
        this.centerX = centerX;
        this.centerY = centerY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.isMove = isMove;
    }

    // Getters:

    public double getCenterX() { return centerX; }
    public double getCenterY() { return centerY; }
    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }
    public boolean isMove() { return isMove; }

    // Setters:

    public void setCenterX(double centerX) { this.centerX = centerX; }
    public void setCenterY(double centerY) { this.centerY = centerY; }

    public void setCenter(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void setVelocityX(double velocityX) { this.velocityX = velocityX; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }

    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void setMove(boolean isMove) { this.isMove = isMove; }
}
