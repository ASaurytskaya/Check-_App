package ru.clevertec.check.core.dto;

public record Product(int id, String description, double price, int qntInStock, boolean isWholesale) {
}
