package org.mrp.repository.media;

import org.mrp.config.DatabaseManager;
import org.mrp.model.enums.Genres;
import org.mrp.model.Media;
import org.mrp.model.enums.MediaType;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

public class MediaRepositoryDB implements MediaRepository {

    private static final Logger LOGGER = Logger.getLogger(MediaRepositoryDB.class.getName());

    private static final int MAX_TITLE_LENGTH = 64;
    private static final int MAX_DESCRIPTION_LENGTH = 256;

    private final Connection conn = Objects.requireNonNull(
            DatabaseManager.getConnection(), "No database connection available");

    private static final String INSERT_SQL = """
            INSERT INTO media (media_id, media_title, media_description, media_type, creator_id,
                               release_year, genre, min_age, avg_score, favorite_count)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM media WHERE media_id = ?";

    private static final String SELECT_BY_TITLE_SQL =
            "SELECT * FROM media WHERE LOWER(media_title) = LOWER(?) LIMIT 1";

    private static final String SELECT_ALL_SQL =
            "SELECT * FROM media ORDER BY media_title";

    private static final String UPDATE_SQL = """
            UPDATE media
               SET media_title = ?, media_description = ?, media_type = ?, creator_id = ?,
                   release_year = ?, genre = ?, min_age = ?, avg_score = ?, favorite_count = ?
             WHERE media_id = ?
            """;

    private static final String DELETE_SQL =
            "DELETE FROM media WHERE media_id = ?";

    private static final String NAME_COMPLETION_SQL =
            "SELECT media_title FROM media WHERE media_title ILIKE ? ORDER BY media_title LIMIT 10";

    @Override
    public boolean add(UUID key, Media value) {
        validate(key, value);
        try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setObject(1, key);
            ps.setString(2, value.getMediaTitle());
            ps.setString(3, value.getMediaDescription());
            ps.setShort(4, (short) value.getMediaType().ordinal());
            ps.setObject(5, value.getCreatorId());
            ps.setInt(6, value.getReleaseYear());
            ps.setShort(7, (short) value.getGenre().ordinal());
            ps.setInt(8, value.getMinAge());
            ps.setBigDecimal(9, toScore(value.getAvgScore()));
            ps.setInt(10, value.getFavoriteCount());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting media", e);
            return false;
        }
    }

    @Override
    public Media get(UUID key) {
        try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setObject(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading media by id", e);
            return null;
        }
    }

    @Override
    public Media get(String name) {
        try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_TITLE_SQL)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading media by title", e);
            return null;
        }
    }

    @Override
    public List<Media> getAll() {
        List<Media> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading all media", e);
        }
        return result;
    }

    @Override
    public boolean update(UUID key, Media value) {
        validate(key, value);
        try (PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, value.getMediaTitle());
            ps.setString(2, value.getMediaDescription());
            ps.setShort(3, (short) value.getMediaType().ordinal());
            ps.setObject(4, value.getCreatorId());
            ps.setInt(5, value.getReleaseYear());
            ps.setShort(6, (short) value.getGenre().ordinal());
            ps.setInt(7, value.getMinAge());
            ps.setBigDecimal(8, toScore(value.getAvgScore()));
            ps.setInt(9, value.getFavoriteCount());
            ps.setObject(10, key);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating media", e);
            return false;
        }
    }

    @Override
    public boolean remove(UUID key) {
        try (PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setObject(1, key);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing media", e);
            return false;
        }
    }

    @Override
    public List<String> getNameCompletion(String name) {
        List<String> result = new ArrayList<>();
        if (name == null) {
            return result;
        }
        try (PreparedStatement ps = conn.prepareStatement(NAME_COMPLETION_SQL)) {
            ps.setString(1, escapeLike(name) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getString("media_title"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading name completions", e);
        }
        return result;
    }


    private static void validate(UUID key, Media media) {
        if (key == null) {
            throw new IllegalArgumentException("Media id must not be null");
        }
        if (media == null) {
            throw new IllegalArgumentException("Media must not be null");
        }

        String title = media.getMediaTitle();
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be empty");
        }
        if (lengthOf(title) > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException(
                    "Title too long (max " + MAX_TITLE_LENGTH + " characters)");
        }

        String description = media.getMediaDescription();
        if (description != null && lengthOf(description) > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException(
                    "Description too long (max " + MAX_DESCRIPTION_LENGTH + " characters)");
        }

        if (media.getMediaType() == null) {
            throw new IllegalArgumentException("Media type must not be null");
        }
        if (media.getGenre() == null) {
            throw new IllegalArgumentException("Genre must not be null");
        }
        if (media.getCreatorId() == null) {
            throw new IllegalArgumentException("Creator id must not be null");
        }
    }

    private static int lengthOf(String s) {
        return s.codePointCount(0, s.length());
    }


    private Media mapRow(ResultSet rs) throws SQLException {
        return new Media(
                rs.getObject("media_id", UUID.class),
                rs.getString("media_title"),
                rs.getString("media_description"),
                MediaType.values()[rs.getShort("media_type")],
                rs.getObject("creator_id", UUID.class),
                rs.getInt("release_year"),
                Genres.values()[rs.getShort("genre")],
                rs.getInt("min_age"),
                rs.getBigDecimal("avg_score").floatValue(),
                rs.getInt("favorite_count")
        );
    }

    private static BigDecimal toScore(float score) {
        return BigDecimal.valueOf(score).setScale(2, RoundingMode.HALF_UP);
    }

    private static String escapeLike(String input) {
        return input.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}