package org.mrp.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mrp.model.HttpResponse;
import org.mrp.model.Media;
import org.mrp.model.Session;
import org.mrp.model.requests.MediaRequest;
import org.mrp.repository.media.MediaManager;

import java.util.UUID;

public class MediaService {
    MediaManager mediaManager = MediaManager.getInstance();
    HttpResponse invalid = HttpResponse.message(500, "Hell nah");
    HttpResponse notFound = HttpResponse.message(404, "media not found");
    HttpResponse forbidden = HttpResponse.message(403, "not your media");
    HttpResponse mediadel = new HttpResponse(204, "");

    public HttpResponse add(Session session, MediaRequest request) {
        Media media = new Media(request.getTitle(), request.getDescription(), request.getMediaType(),
                session.getUserId(), request.getReleaseYear(), request.getGenre(), request.getMinAge());
        if(mediaManager.add(media)){
            return new HttpResponse(201, toJson(media).toString());
        }else {
            return invalid;
        }
    }

    public HttpResponse get(UUID id) {
        Media media = mediaManager.get(id);
        if(media == null){
            return notFound;
        }
        return new HttpResponse(200, toJson(media).toString());
    }

    public HttpResponse getAll() {
        JSONArray array = new JSONArray();
        for (Media media : mediaManager.getAll()) {
            array.put(toJson(media));
        }
        return new HttpResponse(200, array.toString());
    }

    public HttpResponse update(UUID id, Session session, MediaRequest request) {
        Media media = mediaManager.get(id);
        if(media == null){
            return notFound;
        }
        if(!media.getCreatorId().equals(session.getUserId())){
            return forbidden;
        }
        Media updated = new Media(id, request.getTitle(), request.getDescription(), request.getMediaType(),
                media.getCreatorId(), request.getReleaseYear(), request.getGenre(), request.getMinAge(),
                media.getAvgScore(), media.getFavoriteCount());
        if(mediaManager.update(id, updated)){
            return new HttpResponse(200, toJson(updated).toString());
        }else {
            return invalid;
        }
    }

    public HttpResponse remove(UUID id, Session session) {
        Media media = mediaManager.get(id);
        if(media == null){
            return notFound;
        }
        if(!media.getCreatorId().equals(session.getUserId())){
            return forbidden;
        }
        if(mediaManager.remove(id)){
            return mediadel;
        }else {
            return invalid;
        }
    }

    private JSONObject toJson(Media media) {
        JSONObject json = new JSONObject();
        json.put("id", media.getMediaId().toString());
        json.put("title", media.getMediaTitle());
        json.put("description", media.getMediaDescription() == null ? JSONObject.NULL : media.getMediaDescription());
        json.put("mediaType", media.getMediaType().name());
        json.put("creatorId", media.getCreatorId().toString());
        json.put("releaseYear", media.getReleaseYear());
        json.put("genre", media.getGenre().name());
        json.put("minAge", media.getMinAge());
        json.put("avgScore", media.getAvgScore());
        json.put("favoriteCount", media.getFavoriteCount());
        return json;
    }
}
