package ru.clevertec.check.service.implementation;


import ru.clevertec.check.core.dto.Product;
import ru.clevertec.check.dao.api.IProductDao;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.service.api.IProductService;

public class ProductService implements IProductService {
    private final IProductDao productDao;
    private static final String MESSAGE_NO_SUCH_PRODUCT = "Запрашиваемый product не найден, id - ";

    public ProductService(IProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public Product getProductById(int id) {
        Product product = productDao.getProductById(id);
        if(product == null) {
            throw new BadRequestException(MESSAGE_NO_SUCH_PRODUCT + id);
        }
        return product;
    }
}
