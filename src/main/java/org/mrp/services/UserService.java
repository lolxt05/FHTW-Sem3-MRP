package org.mrp.services;

import lombok.Data;
import org.mrp.modal.User;

import java.util.*;

@Data
public class UserService {

    private static final UserService INSTANCE = new UserService();

    private final TreeSet<String> userNames = new TreeSet<>();
    private final TreeSet<String> userPasswords = new TreeSet<>();
    private final TreeMap<UUID, User> cachedUsers = new TreeMap<>();
    private int cachedUsersCount = 0;
    private final int cachedUsersLimit = 10000;

    private UserService() {
        // initialize
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}

