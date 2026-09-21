package org.mrp.modal;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class Rating {
    private final UUID ratingId;
    private final UUID creatorId;
    private final UUID mediaId;
    private final int starRating;
    private final Date timestamp;
    private int likes;
    private List<UUID> likesIds;
    private int confirmedFlag;
    private String comment;

    public Rating(UUID creatorId, UUID mediaId, int starRating, String comment) {
        this.timestamp = new Date();
        this.ratingId = UUID.randomUUID();
        this.creatorId = creatorId;
        this.mediaId = mediaId;
        this.starRating = starRating;
        this.likes = 0;
        this.likesIds = new ArrayList<>();
        this.confirmedFlag = 0;
        this.comment = comment;
    }
}
