package by.fin.example.services;

import by.fin.example.entities.CategoryModel;

import java.util.Optional;

public interface CategoryService extends CRUDService<CategoryModel, Long> {
    Optional<CategoryModel> findByName(String name);
}
