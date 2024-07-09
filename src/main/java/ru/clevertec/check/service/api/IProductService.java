package ru.clevertec.check.service.api;


import ru.clevertec.check.core.dto.Product;

public interface IProductService {
    Product getProductById(int id);
}
