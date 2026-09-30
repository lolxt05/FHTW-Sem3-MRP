package org.mrp;

import org.mrp.modal.Media;
import org.mrp.modal.User;
import org.mrp.modal.enums.Genres;
import org.mrp.modal.enums.MediaType;
import org.mrp.repository.media.MediaManager;
import org.mrp.repository.user.UserManager;
import org.mrp.repository.user.UserRepositoryCache;

import java.util.UUID;
import java.util.logging.Logger;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static void main() {
        UserManager userManager = UserManager.getInstance();
        MediaManager mediaManager = MediaManager.getInstance();

        userManager.add(new User("u1", "Password"));
        userManager.add(new User("u2", "Password"));
        userManager.add(new User("u3", "Password"));

        System.out.println("Working User examples:");
        System.out.println("u1: " + userManager.login("u1", "Password"));
        System.out.println("u2: " + userManager.login("u2", "Password"));
        System.out.println("u3: " + userManager.login("u3", "Password"));

        userManager.remove(userManager.login("u3", "Password"));

        System.out.println("\nNot Working User examples:");
        System.out.println("u3: " + userManager.login("u3", "Password"));
        System.out.println("u4: " + userManager.login("u4", "Password"));

        mediaManager.add(new Media("Jurrasic Park 1", "Desc", MediaType.MOVIE, UUID.randomUUID(), 2000, Genres.ACTION, 12));
        mediaManager.add(new Media("Jurrasic Park 2", "Desc", MediaType.MOVIE, UUID.randomUUID(), 2000, Genres.ACTION, 12));
        mediaManager.add(new Media("Jurrasic Park 3", "Desc", MediaType.MOVIE, UUID.randomUUID(), 2000, Genres.ACTION, 12));

        System.out.println("\nWorking Media examples:");
        System.out.println("JP1: " + mediaManager.get("Jurrasic Park 1"));
        System.out.println("JP2: " + mediaManager.get("Jurrasic Park 2"));
        System.out.println("JP3: " + mediaManager.get("Jurrasic Park 3"));

        mediaManager.remove(mediaManager.get("Jurrasic Park 3").getMediaId());

        System.out.println("\nNot Working Media examples:");
        System.out.println("JP3: " + mediaManager.get("Jurrasic Park 3"));
        System.out.println("JP4: " + mediaManager.get("Jurrasic Park 4"));
    }
}
