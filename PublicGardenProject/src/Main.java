import java.util.Scanner;
import model.*;
import service.*;
import controller.*;

public class Main {

    private static final String SAVE_FILE = "garden.dat";

    private static Garden garden;
    private static final Scanner scanner = new Scanner(System.in);
    private static ThreadManager threadManager;
    private static RequestManager requestManager;

    // controllers
    private static RequestController requestController;
    private static ThreadController threadController;
    private static BedController bedController;
    private static ModerationController moderationController;
    private static GardenViewController gardenViewController;

    public static void main(String[] args) {

        // load or create garden
        garden = FileReader.loadGarden(SAVE_FILE);
        if (garden == null) {
            garden = new Garden();
        }

        // set up managers
        threadManager = new ThreadManager(garden);
        requestManager = new RequestManager(garden, scanner);

        // set up controllers
        requestController = new RequestController(garden, scanner, requestManager, SAVE_FILE);
        threadController = new ThreadController(threadManager, scanner, garden, SAVE_FILE);
        bedController = new BedController(garden, scanner, SAVE_FILE);
        moderationController = new ModerationController(garden, scanner, SAVE_FILE);
        gardenViewController = new GardenViewController(garden);
        
        // start background web server for the frontend
        GardenWebServer webServer = new GardenWebServer(garden, requestManager, SAVE_FILE, 8080);
        webServer.start();

        System.out.println(garden.getWelcomeMessage());

        boolean running = true;
        while (running) {
            System.out.print("\nEnter username (or type 'exit' to quit): ");
            String username = scanner.nextLine().trim();

            if (username.equalsIgnoreCase("exit")) {
                saveGarden();
                running = false;
                break;
            }

            if (username.isEmpty()) {
                System.out.println("Username cannot be empty.");
                continue;
            }

            User existing = findUserByUsername(username);

            if (existing != null) {
                boolean isMod = (existing instanceof Moderator) || (existing instanceof Admin);
                boolean isAdmin = (existing instanceof Admin);

                System.out.println("Welcome back, " +
                        (existing.name != null ? existing.name : existing.username) +
                        " [" + (isAdmin ? "Admin" : isMod ? "Moderator" : "Member") + "]");

                runUserMenu(existing, isMod, isAdmin);
            } else {
                System.out.println("No user found with that username. Let's create a new user.");
                System.out.print("Your first name: ");
                String name = scanner.nextLine().trim();

                System.out.println("Choose role:");
                System.out.println("1. Member");
                System.out.println("2. Moderator");
                System.out.println("3. Admin");
                System.out.print("Choice: ");

                int roleChoice = readInt();

                User user;
                boolean isMod = false;
                boolean isAdmin = false;

                switch (roleChoice) {
                    case 3 -> {
                        user = new Admin();
                        isMod = true;
                        isAdmin = true;
                    }
                    case 2 -> {
                        user = new Moderator();
                        isMod = true;
                    }
                    case 1 -> {
                        user = new Member();
                    }
                    default -> {
                        System.out.println("Invalid choice, defaulting to Member.");
                        user = new Member();
                    }
                }

                user.name = name;
                user.username = username;

                garden.addUser(user);
                saveGarden();

                System.out.println("User created. Logged in as " +
                        user.username + " [" + (isAdmin ? "Admin" : isMod ? "Moderator" : "Member") + "]");

                runUserMenu(user, isMod, isAdmin);
            }
        }

        if (!running) {
            System.out.println("Goodbye.");
        }
        
    }

    // find a user by username (case-insensitive)
    private static User findUserByUsername(String username) {
        if (username == null || garden.getUsers() == null) return null;

        for (User u : garden.getUsers()) {
            if (u != null && u.username != null &&
                    u.username.equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    // per-user menu
    private static void runUserMenu(User user, boolean isMod, boolean isAdmin) {
        System.out.println("\n" + garden.getWelcomeMessage());

        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n--- MENU (" + user.username + ") ---");
            System.out.println("1. View garden");
            System.out.println("2. Forum");
            System.out.println("3. Submit plant request");
            System.out.println("4. View my requests");
            System.out.println("5. Weather");

            if (isMod || isAdmin) {
                System.out.println("6. Manage plant requests (view/choose/approve/reject)");
                System.out.println("7. Ban user");
            }
            if (isAdmin) {
                System.out.println("8. Change welcome message");
                System.out.println("9. Set location by city");
                System.out.println("10. Manage beds");
            }

            System.out.println("11. Save garden");
            System.out.println("0. Logout");
            System.out.print("Choice: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> gardenViewController.viewGarden();
                case 2 -> threadController.forumMenu(user, isMod || isAdmin);
                case 3 -> requestManager.submitPlantRequest(user);
                case 4 -> requestManager.viewMyRequests(user);
                case 5 -> weatherMenu();

                case 6 -> {
                    if (isMod || isAdmin) {
                        requestController.manageRequests();
                    } else {
                        System.out.println("No permission.");
                    }
                }
                case 7 -> {
                    if (isMod || isAdmin) {
                        moderationController.banUser();
                    } else {
                        System.out.println("No permission.");
                    }
                }
                case 8 -> {
                    if (isAdmin) {
                        moderationController.changeWelcomeMessage();
                    } else {
                        System.out.println("No permission.");
                    }
                }
                case 9 -> {
                    if (isAdmin) {
                        moderationController.setLocationByCity();
                    } else {
                        System.out.println("No permission.");
                    }
                }
                case 10 -> {
                    if (isAdmin) {
                        bedController.manageBeds();
                    } else {
                        System.out.println("No permission.");
                    }
                }
                case 11 -> saveGarden();
                case 0 -> loggedIn = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // weather

    private static void weatherMenu() {
        System.out.println("\n--- WEATHER ---");
        System.out.println("Location: " + garden.getLocation());
        System.out.println("Stored weather: " + garden.getWeather());

        System.out.print("Update from API? (y/n): ");
        String ans = scanner.nextLine().trim().toLowerCase();
        if (ans.equals("y")) {
            WeatherService.updateWeatherFromApi(garden);
            System.out.println("Now: " + garden.getWeather());
            saveGarden();
        }
    }

    // save

    private static void saveGarden() {
        FileReader.saveGarden(garden, SAVE_FILE);
    }

    // input helper

    private static int readInt() {
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
