package by.fin.example.facades;

import java.io.Serializable;
import java.util.List;

public interface CRUDFacade<T, ID extends Serializable> {
    T save(T t);
    T update(T t);
    void delete(ID id);
    T findById(ID id);
    List<T> findAll();

}
