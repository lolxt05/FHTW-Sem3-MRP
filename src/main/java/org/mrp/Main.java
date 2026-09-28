package org.mrp;

import org.mrp.modal.Media;
import org.mrp.modal.User;
import org.mrp.modal.enums.Genres;
import org.mrp.modal.enums.MediaType;
import org.mrp.repository.media.MediaManager;
import org.mrp.repository.user.UserManager;
import org.mrp.repository.user.UserRepositoryCache;

import java.util.UUID;

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
        System.out.println(userManager.login("u1", "Password"));
        System.out.println(userManager.login("u2", "Password"));
        System.out.println(userManager.login("u3", "Password"));

        System.out.println("\nNot Working User examples:");
        System.out.println(userManager.login("u4", "Password"));

        mediaManager.add(new Media("Jurrasic Park 1", "Desc", MediaType.MOVIE, UUID.randomUUID(), 2000, Genres.ACTION, 12));
        mediaManager.add(new Media("Jurrasic Park 2", "Desc", MediaType.MOVIE, UUID.randomUUID(), 2000, Genres.ACTION, 12));
        mediaManager.add(new Media("Jurrasic Park 3", "Desc", MediaType.MOVIE, UUID.randomUUID(), 2000, Genres.ACTION, 12));

        System.out.println("\nWorking Media examples:");
        System.out.println(mediaManager.get("Jurrasic Park 1"));
        System.out.println(mediaManager.get("Jurrasic Park 2"));
        System.out.println(mediaManager.get("Jurrasic Park 3"));

        System.out.println("\nNot Working Media examples:");
        System.out.println(userManager.get("Jurrasic Park 4"));
    }
}
