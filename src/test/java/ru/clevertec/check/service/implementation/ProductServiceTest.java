package ru.clevertec.check.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import ru.clevertec.check.core.dto.Product;
import ru.clevertec.check.dao.api.IProductDao;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.InternalServerErrorException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class ProductServiceTest {
    @Mock
    private IProductDao productDao;
    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductById_ProductExists_shouldReturnCorrectProduct() {
        int productId = 1;
        String description = "Mock Product";
        double price = 10.0;
        int qntInStock = 100;
        Product mockProduct = new Product(productId, description, price, qntInStock, false);
        when(productDao.getProductById(productId)).thenReturn(mockProduct);

        Product result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals(productId, result.id());
        assertEquals(description, result.description());
        assertEquals(price, result.price());
        assertEquals(qntInStock, result.qntInStock());
        assertFalse(result.isWholesale());
    }

    @Test
    void testGetProductById_ProductDoesNotExist_shouldThrowException() {
        int productId = 999;
        when(productDao.getProductById(productId)).thenReturn(null);

        assertThrows(BadRequestException.class, () -> productService.getProductById(productId));
    }

    @Test
    void getCardByNumber_whenDaoThrowsException_shouldThrowException() {
        when(productDao.getProductById(anyInt())).thenThrow(new InternalServerErrorException("Test exception"));

        assertThrows(InternalServerErrorException.class, () -> productService.getProductById(9));
    }
}