package lesson46.controller;

import lesson43.controller.FileUploadController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class FileUploadControllerTest {

    private FileUploadController fileUploadController;

    @BeforeEach
    void setUp() throws Exception {
        fileUploadController = new FileUploadController();
        fileUploadController.createUploadDirectory();
    }

    @Test
    void uploadFileShouldReturnOk() {
        MockMultipartFile file = new MockMultipartFile("file", "test-file.txt", "text/plain", "Test file content".getBytes());

        ResponseEntity<?> response = fileUploadController.uploadFile(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Files.exists(Path.of("uploads", "test-file.txt")));
    }

    @Test
    void uploadFileShouldReturnBadRequestWhenFileIsEmpty() {
        MockMultipartFile file = new MockMultipartFile("file", "empty-file.txt", "text/plain", new byte[0]);

        ResponseEntity<?> response = fileUploadController.uploadFile(file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void uploadFileShouldReturnBadRequestWhenFileNameIsInvalid() {
        MockMultipartFile file = new MockMultipartFile("file", "../bad-file.txt", "text/plain", "Bad file info (content)".getBytes());

        ResponseEntity<?> response = fileUploadController.uploadFile(file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}