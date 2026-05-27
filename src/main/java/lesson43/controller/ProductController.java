package lesson43.controller;

import lesson43.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/app/products")
public class ProductController {

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
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Product product = products.get(id);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with id " + id + " wasn't found");
        }

        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product) {
        product.setId(currentId++);
        products.put(product.getId(), product);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody Product updatedProduct) {
        Product existingProduct = products.get(id);

        if (existingProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with id " + id + " wasn't found");
        }

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        products.put(id, existingProduct);

        return ResponseEntity.ok(existingProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        Product product = products.remove(id);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with id " + id + " wasn't found");
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}