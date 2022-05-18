package CSLabs.Lab3.Figures;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

@SuppressWarnings("unused")
public class FiguresContainerDTO {
    // Data members:

    private FiguresDTOSet figuresDTO;
    private HashMap<String, String> imageMapDTO;

    // Constructors:

    public FiguresContainerDTO(
            @JsonProperty(value = "figuresDTO") FiguresDTOSet figuresDTO,
            @JsonProperty(value = "imageMapDTO") HashMap<String, String> imageMapDTO
    )
    {
        this.figuresDTO = figuresDTO;
        this.imageMapDTO = imageMapDTO;
    }

    // Getters:

    public FiguresDTOSet getFiguresDTO() { return figuresDTO; }
    public HashMap<String, String> getImageMapDTO() { return imageMapDTO; }

    // Setters:

    public void setFiguresDTO(FiguresDTOSet figuresDTO) { this.figuresDTO = figuresDTO; }
    public void setImageMapDTO(HashMap<String, String> imageMapDTO) { this.imageMapDTO = imageMapDTO; }
}
