package by.fin.example.services.impl;

import by.fin.example.services.ImageIntegrationService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class ImageIntegrationServiceImpl implements ImageIntegrationService {

    @Value("${imgur.api}")
    private String imgurApiUrl;

    @Value("${background.api}")
    private String bgApiUrl;

    @Value("${imgur.api.key}")
    private String imgurApiKey;

    @Value("${background.api.key}")
    private String bgApiKey;

    /**
     * Downloads an image from the specified URL and saves it to the local file system.
     *
     * Downloads an image from the provided URL and saves it as {@code outputFileName} in the specified {@code outputDirectory}.
     * If the directory does not exist, it creates the necessary directories. Logs success or failure messages accordingly.
     *
     * @param imageUrl The URL of the image to download.
     * @param outputFileName The name of the file to save the downloaded image as.
     * @param outputDirectory The directory where the image should be saved.
     * @throws IOException if an I/O error occurs during file operations or while downloading the image.
     */
    @SneakyThrows
    @Override
    public void downloadImageFromURL(String imageUrl, String outputFileName, String outputDirectory) {
        Path outputDirPath = Paths.get(outputDirectory);

        try {
            if (Files.notExists(outputDirPath)) {
                Files.createDirectories(outputDirPath);
            }

            try (InputStream in = URI.create(imageUrl).toURL().openStream();
                 FileOutputStream out = new FileOutputStream(outputDirPath.resolve(outputFileName).toFile())) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                log.info("Image downloaded successfully: {}", outputDirPath.resolve(outputFileName).toAbsolutePath());

            } catch (IOException e) {
                log.error("Failed to download the image: {}", e.getMessage(), e);
            }
        } catch (IOException e) {
            log.error("Failed to create directories: {}", e.getMessage(), e);
        }
    }

    /**
     * Deletes the background of an image located at the specified resource path using a remote API.
     *
     * Deletes the background of the image located at {@code resourcePath} using the Background Removal API.
     * It creates an HTTP POST request with the image content and executes it to obtain the modified image.
     *
     * @param resourcePath The path to the image file to be processed.
     * @param outputDirectory The directory where the processed image should be saved.
     * @param outputFileName The name of the file to save the processed image as.
     * @return A File object representing the processed image.
     * @throws RuntimeException if the specified file is not found, or if an error occurs during HTTP request execution.
     */
    @Override
    public File deleteBackground(String resourcePath, String outputDirectory, String outputFileName) {
        File imageFile = new File(resourcePath);
        if (!imageFile.exists()) {
            log.error("File not found in resources: {}", resourcePath);
            throw new RuntimeException();
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             InputStream inputStream = Files.newInputStream(imageFile.toPath())) {
            HttpPost httpPost = createHttpPost(bgApiKey, inputStream);
            return executeHttpRequest(httpClient, httpPost, outputDirectory, outputFileName);

        } catch (IOException e) {
            log.error("Error occurred during photo upload: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Uploads the given image file to Imgur using the Imgur API.
     *
     * Uploads the specified image file to Imgur using HTTP POST request with multipart form data.
     * The method constructs the request with necessary headers and parameters, executes it,
     * and retrieves the URL link of the uploaded image from the API response.
     *
     * @param image The File object representing the image to be uploaded.
     * @return A String containing the URL link of the uploaded image.
     * @throws RuntimeException if an error occurs during the upload process, such as connection errors,
     *                          HTTP request execution errors, or JSON parsing errors.
     */
    @Override
    public String uploadPhoto(File image) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost uploadFile = new HttpPost(imgurApiUrl);
            uploadFile.addHeader("Authorization", "Client-ID " + imgurApiKey);

            HttpEntity httpEntity = MultipartEntityBuilder.create()
                    .addBinaryBody("image", image, ContentType.DEFAULT_BINARY, image.getName())
                    .build();
            uploadFile.setEntity(httpEntity);

            try (CloseableHttpResponse response = httpClient.execute(uploadFile)) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    String responseString = EntityUtils.toString(responseEntity);
                    JSONObject jsonResponse = new JSONObject(responseString);
                    return jsonResponse.getJSONObject("data").getString("link");
                } else {
                    log.error("No response entity found after uploading image");
                    throw new RuntimeException("No response entity found");
                }
            }
        } catch (IOException | JSONException e) {
            log.error("Failed to upload image: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private HttpPost createHttpPost(String apiKey, InputStream inputStream) {
        HttpPost httpPost = new HttpPost(bgApiUrl);
        httpPost.addHeader("X-Api-Key", apiKey);

        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addBinaryBody("image_file", inputStream, ContentType.DEFAULT_BINARY, "file.jpg")
                .addTextBody("size", "auto")
                .build();

        httpPost.setEntity(httpEntity);
        return httpPost;
    }

    private File executeHttpRequest(CloseableHttpClient httpClient, HttpPost httpPost, String outputDirectory, String outputFileName) throws IOException {
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                File file = saveResponseToFile(responseEntity, outputDirectory, outputFileName);
                EntityUtils.consume(responseEntity);
                return file;
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            throw new IOException("Error occurred during creating HTTP request");
        }
    }

    private File saveResponseToFile(HttpEntity responseEntity, String outputDirectory, String outputFileName) throws IOException {
        Path outputDirPath = Paths.get(outputDirectory);
        try {
            if (!Files.exists(outputDirPath)) {
                Files.createDirectories(outputDirPath);
                log.info("Directory created: {}", outputDirPath);
            }
            File outputFile = new File(outputDirPath.toFile(), outputFileName);
            try (InputStream responseStream = responseEntity.getContent();
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = responseStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return outputFile;
        } catch (IOException e) {
            throw new IOException("Error occurred during saving file.");
        }
    }

    @SneakyThrows
    @Override
    public boolean waitForFileAvailability(String filePath, int attempts, int delay) {
        File file = new File(filePath);
        for (int i = 0; i < attempts; i++) {
            if (file.exists()) {
                return true;
            }
            Thread.sleep(delay);
        }
        return false;
    }
}
