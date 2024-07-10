package ru.clevertec.check.dao.implementation;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.check.core.dto.DBSettings;
import ru.clevertec.check.core.dto.DiscountCard;
import ru.clevertec.check.dao.api.ICardDao;
import ru.clevertec.check.dao.implementation.from_database.CardFromDBDao;
import ru.clevertec.check.exception.InternalServerErrorException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class CardFromDBDaoTest {
    private static final String URL = "jdbc:postgresql://localhost:5430/check_test";
    private static final String USER = "test_manager";
    private static final String PASSWORD = "test";

    private ICardDao cardDao;

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS public.discount_card (id bigserial, \"number\" integer NOT NULL, discount_amount smallint NOT NULL, PRIMARY KEY (id));");
                stmt.execute("INSERT INTO public.discount_card (number, discount_amount) VALUES (1111, 3);");
                stmt.execute("INSERT INTO public.discount_card (number, discount_amount) VALUES (2222, 3);");
            }
        }

        DBSettings dbSettings = new DBSettings(URL, USER, PASSWORD);
        cardDao = new CardFromDBDao(dbSettings);
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DROP TABLE IF EXISTS public.discount_card");
            }
        }
    }

    @Test
    void testGetCardByNumber_CardExists_shouldReturnCorrectCard() {
        DiscountCard card = cardDao.getCardByNumber(1111);

        assertNotNull(card);
        assertEquals(1, card.id());
        assertEquals(1111, card.number());
        assertEquals(3, card.discountAmount());
    }

    @Test
    void testGetCardByNumber_CardDoesNotExist_shouldReturnNull() {
        DiscountCard card = cardDao.getCardByNumber(9999);

        assertNull(card);
    }

    @Test
    void testGetCardByNumber_SQLException_shouldThrowInternalServerErrorException() {
        DBSettings dbSettings = new DBSettings("jdbc:postgresql://localhost:5430/invalid", USER, PASSWORD);
        cardDao = new CardFromDBDao(dbSettings);

        assertThrows(InternalServerErrorException.class, () -> cardDao.getCardByNumber(1111));
    }

    @Test
    void testGetCardByNumber_SQLException2_shouldThrowInternalServerErrorException() {
        DBSettings dbSettings = new DBSettings(URL, "invalid", PASSWORD);
        cardDao = new CardFromDBDao(dbSettings);

        assertThrows(InternalServerErrorException.class, () -> cardDao.getCardByNumber(1111));
    }

}
