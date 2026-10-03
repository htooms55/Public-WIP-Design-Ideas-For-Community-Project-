package controller;
import model.*;
import service.*;
import java.util.ArrayList;
import java.util.List;

public class GardenViewController {

    private final Garden garden;

    public GardenViewController(Garden garden) {
        this.garden = garden;
    }

    public void viewGarden() {
        System.out.println("\n=================================");
        System.out.println("        COMMUNITY GARDEN         ");
        System.out.println("=================================");

        // basic info
        System.out.println("Welcome : " + safe(garden.getWelcomeMessage()));
        System.out.println("Location: " + safe(garden.getLocation()));
        System.out.println("Weather : " + safe(garden.getWeather()));

        // beds
        System.out.println("\n--- BEDS ---");
        ArrayList<Bed> beds = garden.getBeds();
        if (beds == null || beds.isEmpty()) {
            System.out.println("  (no beds yet)");
        } else {
            for (int i = 0; i < beds.size(); i++) {
                Bed b = beds.get(i);
                List<Plant> plants = b.getPlants();

                int plantCount = (plants == null) ? 0 : plants.size();
                System.out.println("[" + i + "] Bed");
                System.out.println("     Soil     : " + b.getSoil());
                System.out.println("     Capacity : " + b.getCapacity());
                System.out.println("     Plants   : " + plantCount);

                if (plants != null && !plants.isEmpty()) {
                    for (Plant p : plants) {
                        // rely on Plant.toString() LOL
                        System.out.println("       - " + p);
                    }
                }
            }
        }

        // users
        System.out.println("\n--- USERS ---");
        ArrayList<User> users = garden.getUsers();
        if (users == null || users.isEmpty()) {
            System.out.println("  (no users yet)");
        } else {
            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);
                String role = roleOf(u);
                String uname = (u.username != null) ? u.username : "(no username)";
                System.out.println("[" + i + "] " + uname + "  [" + role + "]");
            }
        }

        System.out.println("=================================\n");
    }

    private String safe(String s) {
        return (s == null || s.isBlank()) ? "(none)" : s;
    }

    private String roleOf(User u) {
        if (u instanceof Admin) return "Admin";
        if (u instanceof Moderator) return "Moderator";
        if (u instanceof Member) return "Member";
        return "User";
    }
}
