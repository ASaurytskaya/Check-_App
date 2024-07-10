package ru.clevertec.check.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import ru.clevertec.check.core.dto.*;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.service.api.ICardService;
import ru.clevertec.check.service.api.IProductService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CheckServiceTest {
    @Mock
    private IProductService productServiceMock;

    @Mock
    private ICardService cardServiceMock;

    @InjectMocks
    private CheckService checkService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCheck_Success_shouldReturnCorrectCheck() {
        List<ProductRequest> productRequests = new ArrayList<>();
        productRequests.add(new ProductRequest(1, 10));
        productRequests.add(new ProductRequest(2, 5));

        int discountCardNumber = 1111;
        DebitCard debitCard = new DebitCard(1000);

        Product product1 = new Product(1, "Product 1", 10.0, 100, false);
        when(productServiceMock.getProductById(1)).thenReturn(product1);

        Product product2 = new Product(2, "Product 2", 15.0, 15, true);
        when(productServiceMock.getProductById(2)).thenReturn(product2);

        DiscountCard discountCard = new DiscountCard(1, 1111, (short) 5);
        when(cardServiceMock.getCardByNumber(discountCardNumber)).thenReturn(discountCard);

        Check check = checkService.createCheck(productRequests, discountCardNumber, debitCard);

        assertNotNull(check);
        assertEquals(debitCard, check.debitCard());
        assertEquals(discountCard, check.discountCard());
        assertEquals(2, check.items().size());
        assertEquals(175, check.totalPrice());
        assertEquals(12.5, check.totalDiscount());
        assertEquals(162.5, check.totalWithDiscount());
    }

    @Test
    void testCreateCheck_ZeroDiscountCardNumber_shouldReturnCorrectCheckWithoutDiscountCard() {
        List<ProductRequest> productRequests = new ArrayList<>();
        productRequests.add(new ProductRequest(1, 10));
        productRequests.add(new ProductRequest(2, 5));

        int discountCardNumber = 0;
        DebitCard debitCard = new DebitCard(1000);

        Product product1 = new Product(1, "Product 1", 10.0, 100, false);
        when(productServiceMock.getProductById(1)).thenReturn(product1);

        Product product2 = new Product(2, "Product 2", 15.0, 15, false);
        when(productServiceMock.getProductById(2)).thenReturn(product2);

        DiscountCard discountCard = new DiscountCard(0, 0, (short) 0);
        when(cardServiceMock.getCardByNumber(discountCardNumber)).thenReturn(discountCard);

        Check check = checkService.createCheck(productRequests, discountCardNumber, debitCard);

        assertNotNull(check);
        assertEquals(debitCard, check.debitCard());
        assertEquals(discountCard, check.discountCard());
        assertEquals(2, check.items().size());
        assertEquals(175, check.totalPrice());
        assertEquals(0, check.totalDiscount());
        assertEquals(175, check.totalWithDiscount());
    }

    @Test
    void testCreateCheck_NotEnoughMoney_shouldThrowException() {
        List<ProductRequest> productRequests = new ArrayList<>();
        productRequests.add(new ProductRequest(1, 3));
        productRequests.add(new ProductRequest(2, 2));

        int discountCardNumber = 1111;
        DebitCard debitCard = new DebitCard(10);

        Product product1 = new Product(1, "Product 1", 10.0, 10, false);
        when(productServiceMock.getProductById(1)).thenReturn(product1);

        Product product2 = new Product(2, "Product 2", 15.0, 5, true);
        when(productServiceMock.getProductById(2)).thenReturn(product2);

        DiscountCard discountCard = new DiscountCard(1, 1111, (short) 5);
        when(cardServiceMock.getCardByNumber(discountCardNumber)).thenReturn(discountCard);

        assertThrows(NotEnoughMoneyException.class, () -> checkService.createCheck(productRequests, discountCardNumber, debitCard));
    }

    @Test
    void testCreateCheck_NotEnoughQnt_shouldThrowException() {
        List<ProductRequest> productRequests = new ArrayList<>();
        productRequests.add(new ProductRequest(1, 3));

        int discountCardNumber = 1111;
        DebitCard debitCard = new DebitCard(10);

        Product product1 = new Product(1, "Product 1", 10.0, 2, false);
        when(productServiceMock.getProductById(1)).thenReturn(product1);

        DiscountCard discountCard = new DiscountCard(1, 1111, (short) 5);
        when(cardServiceMock.getCardByNumber(discountCardNumber)).thenReturn(discountCard);

        assertThrows(BadRequestException.class, () -> checkService.createCheck(productRequests, discountCardNumber, debitCard));
    }

    @Test
    void testCreateCheck_ProductServiceException_shouldPropagateException() {
        List<ProductRequest> productRequests = new ArrayList<>();
        productRequests.add(new ProductRequest(1, 10));

        int discountCardNumber = 1111;
        DebitCard debitCard = new DebitCard(1000);

        when(productServiceMock.getProductById(1)).thenThrow(new RuntimeException("Mock exception"));

        assertThrows(RuntimeException.class, () -> checkService.createCheck(productRequests, discountCardNumber, debitCard));
    }

    @Test
    void testCreateCheck_CardServiceException_shouldPropagateException() {
        List<ProductRequest> productRequests = new ArrayList<>();
        productRequests.add(new ProductRequest(1, 10));

        int discountCardNumber = 1111;
        DebitCard debitCard = new DebitCard(1000);

        when(productServiceMock.getProductById(1)).thenReturn(new Product(1, "Product 1", 10.0, 100, false));
        when(cardServiceMock.getCardByNumber(discountCardNumber)).thenThrow(new RuntimeException("Mock exception"));

        assertThrows(RuntimeException.class, () -> checkService.createCheck(productRequests, discountCardNumber, debitCard));
    }
}