package by.fin.example.controlles;

import by.fin.example.dto.CategoryDTO;
import by.fin.example.facades.CategoryFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static by.fin.example.constants.FinByConstants.*;

@RestController
@RequestMapping(API + CATEGORY)
public class CategoryController {

    private CategoryFacade categoryFacade;

    @Autowired
    public CategoryController(CategoryFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryFacade.findAll();
    }

    @GetMapping(ID)
    public CategoryDTO getCategoryById(@PathVariable Long id) {
        return categoryFacade.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO createCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryFacade.save(categoryDTO);
    }

    /**
     * Updates an existing category or creates a new one if it doesn't exist.
     *
     * @param id The ID of the category to be updated.
     * @param categoryDTO The category data transfer object containing the details of the category.
     * @return A ResponseEntity containing the updated or created CategoryDTO.
     * - If the category with the given ID does not exist, and the category with the given name does not exist, a new category is created and returned with HTTP status 201 (Created).
     * - If the category with the given ID does not exist, but a category with the given name exists, the existing category with the given name is updated and returned with HTTP status 200 (OK).
     * - If the category with the given ID exists, the existing category is updated and returned with HTTP status 200 (OK).
     */
    @PutMapping(ID)
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryDTO categoryDTO) {
        CategoryDTO categoryToUpdateById = categoryFacade.findById(id);
        CategoryDTO categoryToUpdateByName = categoryFacade.findByName(categoryDTO.getName());

        if (categoryToUpdateById == null) {
            if (categoryToUpdateByName == null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(categoryFacade.save(categoryDTO));
            } else {
                categoryDTO.setId(categoryToUpdateByName.getId());
                return ResponseEntity.ok(categoryFacade.update(categoryDTO));
            }
        } else {
            categoryDTO.setId(id);
            return ResponseEntity.ok(categoryFacade.update(categoryDTO));
        }
    }

    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@PathVariable Long id) {
        categoryFacade.delete(id);
    }
}