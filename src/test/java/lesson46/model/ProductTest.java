package lesson46.model;

import lesson43.model.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void constructorShouldCreateProductCorrectly() {
        Product product = new Product(1L, "Laptop", "Electronics", 2500.0, 5);

        assertEquals(1L, product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals("Electronics", product.getCategory());
        assertEquals(2500.0, product.getPrice());
        assertEquals(5, product.getQuantity());
    }

    @Test
    void settersShouldUpdateProductFields() {
        Product product = new Product();

        product.setId(2L);
        product.setName("Phone");
        product.setCategory("Electronics");
        product.setPrice(1200.0);
        product.setQuantity(10);

        assertEquals(2L, product.getId());
        assertEquals("Phone", product.getName());
        assertEquals("Electronics", product.getCategory());
        assertEquals(1200.0, product.getPrice());
        assertEquals(10, product.getQuantity());
    }
}