package by.fin.example.services;

import by.fin.example.entities.ProductModel;

import java.util.List;
import java.util.Optional;

public interface ProductService extends CRUDService<ProductModel, Long> {

    ProductModel findHighestRatingProduct();
    ProductModel findMostExpensiveProduct();
    ProductModel findCheapestProduct();
    Optional<ProductModel> findByName(String name);
    List<ProductModel> findProductsByCategoryId(Long id);
    List<ProductModel> saveAll(List<ProductModel> products);
}
