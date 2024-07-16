package by.fin.example.dto;

import lombok.*;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDTO {

    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String url;
}
