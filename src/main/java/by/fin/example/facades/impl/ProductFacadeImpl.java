package by.fin.example.facades.impl;

import by.fin.example.dto.ProductDTO;
import by.fin.example.entities.ProductModel;
import by.fin.example.facades.ProductFacade;
import by.fin.example.mappers.ProductMapper;
import by.fin.example.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductFacadeImpl implements ProductFacade {

    private ProductService productService;
    private ProductMapper productMapper;

    @Autowired
    public ProductFacadeImpl(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDTO findHighestRatingProduct() {
        return productMapper.toDTO(productService.findHighestRatingProduct());
    }

    @Override
    public ProductDTO findMostExpensiveProduct() {
        return productMapper.toDTO(productService.findMostExpensiveProduct());
    }

    @Override
    public ProductDTO findCheapestProduct() {
        return productMapper.toDTO(productService.findCheapestProduct());
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        ProductModel saved = productService.save(productMapper.toModel(productDTO));
        return productMapper.toDTO(saved);
    }

    @Override
    public ProductDTO update(ProductDTO productDTO) {
        return this.save(productDTO);
    }

    @Override
    public void delete(Long id) {
        productService.delete(id);
    }

    @Override
    public ProductDTO findById(Long id) {
        Optional<ProductModel> productOptional = productService.findById(id);
        return productOptional.map(productMapper::toDTO).orElse(null);
    }

    @Override
    public ProductDTO findByName(String name) {
        Optional<ProductModel> productOptional = productService.findByName(name);
        return productOptional.map(productMapper::toDTO).orElse(null);
    }

    @Override
    public List<ProductDTO> findAll() {
        return productService.findAll().stream()
                .map(productMapper::toDTO)
                .toList();
    }
}
