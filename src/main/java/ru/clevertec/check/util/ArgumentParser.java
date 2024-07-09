package ru.clevertec.check.util;

import ru.clevertec.check.core.dto.DebitCard;
import ru.clevertec.check.core.dto.ProductRequest;
import ru.clevertec.check.exception.BadRequestException;

import java.util.ArrayList;
import java.util.List;

public class ArgumentParser {
    public static ParsedArguments parseArguments(String[] args) throws BadRequestException {
        List<ProductRequest> products = new ArrayList<>();
        int discountCardNumber = 0;
        double balanceDebitCard = 0;

        for (String arg : args) {
             if (arg.startsWith("discountCard=")) {
                String number = arg.split("=")[1];
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
            } else {
                 String[] parts = arg.split("-");
                 if (parts.length != 2) {
                     throw new BadRequestException(arg);
                 }
                 try {
                     int id = Integer.parseInt(parts[0]);
                     int quantity = Integer.parseInt(parts[1]);
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

        return new ParsedArguments(products, discountCardNumber,new DebitCard(balanceDebitCard));
    }

    public record ParsedArguments(List<ProductRequest> products, int discountCardNumber, DebitCard balanceDebitCard) {
    }
}
