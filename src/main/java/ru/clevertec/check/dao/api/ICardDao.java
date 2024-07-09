package ru.clevertec.check.dao.api;

import ru.clevertec.check.core.dto.DiscountCard;

public interface ICardDao {
    DiscountCard getCardByNumber(int number);
}
