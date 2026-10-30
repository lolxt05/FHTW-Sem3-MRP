package org.mrp;

import org.mrp.config.Server;
import org.mrp.repository.media.MediaManager;
import org.mrp.repository.user.UserManager;

import java.util.UUID;

public class Main {

    static void main() {
        UserManager userManager = UserManager.getInstance();
        MediaManager mediaManager = MediaManager.getInstance();

        Server server = new Server();
    }
}