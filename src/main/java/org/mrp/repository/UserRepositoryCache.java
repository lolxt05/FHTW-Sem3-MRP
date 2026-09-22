package org.mrp.repository;

import org.mrp.modal.User;

import java.util.UUID;

public class UserRepositoryCache extends StringCache implements UserRepository{
    @Override
    public UUID create(User user) {
        return null;
    }
}
