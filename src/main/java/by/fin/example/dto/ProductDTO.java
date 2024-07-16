package by.fin.example.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String brand;

    @NotNull
    private String model;

    @NotNull
    private Integer quantityAvailable;

    @NotNull
    private Double weight;

    @NotNull
    private Double rating;

    @NotNull
    private String category;

    private String description;

    @NotNull
    private String color;

    private Double price;

    private List<String> features;

    @Valid
    private List<PhotoDTO> photos;
}
