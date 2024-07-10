package ru.clevertec.check.util;

import ru.clevertec.check.core.dto.DBSettings;
import ru.clevertec.check.core.dto.DebitCard;
import ru.clevertec.check.core.dto.ProductRequest;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.NoPathToSaveFileException;

import java.util.ArrayList;
import java.util.List;

public class ArgumentParser {
    public static ParsedArguments parseArguments(String[] args) throws BadRequestException {
        List<ProductRequest> products = new ArrayList<>();
        int discountCardNumber = 0;
        double balanceDebitCard = 0;
        String saveToFile = null;
        String url = null, username = null, password = null;

        for (String arg : args) {
             if (arg.startsWith("discountCard=")) {
                String number = arg.split("=")[1];
                if(number.length() != 4) {
                    throw new BadRequestException(arg);
                }
                try {
                    discountCardNumber = Integer.parseInt(number);
                }  catch (NumberFormatException e) {
                throw new BadRequestException(arg);
                }
             } else if (arg.startsWith("balanceDebitCard=")) {
                try {
                    balanceDebitCard = Double.parseDouble(arg.split("=")[1]);
                } catch (NumberFormatException e) {
                    throw new BadRequestException(arg);
                }
             } else if(arg.startsWith("saveToFile=")) {
                 String path = arg.split("=")[1];
                 if(!path.endsWith(".csv")) {
                     throw new NoPathToSaveFileException();
                 }
                 saveToFile = path;
             } else if (arg.startsWith("datasource")) {
                 if(arg.startsWith("datasource.url=")) {
                     url = arg.split("=", 2)[1];
                 } else if(arg.startsWith("datasource.username=")) {
                     username = arg.split("=", 2)[1];
                 } else if(arg.startsWith("datasource.password=")) {
                     password = arg.split("=", 2)[1];
                 }
             } else {
                 String[] parts = arg.split("-");
                 if (parts.length != 2) {
                     throw new BadRequestException(arg);
                 }
                 try {
                     int id = Integer.parseInt(parts[0]);
                     int quantity = Integer.parseInt(parts[1]);

                     if(quantity <= 0) throw new BadRequestException("Количество заказанного товара не может быть равно 0.");

                     ProductRequest pr = new ProductRequest(id, quantity);
                     int i = products.indexOf(pr);
                     if(i < 0) {
                         products.add(pr);
                     } else {
                         products.get(i).increaseQuantity(quantity);
                     }

                 } catch (NumberFormatException e) {
                     throw new BadRequestException(arg);
                 }
             }
        }

        if (products.isEmpty()) {
            throw new BadRequestException("Не передан id товара.");
        }

        if(balanceDebitCard == 0) {
            throw new BadRequestException("Не передан баланс карты.");
        }

        if(saveToFile == null) {
            throw new NoPathToSaveFileException();
        }

        if(url == null || username == null || password == null) {
            throw new BadRequestException("Не переданы настройки подключения к БД.");
        }

        return new ParsedArguments(
                products,
                discountCardNumber,
                new DebitCard(balanceDebitCard),
                saveToFile,
                new DBSettings(url, username, password));
    }

    public record ParsedArguments(List<ProductRequest> products, int discountCardNumber, DebitCard balanceDebitCard, String saveToFile, DBSettings dbSettings) {}
}
