package by.fin.example.services.impl;

import by.fin.example.entities.FeatureModel;
import by.fin.example.entities.PhotoModel;
import by.fin.example.entities.ProductModel;
import by.fin.example.repositories.CategoryRepository;
import by.fin.example.repositories.FeatureRepository;
import by.fin.example.repositories.PhotoRepository;
import by.fin.example.repositories.ProductRepository;
import by.fin.example.services.ProductService;
import by.fin.example.util.ProductUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static by.fin.example.util.ProductUtil.updateProductDescription;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private FeatureRepository featureRepository;
    private CategoryRepository categoryRepository;
    private PhotoRepository photoRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              FeatureRepository featureRepository,
                              CategoryRepository categoryRepository,
                              PhotoRepository photoRepository
    ) {
        this.productRepository = productRepository;
        this.featureRepository = featureRepository;
        this.categoryRepository = categoryRepository;
        this.photoRepository = photoRepository;
    }

    @Override
    public ProductModel findHighestRatingProduct() {
        return productRepository.findTopByOrderByRatingDesc();
    }

    @Override
    public ProductModel findMostExpensiveProduct() {
        return productRepository.findFirstByOrderByPriceDesc();
    }

    @Override
    public ProductModel findCheapestProduct() {
        return productRepository.findFirstByOrderByPriceAsc();
    }

    @Override
    public List<ProductModel> findProductsByCategoryId(Long id) {
        return productRepository.findByCategoryId(id);
    }

    @Override
    @Transactional
    public ProductModel save(ProductModel product) {
        updateProductDescription(product);
        saveFeatures(product);
        saveCategory(product);
        savePhotos(product);
        return productRepository.save(product);
    }

    private void saveCategory(ProductModel product) {
        Optional.ofNullable(product.getCategory())
                .ifPresent(category -> {
                    categoryRepository.findByName(category.getName())
                            .ifPresentOrElse(
                                    product::setCategory,
                                    () -> categoryRepository.save(category)
                            );
                });
    }

    private void saveFeatures(ProductModel product) {
        List<FeatureModel> features = product.getFeatures();
        if (features == null) {
            product.setFeatures(Collections.emptyList());
            return;
        }

        List<FeatureModel> savedFeatures = features.stream()
                .map(feature -> featureRepository.findByName(feature.getName())
                        .orElseGet(() -> featureRepository.save(feature)))
                .collect(Collectors.toList());
        product.setFeatures(savedFeatures);
    }

    private void savePhotos(ProductModel product) {
        List<PhotoModel> photos = product.getPhotos();
        if (photos == null) {
            product.setPhotos(Collections.emptyList());
            return;
        }

        List<PhotoModel> savedPhotos = photos.stream()
                .map(photo -> photoRepository.findByName(photo.getName())
                        .orElseGet(() -> photoRepository.save(photo)))
                .collect(Collectors.toList());
        product.setPhotos(savedPhotos);
    }

    @Override
    @Transactional
    public List<ProductModel> saveAll(List<ProductModel> products) {
        products.forEach(ProductUtil::updateProductDescription);
        products.forEach(this::saveCategory);
        products.forEach(this::saveFeatures);
        products.forEach(this::savePhotos);

        return productRepository.saveAll(products);
    }

    @Override
    @Transactional
    public ProductModel update(ProductModel product) {
        return this.save(product);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
        log.info("Product with id {} successfully deleted", id);
    }

    @Override
    public Optional<ProductModel> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Optional<ProductModel> findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<ProductModel> findAll() {
        return productRepository.findAll();
    }
}
