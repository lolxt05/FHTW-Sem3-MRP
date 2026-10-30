package org.mrp.model;

import lombok.Data;
import org.mrp.model.enums.Genres;
import org.mrp.model.enums.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Media {
    private final UUID mediaId;
    private String mediaTitle;
    private String mediaDescription;
    private MediaType mediaType;
    private UUID creatorId;
    private int releaseYear;
    private Genres genre;
    private int minAge;
    private List<UUID> ratingIds;
    private List<UUID> favoriteIds;
    private float avgScore;
    private int favoriteCount;

    public Media(String mediaTitle, String mediaDescription, MediaType mediaType, UUID creatorId, int releaseYear, Genres genre, int minAge) {
        this.mediaId = UUID.randomUUID();
        this.mediaTitle = mediaTitle;
        this.mediaDescription = mediaDescription;
        this.mediaType = mediaType;
        this.creatorId = creatorId;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.minAge = minAge;
        this.ratingIds = new ArrayList<>();
        this.favoriteIds = new ArrayList<>();
        this.avgScore = 0;
        this.favoriteCount = 0;
    }
    public Media(UUID mediaId, String mediaTitle, String mediaDescription, MediaType mediaType,
                 UUID creatorId, int releaseYear, Genres genre, int minAge,
                 float avgScore, int favoriteCount) {
        this.mediaId = mediaId;
        this.mediaTitle = mediaTitle;
        this.mediaDescription = mediaDescription;
        this.mediaType = mediaType;
        this.creatorId = creatorId;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.minAge = minAge;
        this.ratingIds = new ArrayList<>();
        this.favoriteIds = new ArrayList<>();
        this.avgScore = avgScore;
        this.favoriteCount = favoriteCount;
    }
}