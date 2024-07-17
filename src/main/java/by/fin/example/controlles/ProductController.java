package by.fin.example.controlles;

import by.fin.example.dto.PhotoDTO;
import by.fin.example.dto.ProductDTO;
import by.fin.example.facades.ImageIntegrationFacade;
import by.fin.example.facades.ProductFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static by.fin.example.constants.FinByConstants.*;

@RestController
@RequestMapping(API + PRODUCTS)
public class ProductController {


    private ProductFacade productFacade;
    private ImageIntegrationFacade imageIntegrationFacade;

    @Autowired
    public ProductController(ProductFacade productFacade, ImageIntegrationFacade imageIntegrationFacade) {
        this.productFacade = productFacade;
        this.imageIntegrationFacade = imageIntegrationFacade;
    }


    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productFacade.findAll();
    }

    @GetMapping(ID)
    public ProductDTO getProductById(@PathVariable Long id) {
        return productFacade.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody @Valid ProductDTO productDTO) {
        return productFacade.save(productDTO);
    }

    /**
     * Updates an existing product or creates a new one if it doesn't exist.
     *
     * @param id The ID of the product to be updated.
     * @param productDTO The product data transfer object containing the details of the product.
     * @return A ResponseEntity containing the updated or created ProductDTO.
     * - If the product with the given ID does not exist, and the product with the given name does not exist, a new product is created and returned with HTTP status 201 (Created).
     * - If the product with the given name exists, the existing product with the given name is updated and returned with HTTP status 200 (OK).
     * - If the product with the given ID exists, the existing product is updated and returned with HTTP status 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDTO productDTO) {
        ProductDTO productToUpdateById = productFacade.findById(id);
        ProductDTO productToUpdateByName = productFacade.findByName(productDTO.getName());

        if (productToUpdateById == null) {
            if (productToUpdateByName == null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(productFacade.save(productDTO));
            } else {
                productDTO.setId(productToUpdateByName.getId());
                return ResponseEntity.ok(productFacade.update(productDTO));
            }
        } else {
            productDTO.setId(id);
            return ResponseEntity.ok(productFacade.update(productDTO));
        }
    }

    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@PathVariable Long id) {
        productFacade.delete(id);
    }

    /**
     * Endpoint to remove background from product photos.
     *
     * Removes the background of a product's photos identified by the given ID using the provided PhotoDTO.
     * If the product with the specified ID exists, the background removal process is executed and the updated
     * ProductDTO with modified photos is returned in the response body.
     *
     * @param id The ID of the product whose background photos need to be modified.
     * @param photoDTO The PhotoDTO containing details necessary for background removal.
     * @return ResponseEntity containing the updated ProductDTO with modified photos and HTTP status 200 (OK).
     * @throws ResponseStatusException if no product exists with the specified ID, returning HTTP status 400 (BAD_REQUEST).
     *
     * To test this endpoint you need to fetch photo's url with background using GET endpoint. All products have one.
     */
    @PutMapping(REMOVE_BACKGROUND + ID)
    public ResponseEntity<ProductDTO> removeBackground(@PathVariable Long id, @RequestBody PhotoDTO photoDTO) {
        ProductDTO productDTO = productFacade.findById(id);
        if (productDTO != null) {
            return ResponseEntity.status(HttpStatus.OK).body(imageIntegrationFacade.removeBackground(photoDTO, productDTO));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no such product with id: " + id);
        }
    }

    @GetMapping(HIGHEST_RATING_PRODUCT)
    public ProductDTO getHighestRatingProduct() {
        return productFacade.findHighestRatingProduct();
    }

    @GetMapping(MOST_EXPENSIVE_PRODUCT)
    public ProductDTO getMostExpensiveProduct() {
        return productFacade.findMostExpensiveProduct();
    }

    @GetMapping(CHEAPEST_PRODUCT)
    public ProductDTO getCheapestProduct() {
        return productFacade.findCheapestProduct();
    }

}