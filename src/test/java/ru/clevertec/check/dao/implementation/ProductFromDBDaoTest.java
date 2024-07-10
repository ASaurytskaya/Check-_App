package ru.clevertec.check.dao.implementation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.check.core.dto.DBSettings;
import ru.clevertec.check.core.dto.Product;
import ru.clevertec.check.dao.api.IProductDao;
import ru.clevertec.check.dao.implementation.from_database.ProductFromDBDao;
import ru.clevertec.check.exception.InternalServerErrorException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class ProductFromDBDaoTest {

    private static final String URL = "jdbc:postgresql://localhost:5430/check_test";
    private static final String USER = "test_manager";
    private static final String PASSWORD = "test";

    private IProductDao productDao;

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("""
                        CREATE TABLE IF NOT EXISTS public.product
                        (
                            id bigserial,
                            description character varying(50) NOT NULL,
                            price numeric(1000, 2) NOT NULL,
                            quantity_in_stock integer NOT NULL,
                            wholesale_product boolean NOT NULL,
                            PRIMARY KEY (id)
                        );""");
                stmt.execute("INSERT INTO public.product (description, price, quantity_in_stock, wholesale_product) VALUES ('Product 1', 10.0, 100, false); ");
                stmt.execute("INSERT INTO public.product (description, price, quantity_in_stock, wholesale_product) VALUES ('Product 2', 20.0, 200, true);");
            }
        }

        DBSettings dbSettings = new DBSettings(URL, USER, PASSWORD);
        productDao = new ProductFromDBDao(dbSettings);
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DROP TABLE IF EXISTS public.products");
            }
        }
    }

    @Test
    void testGetProductById_ProductExists_shouldReturnCorrectProduct() {
        Product product = productDao.getProductById(1);

        assertNotNull(product);
        assertEquals(1, product.id());
        assertEquals("Product 1", product.description());
        assertEquals(10.0, product.price());
        assertEquals(100, product.qntInStock());
        assertFalse(product.isWholesale());
    }

    @Test
    void testGetProductById_ProductDoesNotExist_shouldReturnNull() {
        Product product = productDao.getProductById(999);

        assertNull(product);
    }

    @Test
    void testGetProductById_SQLException_shouldThrowInternalServerErrorException() {
        DBSettings dbSettings = new DBSettings("jdbc:postgresql://localhost:5430/invalid", USER, PASSWORD);
        productDao = new ProductFromDBDao(dbSettings);

        assertThrows(InternalServerErrorException.class, () -> productDao.getProductById(1));
    }

    @Test
    void testGetProductById_SQLException2_shouldThrowInternalServerErrorException() {
        DBSettings dbSettings = new DBSettings(URL, "invalid", PASSWORD);
        productDao = new ProductFromDBDao(dbSettings);

        assertThrows(InternalServerErrorException.class, () -> productDao.getProductById(1));
    }
}

