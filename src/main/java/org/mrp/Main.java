package org.mrp;

import org.mrp.modal.User;
import org.mrp.repository.user.UserManager;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        UserManager userManager = new UserManager();
        userManager.addUser(new User("Name", "Password"));
        System.out.println(userManager.getUser("Name").toString());
    }
}
