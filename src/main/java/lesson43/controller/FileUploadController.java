package lesson43.controller;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/app/files")
@Tag(name = "Files", description = "Operations for uploading files")
public class FileUploadController {

    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    private final Path uploadDirectory = Paths.get("uploads");

    @PostConstruct
    public void createUploadDirectory() throws IOException {
        if (!Files.exists(uploadDirectory)) {
            Files.createDirectories(uploadDirectory);
            log.info("Upload directory was created: {}", uploadDirectory.toAbsolutePath());
        } else {
            log.debug("Upload directory already exists: {}", uploadDirectory.toAbsolutePath());
        }
    }

    @Operation(summary = "Upload file", description = "Uploads a file to the server and saves it to the uploads dir.")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@Parameter(description = "File to upload") @RequestParam("file") MultipartFile file) {
        log.info("Request to upload file");

        if (file.isEmpty()) {
            log.warn("Upload failed. File is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "File is empty"));
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        log.debug("Original file name: {}", fileName);
        log.debug("File size: {} bytes", file.getSize());

        if (fileName.contains("..")) {
            log.warn("Upload failed. Invalid file name: {}", fileName);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid file name"));
        }

        try {
            Path targetPath = uploadDirectory.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            log.info("File was uploaded successfully: {}", targetPath.toAbsolutePath());

            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "File uploaded successfully", "fileName", fileName));
        } catch (IOException exception) {
            log.error("Couldn't upload file: {}", fileName, exception);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Couldn't upload the file"));
        }
    }
}