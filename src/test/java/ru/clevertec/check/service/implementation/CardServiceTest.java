package ru.clevertec.check.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.core.dto.DiscountCard;
import ru.clevertec.check.dao.api.ICardDao;
import ru.clevertec.check.exception.InternalServerErrorException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class CardServiceTest {
    @Mock
    private ICardDao cardDao;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCardByNumber_whenCardExists_shouldReturnCard() {
        int id = 1;
        int number = 1234;
        short discount = 5;
        DiscountCard mockCard = new DiscountCard(id, number, discount);
        when(cardDao.getCardByNumber(number)).thenReturn(mockCard);

        DiscountCard card = cardService.getCardByNumber(number);

        assertNotNull(card);
        assertEquals(id, card.id());
        assertEquals(number, card.number());
        assertEquals(discount, card.discountAmount());
    }

    @Test
    void getCardByNumber_whenCardDoesNotExistAndNumberIsNotZero_shouldReturnDefaultCard() {
        when(cardDao.getCardByNumber(5678)).thenReturn(null);

        DiscountCard card = cardService.getCardByNumber(5678);

        assertNotNull(card);
        assertEquals(0, card.id());
        assertEquals(5678, card.number());
        assertEquals(2, card.discountAmount());
    }

    @Test
    void getCardByNumber_whenCardDoesNotExistAndNumberIsZero_shouldReturnZeroCard() {
        when(cardDao.getCardByNumber(0)).thenReturn(null);

        DiscountCard card = cardService.getCardByNumber(0);

        assertNotNull(card);
        assertEquals(0, card.id());
        assertEquals(0, card.number());
        assertEquals(0, card.discountAmount());
    }

    @Test
    void getCardByNumber_whenDaoThrowsException_shouldThrowException() {
        when(cardDao.getCardByNumber(anyInt())).thenThrow(new InternalServerErrorException("Test exception"));

        assertThrows(InternalServerErrorException.class, () -> cardService.getCardByNumber(9999));
    }
}