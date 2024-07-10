package ru.clevertec.check;

import ru.clevertec.check.core.dto.Check;
import ru.clevertec.check.dao.factory.DaoFactory;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.InternalServerErrorException;
import ru.clevertec.check.exception.NoPathToSaveFileException;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.service.api.ICheckService;
import ru.clevertec.check.service.implementation.CardService;
import ru.clevertec.check.service.implementation.CheckService;
import ru.clevertec.check.service.implementation.ProductService;
import ru.clevertec.check.util.ArgumentParser;
import ru.clevertec.check.util.CsvWriter;


public class CheckRunner {
    private static final String RESULT_FILENAME = "./src/main/resources/result.csv";

    public static void main(String[] args) {
        CsvWriter writer = new CsvWriter();
        try {
            ArgumentParser.ParsedArguments parsedArguments = ArgumentParser.parseArguments(args);
            String result = run(parsedArguments);

            System.out.println(result);
            writer.writeToCsv(result, parsedArguments.saveToFile());
        } catch (NoPathToSaveFileException e) {
            System.out.println(e.getMessage());
            writer.writeToCsv(e.getMessage(), RESULT_FILENAME);
        } catch (BadRequestException | NotEnoughMoneyException | InternalServerErrorException e) {
            System.out.println(e.getMessage());
            writer.writeToCsv(e.getMessage(), parsePath(args));
        } catch (Exception e) {
            System.out.println("ERROR\nINTERNAL SERVER ERROR\n" + e.getMessage());
            writer.writeToCsv("INTERNAL SERVER ERROR", parsePath(args));
        }
    }

    protected static String run(ArgumentParser.ParsedArguments parsedArguments) {
        ICheckService checkService = new CheckService(
                new ProductService(DaoFactory.createProductFromDBDao(parsedArguments.dbSettings())),
                new CardService(DaoFactory.createCardFromDBDao(parsedArguments.dbSettings())));
        Check check = checkService.createCheck(parsedArguments.products(), parsedArguments.discountCardNumber(), parsedArguments.balanceDebitCard());
        return checkService.checkToText(check);
    }

    protected static String parsePath(String[] args) {
        for(String arg : args) {
            if(arg.startsWith("saveToFile=")) {
                return arg.split("=")[1];
            }
        }
        return null;
    }

}
