package lesson43.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/app/files")
public class FileUploadController {

    private final Path uploadDirectory = Paths.get("uploads");

    @PostConstruct
    public void createUploadDirectory() throws IOException {
        if (!Files.exists(uploadDirectory)) {
            Files.createDirectories(uploadDirectory);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) { // приходит с клиента
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "File is empty"));
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (fileName.contains("..")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid file name"));
        }

        try {
            Path targetPath = uploadDirectory.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "File uploaded successfully", "fileName", fileName));
        }

        catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Couldn't upload the file"));
        }
    }
}