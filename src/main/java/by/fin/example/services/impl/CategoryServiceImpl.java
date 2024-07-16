package by.fin.example.services.impl;

import by.fin.example.entities.CategoryModel;
import by.fin.example.entities.ProductModel;
import by.fin.example.repositories.CategoryRepository;
import by.fin.example.services.CategoryService;
import by.fin.example.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ProductService productService;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
    }

    @Override
    @Transactional
    public CategoryModel save(CategoryModel categoryModel) {
        return categoryRepository.save(categoryModel);
    }

    @Override
    @Transactional
    public CategoryModel update(CategoryModel categoryModel) {
        return this.save(categoryModel);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        List<ProductModel> products = productService.findProductsByCategoryId(id);
        if (!products.isEmpty()) {
            products.forEach(product -> product.setCategory(null));
            productService.saveAll(products);
        }
        categoryRepository.deleteById(id);
        log.info("Category with id {} successfully deleted", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryModel> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<CategoryModel> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryModel> findAll() {
        return categoryRepository.findAll();
    }
}
