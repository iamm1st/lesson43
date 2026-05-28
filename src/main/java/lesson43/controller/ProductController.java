package lesson43.controller;

import lesson43.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/app/products")
@Tag(name = "Products", description = "Operations for managing products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private Long currentId = 1L;

    @PostConstruct
    public void init() {
        Product product1 = new Product(currentId++, "Laptop", "Electronics", 2500.0, 5);
        Product product2 = new Product(currentId++, "Phone", "Electronics", 1200.0, 10);
        Product product3 = new Product(currentId++, "Mouse", "Accessories", 45.0, 25);

        products.put(product1.getId(), product1);
        products.put(product2.getId(), product2);
        products.put(product3.getId(), product3);

        log.info("Initial products were added. Products count: {}", products.size());
    }

    @Operation(summary = "Get all products", description = "Returns a list of all products")
    @GetMapping
    public List<Product> getAllProducts() {
        log.info("Request to get all products");
        log.debug("Current products count: {}", products.size());

        return new ArrayList<>(products.values());
    }

    @Operation(summary = "Get product by ID", description = "Returns one product by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@Parameter(description = "Product ID", example = "1") @PathVariable Long id) {
        log.info("Request to get product by id: {}", id);
        Product product = products.get(id);

        if (product == null) {
            log.warn("Product with id {} wasn't found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with id " + id + " wasn't found");
        }

        log.debug("Product was found: id={}, name={}", product.getId(), product.getName());
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Create product", description = "Creates a new product. Product data is sent in JSON format")
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product) {
        log.info("Request to create a new product");
        log.debug("Product data before saving: name={}, category={}, price={}, quantity={}",
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getQuantity());

        product.setId(currentId++);
        products.put(product.getId(), product);

        log.info("Product was created successfully. Product id: {}", product.getId());
        log.debug("Products count after creating: {}", products.size());

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @Operation(summary = "Update product", description = "Updates an existing product by its ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@Parameter(description = "Product ID", example = "1") @PathVariable Long id, @Valid @RequestBody Product updatedProduct) {
        log.info("Request to update product with id: {}", id);
        Product existingProduct = products.get(id);

        if (existingProduct == null) {
            log.warn("Product with id {} wasn't found for update", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with id " + id + " wasn't found");
        }

        log.debug("Old product data: name={}, category={}, price={}, quantity={}",
                existingProduct.getName(),
                existingProduct.getCategory(),
                existingProduct.getPrice(),
                existingProduct.getQuantity());

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());

        products.put(id, existingProduct);

        log.info("Product with id {} was updated successfully", id);
        log.debug("New product data: name={}, category={}, price={}, quantity={}",
                existingProduct.getName(),
                existingProduct.getCategory(),
                existingProduct.getPrice(),
                existingProduct.getQuantity());

        return ResponseEntity.ok(existingProduct);
    }

    @Operation(summary = "Delete product", description = "Deletes a product by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@Parameter(description = "Product ID", example = "1") @PathVariable Long id) {
        log.info("Request to delete product with id: {}", id);
        Product product = products.remove(id);

        if (product == null) {
            log.warn("Product with id {} wasn't found for delete", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with id " + id + " wasn't found");
        }

        log.info("Product with id {} was deleted successfully", id);
        log.debug("Products count after deleting: {}", products.size());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}