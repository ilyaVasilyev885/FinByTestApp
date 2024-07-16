package by.fin.example.facades.impl;

import by.fin.example.dto.CategoryDTO;
import by.fin.example.entities.CategoryModel;
import by.fin.example.facades.CategoryFacade;
import by.fin.example.mappers.CategoryMapper;
import by.fin.example.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryFacadeImpl implements CategoryFacade {

    private CategoryService categoryService;
    private CategoryMapper categoryMapper;

    @Autowired
    public CategoryFacadeImpl(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        CategoryModel saved = categoryService.save(categoryMapper.toModel(categoryDTO));
        return categoryMapper.toDTO(saved);
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO) {
        return this.save(categoryDTO);
    }

    @Override
    public void delete(Long id) {
        categoryService.delete(id);
    }

    @Override
    public CategoryDTO findById(Long id) {
        Optional<CategoryModel> category = categoryService.findById(id);
        return category.map(categoryMapper::toDTO).orElse(null);
    }

    @Override
    public CategoryDTO findByName(String name) {
        Optional<CategoryModel> category = categoryService.findByName(name);
        return category.map(categoryMapper::toDTO).orElse(null);
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryService.findAll().stream()
                .map(categoryMapper::toDTO)
                .toList();
    }
}
