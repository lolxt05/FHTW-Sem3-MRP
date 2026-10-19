package org.mrp;

import org.mrp.config.Server;
import org.mrp.repository.media.MediaManager;
import org.mrp.repository.user.UserManager;

import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static void main() {
        UserManager userManager = UserManager.getInstance();
        MediaManager mediaManager = MediaManager.getInstance();

        Server server = new Server();
    }
}