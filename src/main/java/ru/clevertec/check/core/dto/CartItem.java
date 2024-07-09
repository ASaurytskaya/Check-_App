package ru.clevertec.check.core.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CartItem {
    private final Product product;
    private final int quantity;
    private final short discountPercentage;
    private final BigDecimal discount;
    private final BigDecimal totalPrice;

    public CartItem(Product product, int quantity, short discountPercentage) {
        this.product = product;
        this.quantity = quantity;
        this.discountPercentage = discountPercentage;

        this.totalPrice = new BigDecimal(product.price() * quantity).setScale(2, RoundingMode.HALF_UP);
        if(product.isWholesale() && quantity >= 5) {
            this.discount = totalPrice.divide(BigDecimal.valueOf((10)), 2, RoundingMode.HALF_UP);
        } else {
            this.discount = totalPrice.multiply(BigDecimal.valueOf((discountPercentage * 0.01))).setScale(2, RoundingMode.HALF_UP);
        }
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public short getDiscountPercentage() {
        return discountPercentage;
    }

    public double getDiscount() {
        return discount.doubleValue();
    }

    public double getTotalPrice() {
        return totalPrice.doubleValue();
    }
}
