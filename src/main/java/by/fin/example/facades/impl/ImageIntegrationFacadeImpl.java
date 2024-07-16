package by.fin.example.facades.impl;

import by.fin.example.dto.PhotoDTO;
import by.fin.example.dto.ProductDTO;
import by.fin.example.facades.ImageIntegrationFacade;
import by.fin.example.facades.ProductFacade;
import by.fin.example.services.ImageIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class ImageIntegrationFacadeImpl implements ImageIntegrationFacade {

    private static final String BUILD_IMAGES_WITHOUT_BG = "build/imagesWithoutBG/%s";
    private static final String BUILD_ORIGINAL_IMAGES = "build/originalImages/%s";

    private ProductFacade productFacade;
    private ImageIntegrationService imageIntegrationService;

    @Autowired
    public ImageIntegrationFacadeImpl(ProductFacade productFacade, ImageIntegrationService imageIntegrationService) {
        this.productFacade = productFacade;
        this.imageIntegrationService = imageIntegrationService;
    }

    @Override
    public ProductDTO removeBackground(PhotoDTO photoDTO, ProductDTO productDTO) {
        List<PhotoDTO> photoUrls = new ArrayList<>(Optional.ofNullable(productDTO.getPhotos())
                .orElse(Collections.emptyList()));

        String fileName = photoDTO.getName();
        String outputDirOriginal = String.format(BUILD_ORIGINAL_IMAGES, productDTO.getName());
        String filePath = outputDirOriginal + "/" + fileName;
        String outputDirWithoutBG = String.format(BUILD_IMAGES_WITHOUT_BG, productDTO.getName());
        imageIntegrationService.downloadImageFromURL(photoDTO.getUrl(), fileName, outputDirOriginal);
        boolean exist = imageIntegrationService.waitForFileAvailability(filePath, 10, 500);

        if (exist) {
            File image = imageIntegrationService.deleteBackground(filePath, outputDirWithoutBG, fileName);
            String uploadedUrl = imageIntegrationService.uploadPhoto(image);

            photoUrls.add(PhotoDTO.builder()
                    .name(fileName)
                    .url(uploadedUrl)
                    .build());

            productDTO.setPhotos(photoUrls);
        }
        return productFacade.update(productDTO);
    }
}
