package ru.clevertec.check.service.implementation;

import ru.clevertec.check.core.dto.*;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.service.api.ICardService;
import ru.clevertec.check.service.api.ICheckService;
import ru.clevertec.check.service.api.IProductService;
import ru.clevertec.check.util.ArgumentParser;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CheckService implements ICheckService {
    private final IProductService productService;
    private final ICardService cardService;

    private static final String MESSAGE_NOT_ENOUGH_PRODUCT = "На складе недостаточно товара с id - ";

    public CheckService(IProductService productService, ICardService cardService) {
        this.productService = productService;
        this.cardService = cardService;
    }

    @Override
    public Check createCheck(String[] args) {
        ArgumentParser.ParsedArguments parsedArgs = ArgumentParser.parseArguments(args);
        List<ProductRequest> productRequestList = parsedArgs.products();
        int discountCardNumber = parsedArgs.discountCardNumber();
        DebitCard debitCard = parsedArgs.balanceDebitCard();
        return createCheck(productRequestList,discountCardNumber,debitCard);
    }

    public Check createCheck(List<ProductRequest> productRequestList, int discountCardNumber, DebitCard debitCard) {
        CheckBuilder checkBuilder = new CheckBuilder();
        checkBuilder.setDebitCard(debitCard);

        DiscountCard card = cardService.getCardByNumber(discountCardNumber);
        checkBuilder.setDiscountCard(card);

        for(ProductRequest pr : productRequestList) {
            Product product = productService.getProductById(pr.getId());
            if(product.qntInStock() < pr.getQuantity()) {
                throw new BadRequestException(MESSAGE_NOT_ENOUGH_PRODUCT + pr.getId());
            }
            CartItem item = new CartItem(product, pr.getQuantity(), card.discountAmount());
            checkBuilder.addCartItem(item);
        }

        return checkBuilder.build();
    }

    @Override
    public String checkToText(Check check) {
        StringBuilder builder = new StringBuilder();
        builder.append("Date;Time\n").
                append(check.dateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm:ss"))).
                append("\n\nQTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n");
        for (CartItem item : check.items()) {
            builder.append(item.getQuantity()).append(';')
                    .append(item.getProduct().description()).append(';')
                    .append(String.format("%.2f", item.getProduct().price())).append('$').append(';')
                    .append(String.format("%.2f", item.getDiscount())).append('$').append(';')
                    .append(String.format("%.2f", item.getTotalPrice())).append('$').append('\n');
        }

        if (check.discountCard() != null) {
            builder.append("\nDISCOUNT CARD;DISCOUNT PERCENTAGE\n")
                    .append(check.discountCard().number()).append(';')
                    .append(check.discountCard().discountAmount()).append('%');
        }

        builder.append("\n\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n")
                .append(String.format("%.2f", check.totalPrice())).append('$').append(';')
                .append(String.format("%.2f", check.totalDiscount())).append('$').append(';')
                .append(String.format("%.2f", check.totalWithDiscount())).append('$');

        return builder.toString();
    }
}
