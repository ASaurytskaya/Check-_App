package ru.clevertec.check.dao.implementation;

import ru.clevertec.check.core.dto.DiscountCard;
import ru.clevertec.check.dao.api.ICardDao;
import ru.clevertec.check.exception.InternalServerErrorException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CardFromFileDao implements ICardDao {
    private Map<Integer, DiscountCard> cards;

    private static final String DISCOUNT_CARDS_FILENAME = "./src/main/resources/discountCards.csv";

    @Override
    public DiscountCard getCardByNumber(int number) {
        DiscountCard card;
        if(cards == null) {
            readCardsFromCsv(DISCOUNT_CARDS_FILENAME);
        }
        return cards.get(number);
    }

    private void readCardsFromCsv(String fileName) {
        Map<Integer, DiscountCard> cards = new HashMap<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                int id = Integer.parseInt(values[0]);
                int number = Integer.parseInt(values[1]);
                short discount = Short.parseShort(values[2]);
                cards.put(number, new DiscountCard(id, number, discount));
            }
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        this.cards = cards;
    }
}
