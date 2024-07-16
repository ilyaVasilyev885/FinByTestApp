package by.fin.example.facades;

import by.fin.example.dto.ProductDTO;

public interface ProductFacade extends CRUDFacade<ProductDTO, Long> {
    ProductDTO findHighestRatingProduct();
    ProductDTO findMostExpensiveProduct();
    ProductDTO findCheapestProduct();
    ProductDTO findByName(String name);
}
