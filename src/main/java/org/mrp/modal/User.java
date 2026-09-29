package org.mrp.modal;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class User {
    private final UUID userId;
    @NonNull
    private String userName; // 64 max len
    @NonNull
    private String userPw;// 32 bytes is a sha256
    private int avgStars;
    private List<UUID> ratingsIds;
    private List<UUID> ratingLikeIds;
    private List<UUID> favMedia;

    public User(@org.jspecify.annotations.NonNull String userName, @org.jspecify.annotations.NonNull String userPw) {
        this.userId = UUID.randomUUID();
        this.userName = userName;
        this.userPw = userPw;
        this.avgStars = 0;
        this.favMedia = new ArrayList<>();
        this.ratingsIds = new ArrayList<>();
        this.ratingLikeIds = new ArrayList<>();
    }
}
