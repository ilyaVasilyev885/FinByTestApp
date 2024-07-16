package by.fin.example.facades;

import by.fin.example.dto.PhotoDTO;
import by.fin.example.dto.ProductDTO;

public interface ImageIntegrationFacade {

    ProductDTO removeBackground(PhotoDTO photoDTO, ProductDTO productDTO);
}
