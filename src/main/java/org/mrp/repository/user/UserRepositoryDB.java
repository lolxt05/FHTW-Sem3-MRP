package org.mrp.repository.user;

import org.mrp.config.DatabaseManager;
import org.mrp.model.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class UserRepositoryDB implements UserRepository {

    private static final Logger LOGGER = Logger.getLogger(UserRepositoryDB.class.getName());

    private static final int MAX_USERNAME_LENGTH = 64;
    private static final Pattern PW_HASH_PATTERN = Pattern.compile("^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$");

    private final Connection conn = Objects.requireNonNull(
            DatabaseManager.getConnection(), "No database connection available");

    private static final String INSERT_SQL =
            "INSERT INTO users (user_id, user_name, user_pw, avg_stars) VALUES (?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM users WHERE user_id = ?";

    private static final String SELECT_BY_NAME_SQL =
            "SELECT * FROM users WHERE user_name = ?";

    private static final String UPDATE_SQL =
            "UPDATE users SET user_name = ?, user_pw = ?, avg_stars = ? WHERE user_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM users WHERE user_id = ?";

    private static final String NAME_COMPLETION_SQL =
            "SELECT user_name FROM users WHERE user_name ILIKE ? ORDER BY user_name LIMIT 10";

    @Override
    public boolean add(UUID key, User value) {
        validate(key, value);
        try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setObject(1, key);
            ps.setString(2, value.getUserName());
            ps.setString(3, value.getUserPw());
            ps.setBigDecimal(4, BigDecimal.valueOf(value.getAvgStars()));
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting user", e);
            return false;
        }
    }

    @Override
    public User get(UUID key) {
        try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setObject(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading user by id", e);
            return null;
        }
    }

    @Override
    public User get(String name) {
        try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_NAME_SQL)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading user by name", e);
            return null;
        }
    }

    @Override
    public boolean update(UUID key, User value) {
        validate(key, value);
        try (PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, value.getUserName());
            ps.setString(2, value.getUserPw());
            ps.setBigDecimal(3, BigDecimal.valueOf(value.getAvgStars()));
            ps.setObject(4, key);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user", e);
            return false;
        }
    }

    @Override
    public boolean remove(UUID key) {
        try (PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setObject(1, key);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing user", e);
            return false;
        }
    }

    @Override
    public List<String> getNameCompletions(String name) {
        List<String> result = new ArrayList<>();
        if (name == null) {
            return result;
        }
        try (PreparedStatement ps = conn.prepareStatement(NAME_COMPLETION_SQL)) {
            ps.setString(1, escapeLike(name) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getString("user_name"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading name completions", e);
        }
        return result;
    }

    private static void validate(UUID key, User user) {
        if (key == null) {
            throw new IllegalArgumentException("User id must not be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        String name = user.getUserName();
        if (name.isBlank()) {
            throw new IllegalArgumentException("Username must not be empty");
        }
        if (name.codePointCount(0, name.length()) > MAX_USERNAME_LENGTH) {
            throw new IllegalArgumentException(
                    "Username too long (max " + MAX_USERNAME_LENGTH + " characters)");
        }

        String pw = user.getUserPw();
        if (!PW_HASH_PATTERN.matcher(pw).matches()) {
            throw new IllegalArgumentException(
                    "Password must be a bcrypt hash");
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getObject("user_id", UUID.class),
                rs.getString("user_name"),
                rs.getString("user_pw"),
                rs.getBigDecimal("avg_stars").intValue()
        );
    }

    private static String escapeLike(String input) {
        return input.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}