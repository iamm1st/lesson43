package lesson43;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class ProductRestApp {

    private static final Logger log = LoggerFactory.getLogger(ProductRestApp.class);

    public static void main(String[] args) {
        log.info("Starting Product REST app");
        SpringApplication.run(ProductRestApp.class, args);
        log.info("Product REST app started successfully WOWWOW");
    }
}