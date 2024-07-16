package by.fin.example.services;

import java.io.File;

public interface ImageIntegrationService {

    void downloadImageFromURL(String imageUrl, String outputFileName, String outputDirectory);
    boolean waitForFileAvailability(String filePath, int attempts, int delay);
    File deleteBackground(String resourcePath, String outputDirectory, String fileName);
    String uploadPhoto(File image);

}
