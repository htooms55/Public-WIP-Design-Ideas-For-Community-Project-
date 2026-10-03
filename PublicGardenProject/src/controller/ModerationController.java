package controller;
import model.*;
import service.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ModerationController {

    private final Garden garden;
    private final Scanner scanner;
    private final String saveFile;

    public ModerationController(Garden garden, Scanner scanner, String saveFile) {
        this.garden = garden;
        this.scanner = scanner;
        this.saveFile = saveFile;
    }

    // ----- users (ban) -----

    public void banUser() {
        ArrayList<User> users = garden.getUsers();
        if (users == null || users.isEmpty()) {
            System.out.println("No users.");
            return;
        }

        System.out.println("\n--- USERS ---");
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            System.out.println("[" + i + "] " + (u.username != null ? u.username : "(no username)"));
        }

        System.out.print("Ban which index? ");
        int idx = readInt();
        if (idx < 0 || idx >= users.size()) {
            System.out.println("Invalid index.");
            return;
        }

        User u = users.get(idx);
        garden.removeUser(u);
        System.out.println("User removed.");
        saveGarden();
    }

    // ----- admin stuff -----

    public void changeWelcomeMessage() {
        System.out.print("New welcome message: ");
        String msg = scanner.nextLine();
        garden.setWelcomeMessage(msg);
        System.out.println("Updated.");
        saveGarden();
    }

    public void setLocationByCity() {
        System.out.print("City name: ");
        String city = scanner.nextLine().trim();
        boolean ok = garden.setLocationByCity(city);
        if (ok) {
            System.out.println("Location set to: " + garden.getLocation());
            saveGarden();
        } else {
            System.out.println("City not in list.");
        }
    }

    // ----- helpers -----

    private void saveGarden() {
        FileReader.saveGarden(garden, saveFile);
    }

    private int readInt() {
        while (true) {
            String line = scanner.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a number: ");
            }
        }
    }
}
