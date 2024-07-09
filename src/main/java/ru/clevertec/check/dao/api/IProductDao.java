package ru.clevertec.check.dao.api;

import ru.clevertec.check.core.dto.Product;

public interface IProductDao {
    Product getProductById(int id);
}
