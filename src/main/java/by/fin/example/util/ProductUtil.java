package by.fin.example.util;

import by.fin.example.entities.FeatureModel;
import by.fin.example.entities.ProductModel;

import java.util.stream.Collectors;

public class ProductUtil {

    public static void updateProductDescription(ProductModel product) {
        StringBuilder description = new StringBuilder();

        description.append(product.getName()).append(" ").append(product.getBrand());
        if (product.getColor() != null) {
            description.append(", color: ").append(product.getColor());
        }

        if (product.getModel() != null) {
            description.append(", model: ").append(product.getModel());
        }

        if (product.getFeatures() != null) {
            description.append(". Features: ");
            String features = product.getFeatures().stream()
                    .map(FeatureModel::getName)
                    .collect(Collectors.joining(", "));
            description.append(features);
        }

        product.setDescription(description.toString());
    }
}
