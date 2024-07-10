package ru.clevertec.check.util;

import org.junit.jupiter.api.Test;

import ru.clevertec.check.core.dto.ProductRequest;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.NoPathToSaveFileException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArgumentParserTest {
    @Test
    void parseArguments_withValidArgs_shouldReturnParsedArguments() throws BadRequestException {
        String[] args = {"1-2",
                "2-3",
                "discountCard=1111",
                "balanceDebitCard=100.50",
                "saveToFile=src/main/resources/result.csv",
                "datasource.url=jdbc:postgresql://localhost:5430/check_test",
                "datasource.username=test_manager",
                "datasource.password=test"
        };

        ArgumentParser.ParsedArguments result = ArgumentParser.parseArguments(args);

        List<ProductRequest> products = result.products();
        assertEquals(2, products.size());
        assertEquals(1, products.getFirst().getId());
        assertEquals(2, products.getFirst().getQuantity());
        assertEquals(2, products.getLast().getId());
        assertEquals(3, products.getLast().getQuantity());
        assertEquals(1111, result.discountCardNumber());
        assertEquals(100.50, result.balanceDebitCard().balance());
        assertEquals("jdbc:postgresql://localhost:5430/check_test", result.dbSettings().url());
        assertEquals("test_manager", result.dbSettings().username());
        assertEquals("test", result.dbSettings().password());
    }

    @Test
    void parseArguments_withDuplicateProductIds_shouldAggregateQuantities() throws BadRequestException {
        String[] args = {"1-2",
                "1-3",
                "discountCard=1111",
                "balanceDebitCard=100.50",
                "saveToFile=src/main/resources/result.csv",
                "datasource.url=jdbc:postgresql://localhost:5430/check_test",
                "datasource.username=test_manager",
                "datasource.password=test"
        };
        ArgumentParser.ParsedArguments result = ArgumentParser.parseArguments(args);

        List<ProductRequest> products = result.products();
        assertEquals(1, products.size());
        assertEquals(1, products.getFirst().getId());
        assertEquals(5, products.getFirst().getQuantity());
        assertEquals(1111, result.discountCardNumber());
        assertEquals(100.50, result.balanceDebitCard().balance());
        assertEquals("jdbc:postgresql://localhost:5430/check_test", result.dbSettings().url());
        assertEquals("test_manager", result.dbSettings().username());
        assertEquals("test", result.dbSettings().password());
    }

    @Test
    void parseArguments_withoutDiscountCard_shouldReturnParsedArguments() throws BadRequestException {
        String[] args = {"1-2",
                "balanceDebitCard=100.50",
                "saveToFile=src/main/resources/result.csv",
                "datasource.url=jdbc:postgresql://localhost:5430/check_test",
                "datasource.username=test_manager",
                "datasource.password=test"
        };
        ArgumentParser.ParsedArguments result = ArgumentParser.parseArguments(args);

        List<ProductRequest> products = result.products();
        assertEquals(1, products.size());
        assertEquals(1, products.getFirst().getId());
        assertEquals(2, products.getFirst().getQuantity());
        assertEquals(0, result.discountCardNumber());
        assertEquals(100.50, result.balanceDebitCard().balance());
        assertEquals("jdbc:postgresql://localhost:5430/check_test", result.dbSettings().url());
        assertEquals("test_manager", result.dbSettings().username());
        assertEquals("test", result.dbSettings().password());
    }

    @Test
    void parseArguments_withInvalidDiscountCard_shouldThrowException() {
        String[] args = {"1-2",
                "discountCard=invalid",
                "balanceDebitCard=100.50",
                "saveToFile=src/main/resources/result.csv",
                "datasource.url=jdbc:postgresql://localhost:5430/check_test",
                "datasource.username=test_manager",
                "datasource.password=test"
        };

        assertThrows(BadRequestException.class, () -> ArgumentParser.parseArguments(args));
    }

    @Test
    void parseArguments_withInvalidDiscountCard2_shouldThrowException() {
        String[] args = {"1-2",
                "discountCard=2",
                "balanceDebitCard=100.50",
                "saveToFile=src/main/resources/result.csv",
                "datasource.url=jdbc:postgresql://localhost:5430/check_test",
                "datasource.username=test_manager",
                "datasource.password=test"
        };

        assertThrows(BadRequestException.class, () -> ArgumentParser.parseArguments(args));
    }

    @Test
    void parseArguments_withInvalidBalanceDebitCard_shouldThrowException() {
        String[] args = {"1-2",
                "discountCard=1111",
                "balanceDebitCard=invalid",
                "saveToFile=src/main/resources/result.csv",
                "datasource.url=jdbc:postgresql://localhost:5430/check_test",
                "datasource.username=test_manager",
                "datasource.password=test"
        };

        assertThrows(BadRequestException.class, () -> ArgumentParser.parseArguments(args));
    }

    @Test
    void parseArguments_withInvalidProductFormat_shouldThrowException() {
        String[] args = {"1-2",
                "a-b",
                "discountCard=1111",
                "balanceDebitCard=100",
                "saveToFile=src/main/resources/result.csv",
                "datasource.url=jdbc:postgresql://localhost:5430/check_test",
                "datasource.username=test_manager",
                "datasource.password=test"
        };

        assertThrows(BadRequestException.class, () -> ArgumentParser.parseArguments(args));
    }

    @Test
    void parseArguments_withInvalidProductQnt_shouldThrowException() {
        String[] args = {"1-2",
                "2-0",
                "discountCard=1111",
                "balanceDebitCard=100",
                "saveToFile=src/main/resources/result.csv",
                "datasource.url=jdbc:postgresql://localhost:5430/check_test",
                "datasource.username=test_manager",
                "datasource.password=test"
        };

        assertThrows(BadRequestException.class, () -> ArgumentParser.parseArguments(args));
    }

    @Test
    void parseArguments_withInvalidProductFormat2_shouldThrowException() {
        String[] args = {"1-2",
                "2-3-4",
                "discountCard=1111",
                "balanceDebitCard=100",
                "saveToFile=src/main/resources/result.csv",
                "datasource.url=jdbc:postgresql://localhost:5430/check_test",
                "datasource.username=test_manager",
                "datasource.password=test"
        };

        assertThrows(BadRequestException.class, () -> ArgumentParser.parseArguments(args));
    }

    @Test
    void parseArguments_withMissingProducts_shouldThrowException() {
        String[] args = {
                "discountCard=1111",
                "balanceDebitCard=100",
                "saveToFile=src/main/resources/result.csv",
                "datasource.url=jdbc:postgresql://localhost:5430/check_test",
                "datasource.username=test_manager",
                "datasource.password=test"
        };

        assertThrows(BadRequestException.class, () -> ArgumentParser.parseArguments(args));
    }

    @Test
    void parseArguments_withMissingBalanceDebitCard_shouldThrowException() {
        String[] args = {"1-2",
                "discountCard=1111",
                "saveToFile=src/main/resources/result.csv",
                "datasource.url=jdbc:postgresql://localhost:5430/check_test",
                "datasource.username=test_manager",
                "datasource.password=test"
        };

        assertThrows(BadRequestException.class, () -> ArgumentParser.parseArguments(args));
    }


    @Test
    void parseArguments_withoutSaveToFile_shouldThrowException() {
        String[] args = {"1-2",
                "discountCard=1111",
                "balanceDebitCard=100",
                "datasource.url=jdbc:postgresql://localhost:5430/check_test",
                "datasource.username=test_manager",
                "datasource.password=test"
        };

        assertThrows(NoPathToSaveFileException.class, () -> ArgumentParser.parseArguments(args));
    }

    @Test
    void parseArguments_MissingDatasourceArguments_shouldThrowException() {
        String[] args = {"1-2",
                "discountCard=1111",
                "balanceDebitCard=100",
                "saveToFile=src/main/resources/result.csv",
                "datasource.username=test_manager",
                "datasource.password=test"
        };

        assertThrows(BadRequestException.class, () -> ArgumentParser.parseArguments(args));
    }

    @Test
    void parseArguments_MissingDatasourceArguments2_shouldThrowException() {
        String[] args = {"1-2",
                "discountCard=1111",
                "balanceDebitCard=100",
                "saveToFile=src/main/resources/result.csv",
                "datasource.url=jdbc:postgresql://localhost:5430/check_test",
                "datasource.password=test"
        };

        assertThrows(BadRequestException.class, () -> ArgumentParser.parseArguments(args));
    }

    @Test
    void parseArguments_MissingDatasourceArguments3_shouldThrowException() {
        String[] args = {"1-2",
                "discountCard=1111",
                "balanceDebitCard=100",
                "saveToFile=src/main/resources/result.csv",
                "datasource.url=jdbc:postgresql://localhost:5430/check_test",
                "datasource.username=test_manager"
        };

        assertThrows(BadRequestException.class, () -> ArgumentParser.parseArguments(args));
    }

    @Test
    void parseArguments_MissingDatasourceArguments4_shouldThrowException() {
        String[] args = {"1-2",
                "discountCard=1111",
                "balanceDebitCard=100",
                "saveToFile=src/main/resources/result.csv",
                "datasource=invalid",
                "datasource.username=test_manager",
                "datasource.password=test"
        };

        assertThrows(BadRequestException.class, () -> ArgumentParser.parseArguments(args));
    }
}
