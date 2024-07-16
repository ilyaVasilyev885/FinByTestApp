package by.fin.example.repositories;

import by.fin.example.entities.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {

    ProductModel findTopByOrderByRatingDesc();
    ProductModel findFirstByOrderByPriceDesc();
    ProductModel findFirstByOrderByPriceAsc();
    Optional<ProductModel> findByName(String name);
    List<ProductModel> findByCategoryId(Long categoryId);
}
