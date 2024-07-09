package ru.clevertec.check.dao.factory;

import ru.clevertec.check.dao.api.ICardDao;
import ru.clevertec.check.dao.api.IProductDao;
import ru.clevertec.check.dao.implementation.CardFromFileDao;
import ru.clevertec.check.dao.implementation.ProductFromFileDao;

public class DaoFactory {
    public static IProductDao createProductFromFileDao() {
        return new ProductFromFileDao();
    }

    public static ICardDao createCardFromFileDao() {
        return new CardFromFileDao();
    }
}
