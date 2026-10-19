package org.mrp.services;

import org.json.JSONObject;
import org.mrp.model.HttpResponse;
import org.mrp.model.User;
import org.mrp.model.requests.UserAddRequest;
import org.mrp.model.requests.UserDeleteRequest;
import org.mrp.repository.user.UserManager;

import java.util.*;

public class UserService{
    UserManager userManager =  UserManager.getInstance();
    HttpResponse invalid = new HttpResponse(500, "Hell nah");
    HttpResponse useradded = new HttpResponse(200, "added the user");
    HttpResponse userdel = new HttpResponse(200, "deleted the user");

    public HttpResponse add(UserAddRequest request) {
        if(userManager.add(new User(request.getUsername(), request.getPassword()))){
            return useradded;
        }else {
            return invalid;
        }
    }

    public HttpResponse get(JSONObject json) {
        return invalid;
    }

    public HttpResponse update(JSONObject json) {
        return invalid;
    }

    public HttpResponse remove(UserDeleteRequest request) {
        userManager.remove(request.getUserId());
        return userdel;
    }

    public HttpResponse login(JSONObject json) {
        return invalid;
    }

    public HttpResponse getNameCompletions(JSONObject json) {
        return invalid;
    }
}

