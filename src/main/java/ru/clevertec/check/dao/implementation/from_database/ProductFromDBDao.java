package ru.clevertec.check.dao.implementation.from_database;

import ru.clevertec.check.core.dto.DBSettings;
import ru.clevertec.check.core.dto.Product;
import ru.clevertec.check.dao.api.IProductDao;
import ru.clevertec.check.exception.InternalServerErrorException;

import java.sql.*;

public class ProductFromDBDao implements IProductDao {
    private final String url;
    private final String username;
    private final String password;

    public ProductFromDBDao(DBSettings dbSettings) {
        this.url = dbSettings.url();
        this.username = dbSettings.username();
        this.password = dbSettings.password();
    }

    @Override
    public Product getProductById(int id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT * FROM product WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity_in_stock"),
                        rs.getBoolean("wholesale_product")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

}
