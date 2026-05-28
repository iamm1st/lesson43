package lesson46.controller;

import lesson43.controller.ProductController;
import lesson43.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest {

    private ProductController productController;

    @BeforeEach
    void setUp() {
        productController = new ProductController();
        productController.init();
    }

    @Test
    void getAllProductsShouldReturnInitProducts() {
        List<Product> products = productController.getAllProducts();

        assertEquals(3, products.size());
    }

    @Test
    void getProductByIdShouldReturnProductWhenProductExists() {
        ResponseEntity<?> response = productController.getProductById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Product.class, response.getBody());

        Product product = (Product) response.getBody();

        assertEquals(1L, product.getId());
        assertEquals("Laptop", product.getName());
    }

    @Test
    void getProductByIdShouldReturnNotFoundWhenProductDoesntExist() {
        ResponseEntity<?> response = productController.getProductById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createProductShouldAddNewProduct() {
        Product newProduct = new Product(null, "Keyboard", "Accessories", 80.0, 15);

        ResponseEntity<?> response = productController.createProduct(newProduct);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Product.class, response.getBody());

        Product createdProduct = (Product) response.getBody();

        assertNotNull(createdProduct.getId());
        assertEquals("Keyboard", createdProduct.getName());
        assertEquals(4, productController.getAllProducts().size());
    }

    @Test
    void updateProductShouldUpdateProductWhenProductExists() {
        Product updatedProduct = new Product(null, "Gaming Laptop", "Electronics", 3200.0, 3);

        ResponseEntity<?> response = productController.updateProduct(1L, updatedProduct);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Product.class, response.getBody());

        Product product = (Product) response.getBody();

        assertEquals(1L, product.getId());
        assertEquals("Gaming Laptop", product.getName());
        assertEquals(3200.0, product.getPrice());
        assertEquals(3, product.getQuantity());
    }

    @Test
    void updateProductShouldReturnNotFoundWhenProductDoesntExist() {
        Product updatedProduct = new Product(null, "Tablet", "Electronics", 1000.0, 2);

        ResponseEntity<?> response = productController.updateProduct(999L, updatedProduct);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteProductShouldDeleteProductWhenProductExists() {
        ResponseEntity<?> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(2, productController.getAllProducts().size());
    }

    @Test
    void deleteProductShouldReturnNotFoundWhenProductDoesntExist() {
        ResponseEntity<?> response = productController.deleteProduct(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}