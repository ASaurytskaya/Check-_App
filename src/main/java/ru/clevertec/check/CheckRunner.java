package ru.clevertec.check;

import ru.clevertec.check.core.dto.Check;
import ru.clevertec.check.dao.factory.DaoFactory;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.service.api.ICheckService;
import ru.clevertec.check.service.implementation.CardService;
import ru.clevertec.check.service.implementation.CheckService;
import ru.clevertec.check.service.implementation.ProductService;
import ru.clevertec.check.util.ArgumentParser;
import ru.clevertec.check.util.CsvWriter;


public class CheckRunner {
    private static final String DISCOUNT_CARDS_FILENAME = "./src/main/resources/discountCards.csv";
    private static final String PRODUCTS_FILENAME = "./src/main/resources/products.csv";
    private static final String RESULT_FILENAME = "./src/main/resources/result.csv";
    private static final String ERROR_MESSAGE = "ERROR\nINTERNAL SERVER ERROR\n";

    public static void main(String[] args) {
        CsvWriter writer = new CsvWriter();
        try {
            ArgumentParser.ParsedArguments parsedArguments = ArgumentParser.parseArguments(args);
            ICheckService checkService = new CheckService(
                    new ProductService(DaoFactory.createProductFromFileDao(PRODUCTS_FILENAME)),
                    new CardService(DaoFactory.createCardFromFileDao(DISCOUNT_CARDS_FILENAME)));
            Check check = checkService.createCheck(parsedArguments.products(), parsedArguments.discountCardNumber(), parsedArguments.balanceDebitCard());
            String result = checkService.checkToText(check);

            System.out.println(result);
            writer.writeToCsv(result, RESULT_FILENAME);
        } catch (BadRequestException | NotEnoughMoneyException e) {
            System.out.println(e.getMessage());
            writer.writeToCsv(e.getMessage(), RESULT_FILENAME);
        } catch (Exception e) {
            System.out.println(ERROR_MESSAGE + e.getMessage());
            writer.writeToCsv(ERROR_MESSAGE, RESULT_FILENAME);
        }
    }

}
