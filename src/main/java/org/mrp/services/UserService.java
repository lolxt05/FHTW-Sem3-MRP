package org.mrp.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mrp.model.HttpResponse;
import org.mrp.model.Session;
import org.mrp.model.User;
import org.mrp.model.requests.LoginRequest;
import org.mrp.model.requests.UserAddRequest;
import org.mrp.repository.media.MediaManager;
import org.mrp.repository.sessions.SessionManager;
import org.mrp.repository.user.UserManager;

import java.util.*;

public class UserService{
    UserManager userManager =  UserManager.getInstance();
    MediaManager mediaManager = MediaManager.getInstance();
    SessionManager sessionManager = SessionManager.getInstance();
    HttpResponse invalid = HttpResponse.message(500, "Hell nah");
    HttpResponse notFound = HttpResponse.message(404, "user not found");
    HttpResponse forbidden = HttpResponse.message(403, "not your user");
    HttpResponse nameTaken = HttpResponse.message(409, "username already taken");
    HttpResponse wrongLogin = HttpResponse.message(401, "wrong username or password");
    HttpResponse userdel = new HttpResponse(204, "");

    public HttpResponse add(UserAddRequest request) {
        if(userManager.get(request.getUsername()) != null){
            return nameTaken;
        }
        User user = new User(request.getUsername(), request.getPassword());
        if(userManager.add(user)){
            return new HttpResponse(201, toJson(user).toString());
        }else {
            return invalid;
        }
    }

    public HttpResponse get(UUID id) {
        User user = userManager.get(id);
        if(user == null){
            return notFound;
        }
        return new HttpResponse(200, toJson(user).toString());
    }

    public HttpResponse update(UUID id, Session session, UserAddRequest request) {
        User user = userManager.get(id);
        if(user == null){
            return notFound;
        }
        if(!id.equals(session.getUserId())){
            return forbidden;
        }
        User sameName = userManager.get(request.getUsername());
        if(sameName != null && !id.equals(sameName.getUserId())){
            return nameTaken;
        }
        User updated = new User(id, request.getUsername(), request.getPassword(), user.getAvgStars());
        if(userManager.update(id, updated)){
            return new HttpResponse(200, toJson(updated).toString());
        }else {
            return invalid;
        }
    }

    public HttpResponse remove(UUID id, Session session) {
        if(userManager.get(id) == null){
            return notFound;
        }
        if(!id.equals(session.getUserId())){
            return forbidden;
        }
        if(!userManager.remove(id)){
            return invalid;
        }
        mediaManager.removeCachedByCreator(id);
        sessionManager.invalidateUserSessions(id);
        return userdel;
    }

    public HttpResponse login(LoginRequest request) {
        User user = userManager.get(request.getUsername());
        if(user == null || !PasswordValidator.verifyPassword(request.getPassword(), user.getUserPw())){
            return wrongLogin;
        }
        Session session = sessionManager.getNewSession(user);
        JSONObject json = new JSONObject();
        json.put("token", session.getToken());
        json.put("userId", user.getUserId().toString());
        return new HttpResponse(201, json.toString());
    }

    public HttpResponse getNameCompletions(String prefix) {
        return new HttpResponse(200, new JSONArray(userManager.getNameCompletions(prefix)).toString());
    }

    private JSONObject toJson(User user) {
        JSONObject json = new JSONObject();
        json.put("id", user.getUserId().toString());
        json.put("username", user.getUserName());
        json.put("avgStars", user.getAvgStars());
        return json;
    }
}
