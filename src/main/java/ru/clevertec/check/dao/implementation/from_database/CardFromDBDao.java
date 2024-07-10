package ru.clevertec.check.dao.implementation.from_database;

import ru.clevertec.check.core.dto.DBSettings;
import ru.clevertec.check.core.dto.DiscountCard;
import ru.clevertec.check.dao.api.ICardDao;
import ru.clevertec.check.exception.InternalServerErrorException;

import java.sql.*;

public class CardFromDBDao implements ICardDao {
    private final String url;
    private final String username;
    private final String password;

    public CardFromDBDao(DBSettings dbSettings) {
        this.url = dbSettings.url();
        this.username = dbSettings.username();
        this.password = dbSettings.password();
    }

    @Override
    public DiscountCard getCardByNumber(int number) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT * FROM discount_card WHERE number = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, number);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new DiscountCard(
                        rs.getInt("id"),
                        rs.getInt("number"),
                        rs.getShort("discount_amount")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
