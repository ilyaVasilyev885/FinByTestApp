package by.fin.example.mappers;

import by.fin.example.dto.ProductDTO;
import by.fin.example.entities.FeatureModel;
import by.fin.example.entities.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", uses = PhotoMapper.class)
public interface ProductMapper {
    @Mapping(source = "category.name", target = "category")
    @Mapping(source = "product", target = "features", qualifiedByName = "featuresToDTO")
    ProductDTO toDTO(ProductModel product);

    @Mapping(source = "category", target = "category.name")
    @Mapping(source = "features", target = "features", qualifiedByName = "featuresToModel")
    ProductModel toModel(ProductDTO productDTO);

    @Named("featuresToDTO")
    default List<String> getFeaturesFromModel(ProductModel source) {
        return Optional.ofNullable(source.getFeatures()).orElse(Collections.emptyList())
                .stream()
                .map(FeatureModel::getName)
                .toList();
    }

    @Named("featuresToModel")
    default List<FeatureModel> getFeaturesFromDTO(List<String> source) {
        return Optional.ofNullable(source).orElse(Collections.emptyList())
                .stream()
                .map(feature -> FeatureModel.builder().name(feature).build())
                .toList();
    }
}
