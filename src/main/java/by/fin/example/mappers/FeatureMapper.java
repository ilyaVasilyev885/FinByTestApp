package by.fin.example.mappers;

import by.fin.example.dto.FeatureDTO;
import by.fin.example.entities.FeatureModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface FeatureMapper {

    FeatureDTO toDTO(FeatureModel feature);

    FeatureModel toModel(FeatureDTO featureDTO);
}
