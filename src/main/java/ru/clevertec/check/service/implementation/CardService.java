package ru.clevertec.check.service.implementation;

import ru.clevertec.check.core.dto.DiscountCard;
import ru.clevertec.check.dao.api.ICardDao;
import ru.clevertec.check.service.api.ICardService;

public class CardService implements ICardService {

    private final ICardDao cardDao;

    public CardService(ICardDao cardDao) {
        this.cardDao = cardDao;
    }

    @Override
    public DiscountCard getCardByNumber(int number) {
        DiscountCard card = cardDao.getCardByNumber(number);
        if(card != null) return  card;

        if (number != 0) return new DiscountCard(0, number, (short) 2);

        return new DiscountCard(0, 0, (short) 0);
    }
}
