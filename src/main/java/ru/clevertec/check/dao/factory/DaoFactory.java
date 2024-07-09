package ru.clevertec.check.dao.factory;

import ru.clevertec.check.dao.api.ICardDao;
import ru.clevertec.check.dao.api.IProductDao;
import ru.clevertec.check.dao.implementation.CardFromFileDao;
import ru.clevertec.check.dao.implementation.ProductFromFileDao;

public class DaoFactory {
    public static IProductDao createProductFromFileDao(String filename) {
        return new ProductFromFileDao(filename);
    }

    public static ICardDao createCardFromFileDao(String filename) {
        return new CardFromFileDao(filename);
    }
}
