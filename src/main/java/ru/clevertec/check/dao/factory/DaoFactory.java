package ru.clevertec.check.dao.factory;

import ru.clevertec.check.core.dto.DBSettings;
import ru.clevertec.check.dao.api.ICardDao;
import ru.clevertec.check.dao.api.IProductDao;
import ru.clevertec.check.dao.implementation.from_database.CardFromDBDao;
import ru.clevertec.check.dao.implementation.from_database.ProductFromDBDao;

public class DaoFactory {

    public static ICardDao createCardFromDBDao(DBSettings dbSettings) {
        return new CardFromDBDao(dbSettings);
    }

    public  static IProductDao createProductFromDBDao(DBSettings dbSettings) {
        return new ProductFromDBDao(dbSettings);
    }
}
