package by.fin.example.mappers;

import by.fin.example.dto.CategoryDTO;
import by.fin.example.entities.CategoryModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDTO(CategoryModel categoryModel);
    CategoryModel toModel(CategoryDTO categoryDTO);
}
