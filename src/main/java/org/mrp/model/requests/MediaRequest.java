package org.mrp.model.requests;

import lombok.Data;
import org.json.JSONObject;
import org.mrp.model.enums.Genres;
import org.mrp.model.enums.MediaType;
import org.mrp.model.exceptions.ValidationException;

import java.util.Set;

@Data
public final class MediaRequest {

    String title;
    String description;
    MediaType mediaType;
    int releaseYear;
    Genres genre;
    int minAge;
    private static final Set<String> ALLOWED_FIELDS = Set.of("title", "description", "mediaType", "releaseYear", "genre", "minAge");


    public MediaRequest(JSONObject json) {
        RequestFields.checkAllowed(json, ALLOWED_FIELDS);
        this.title = RequestFields.requireString(json,"title");
        if (title.isBlank() || title.length() > 64)
            throw new ValidationException("title must be 1-64 characters");

        if (json.has("description") && !json.isNull("description")) {
            this.description = RequestFields.requireString(json,"description");
            if (description.length() > 256)
                throw new ValidationException("description must be max 256 characters");
        }

        try {
            this.mediaType = MediaType.valueOf(RequestFields.requireString(json,"mediaType").toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("mediaType must be MOVIE, SERIES or GAME");
        }

        try {
            this.genre = Genres.valueOf(RequestFields.requireString(json,"genre").toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("genre must be ACTION, HORROR or THRILLER");
        }

        this.releaseYear = RequestFields.requireInt(json,"releaseYear");
        if (releaseYear < 1 || releaseYear > 9999)
            throw new ValidationException("releaseYear must be 1-9999");

        this.minAge = RequestFields.requireInt(json,"minAge");
        if (minAge < 0 || minAge > 99)
            throw new ValidationException("minAge must be 0-99");
    }
}
