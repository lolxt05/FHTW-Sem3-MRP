package org.mrp.repository;

import org.mrp.modal.User;

import java.util.UUID;

public interface UserRepository {
    UUID create(User user);
}
