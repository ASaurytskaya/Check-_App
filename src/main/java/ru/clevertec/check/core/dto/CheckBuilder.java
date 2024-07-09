package ru.clevertec.check.core.dto;

import ru.clevertec.check.exception.NotEnoughMoneyException;

import java.util.ArrayList;
import java.util.List;

public class CheckBuilder {
    private DiscountCard discountCard;
    private DebitCard debitCard;
    private List<CartItem> items = new ArrayList<>();
    private double totalPrice = 0;
    private double totalDiscount = 0;

    public CheckBuilder setDiscountCard(DiscountCard discountCard) {
        this.discountCard = discountCard;
        return this;
    }

    public CheckBuilder setDebitCard(DebitCard debitCard) {
        this.debitCard = debitCard;
        return this;
    }

    public CheckBuilder addCartItem(CartItem item) {
        items.add(item);
        totalPrice += item.getTotalPrice();
        totalDiscount += item.getDiscount();
        return this;
    }

    public Check build() {
        double totalWithDiscount = totalPrice - totalDiscount;

        if(totalWithDiscount > debitCard.balance()) {
            throw new NotEnoughMoneyException();
        }

        return new Check(discountCard, debitCard, items, totalPrice, totalDiscount, totalWithDiscount);
    }
}
