package by.fin.example.facades;

import by.fin.example.dto.CategoryDTO;

public interface CategoryFacade extends CRUDFacade<CategoryDTO, Long> {
    CategoryDTO findByName(String name);
}
