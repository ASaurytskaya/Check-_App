package ru.clevertec.check.service.api;


import ru.clevertec.check.core.dto.DiscountCard;

public interface ICardService {
    DiscountCard getCardByNumber(int number);
}
