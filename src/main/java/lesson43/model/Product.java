package lesson43.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Product model") // полное описание модели
public class Product {

    @Schema(description = "Product ID", example = "1")
    private Long id;

    @Schema(description = "Product name", example = "Laptop") // описывают отдельные поля
    @NotBlank(message = "Product name must not be empty")
    @Size(min = 2, max = 30, message = "Product name must contain from 2 to 30 characters")
    private String name;

    @Schema(description = "Product category", example = "Electronics")
    @NotBlank(message = "Product category must not be empty")
    @Size(min = 2, max = 30, message = "Product category must contain from 2 to 30 characters")
    private String category;

    @Schema(description = "Product price", example = "2500.0")
    @Positive(message = "Product price must be greater than 0")
    private double price;

    @Schema(description = "Product quantity", example = "5")
    @Min(value = 0, message = "Product quantity must be 0 or greater")
    private int quantity;

    public Product() { }

    public Product(Long id, String name, String category, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}