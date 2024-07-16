package by.fin.example.repositories;

import by.fin.example.entities.PhotoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<PhotoModel, Long> {
    Optional<PhotoModel> findByName(String name);
}
