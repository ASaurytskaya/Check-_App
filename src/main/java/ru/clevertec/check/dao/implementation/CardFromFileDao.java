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

    private final String filename;
    private Map<Integer, DiscountCard> cards;

    public CardFromFileDao(String filename) {
        this.filename = filename;
    }

    @Override
    public DiscountCard getCardByNumber(int number) {
        if(cards == null) {
            readCardsFromCsv(filename);
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
