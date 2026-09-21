package org.mrp.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class User {
    private final UUID userId;
    @NonNull
    private String userName;
    @NonNull
    private String userPw;
    private int avgStars;
    private List<UUID> ratingsIds;
    private List<UUID> ratingLikeIds;
    private List<UUID> favMedia;

    public User(String userName, String userPw) {
        this.userId = UUID.randomUUID();
        this.userName = userName;
        this.userPw = userPw;
        this.avgStars = 0;
        this.favMedia = new ArrayList<>();
        this.ratingsIds = new ArrayList<>();
        this.ratingLikeIds = new ArrayList<>();
    }
}
