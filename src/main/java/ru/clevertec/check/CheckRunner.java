package ru.clevertec.check;

import ru.clevertec.check.core.dto.Check;
import ru.clevertec.check.dao.factory.DaoFactory;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.service.api.ICheckService;
import ru.clevertec.check.service.implementation.CardService;
import ru.clevertec.check.service.implementation.CheckService;
import ru.clevertec.check.service.implementation.ProductService;
import ru.clevertec.check.util.CsvWriter;


public class CheckRunner {
    private static final String DISCOUNT_CARDS_FILENAME = "./src/main/resources/discountCards.csv";
    private static final String PRODUCTS_FILENAME = "./src/main/resources/products.csv";

    public static void main(String[] args) {
        CsvWriter writer = new CsvWriter();
        try {
            ICheckService checkService = new CheckService(
                    new ProductService(DaoFactory.createProductFromFileDao(PRODUCTS_FILENAME)),
                    new CardService(DaoFactory.createCardFromFileDao(DISCOUNT_CARDS_FILENAME)));
            Check check = checkService.createCheck(args);
            String result = checkService.checkToText(check);
            System.out.println(result);
            writer.writeToCsv(result);
        } catch (BadRequestException | NotEnoughMoneyException e) {
            System.out.println(e.getMessage());
            writer.writeToCsv(e.getMessage());
        } catch (Exception e) {
            System.out.println("INTERNAL SERVER ERROR\n" + e.getMessage());
            writer.writeToCsv("INTERNAL SERVER ERROR");
        }
    }

}
