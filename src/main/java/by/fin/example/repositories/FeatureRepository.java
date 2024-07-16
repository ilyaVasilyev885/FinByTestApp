package by.fin.example.repositories;

import by.fin.example.entities.FeatureModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeatureRepository extends JpaRepository<FeatureModel, Long> {
    Optional<FeatureModel> findByName(String name);
}
