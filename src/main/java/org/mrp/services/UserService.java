package org.mrp.services;

import lombok.Data;
import org.mrp.modal.User;
import org.mrp.repository.user.UserManager;

import java.util.*;

public class UserService extends UserManager{

    private static final UserService INSTANCE = new UserService();
    private static final UserManager userManager = new UserManager();

    private UserService() {
        // initialize
    }

    public static UserService getInstance() {return INSTANCE;}
}

