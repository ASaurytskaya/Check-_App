package ru.clevertec.check.core.dto;

import java.time.LocalDateTime;
import java.util.List;

public record Check(
        LocalDateTime dateTime,
        DiscountCard discountCard,
        DebitCard debitCard,
        List<CartItem> items,
        double totalPrice,
        double totalDiscount,
        double totalWithDiscount
) {
    public Check(DiscountCard discountCard, DebitCard debitCard, List<CartItem> items, double totalPrice, double totalDiscount, double totalWithDiscount) {
        this(LocalDateTime.now(), discountCard, debitCard, items, totalPrice, totalDiscount, totalWithDiscount);
    }
}
