package by.fin.example.mappers;

import by.fin.example.dto.PhotoDTO;
import by.fin.example.entities.PhotoModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhotoMapper {
    PhotoModel toModel(PhotoDTO photoDTO);
    PhotoDTO toDTO(PhotoModel photoModel);
}
