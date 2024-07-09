package ru.clevertec.check.dao.implementation;

import ru.clevertec.check.core.dto.Product;
import ru.clevertec.check.dao.api.IProductDao;
import ru.clevertec.check.exception.InternalServerErrorException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProductFromFileDao implements IProductDao {

    private final String filename;
    private Map<Integer, Product> products;

    public ProductFromFileDao(String filename) {
        this.filename = filename;
    }

    @Override
    public Product getProductById(int id) {
        if(products == null) {
            readProductsFromCsv(this.filename);
        }

        return products.get(id);
    }

    private void readProductsFromCsv(String fileName) {
        Map<Integer, Product> products = new HashMap<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                int id = Integer.parseInt(values[0]);
                String description = values[1];
                double price = Double.parseDouble(values[2]);
                int qntInStock = Integer.parseInt(values[3]);
                boolean isWholesale = Boolean.parseBoolean(values[4]);
                products.put(id, new Product(id, description, price, qntInStock, isWholesale));
            }
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        this.products = products;
    }
}
