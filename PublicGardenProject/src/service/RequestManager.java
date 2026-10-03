package service;
import model.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RequestManager {

    private final Garden garden;
    private final Scanner scanner;

    public RequestManager(Garden garden, Scanner scanner) {
        this.garden = garden;
        this.scanner = scanner;

        // ensure queue exists
        if (garden.getPlantQueue().requests == null) {
            garden.getPlantQueue().requests = new ArrayList<>();
        }
            // Sync nextId with existing requests so IDs never collide
        int maxId = 0;
        for (RequestForm r : garden.getPlantQueue().requests) {
            if (r.getRequestId() > maxId) {
                maxId = r.getRequestId();
            }
        }
        RequestForm.setNextId(maxId + 1);
    }

    // submit a new request
    public void submitPlantRequest(User user) {
        if (garden.getBeds().isEmpty()) {
            System.out.println("No beds in the garden. Cannot submit request.");
            return;
        }

        System.out.print("Plant name: ");
        String plantName = scanner.nextLine();

        System.out.print("Plant type: ");
        String plantType = scanner.nextLine();

        System.out.println("Choose a bed:");
        ArrayList<Bed> beds = garden.getBeds();
        for (int i = 0; i < beds.size(); i++) {
            System.out.println("[" + i + "] " + beds.get(i));
        }

        System.out.print("Bed index: ");
        int bedIndex = readInt();
        if (bedIndex < 0 || bedIndex >= beds.size()) {
            System.out.println("Invalid bed index.");
            return;
        }
        Bed bed = beds.get(bedIndex);

        System.out.print("Comment (optional): ");
        String comment = scanner.nextLine();

        // create new RequestForm
        RequestForm form = new RequestForm(user, plantName, bed, comment, plantType);

        garden.getPlantQueue().requests.add(form);

        System.out.println("Request submitted (ID: " + form.getRequestId() + ")");
    }

    // show this user's requests
    public void viewMyRequests(User user) {
        boolean found = false;

        System.out.println("\nMy Requests:");
        for (RequestForm form : garden.getPlantQueue().requests) {
            if (form.getUser() == user) {
                System.out.println(form);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No requests found for you.");
        }
    }

    // show all requests (mod/admin)
    public void viewAllRequests(boolean allowModify) {
        var queue = garden.getPlantQueue();

        if (queue.requests.isEmpty()) {
            System.out.println("No requests.");
            return;
        }

        System.out.println("\nAll Requests:");
        for (int i = 0; i < queue.requests.size(); i++) {
            System.out.println("[" + i + "] " + queue.requests.get(i));
        }

        if (!allowModify) return;

        System.out.print("Approve or reject? (a/r/n): ");
        String ans = scanner.nextLine().trim().toLowerCase();
        if (ans.equals("n") || ans.isEmpty()) return;

        System.out.print("Enter index: ");
        int idx = readInt();
        if (idx < 0 || idx >= queue.requests.size()) {
            System.out.println("Invalid index.");
            return;
        }

        switch (ans) {
            case "a" -> approveRequest(idx);
            case "r" -> rejectRequest(idx);
            default -> System.out.println("Unknown option.");
        }
    }

    // APPROVE: make Plant and add to Bed
    public void approveRequest(int index) {
        PlantQueue queue = garden.getPlantQueue();

        if (index < 0 || index >= queue.requests.size()) {
            System.out.println("Invalid index.");
            return;
        }

        RequestForm form = queue.requests.get(index);
        Bed bed = form.getBed();

        // CAPACITY CHECK — use Bed's built-in logic
        if (bed.getPlants().size() >= bed.getCapacity()) {
            System.out.println("Bed is full! Cannot approve request.");
            return;
        }

        // BUILD PLANT OBJECT
        Plant plant = new Plant();
        plant.name  = form.getPlantName();
        plant.type  = form.getPlantType();
        plant.owner = form.getUser();
        plant.age   = "Seedling";   // default age — you can modify

        // Add to bed using Bed.addPlant()
        bed.addPlant(plant);

        // Add plant to the user's personal list of plants
        if (form.getUser() != null) {
            form.getUser().addPlant(plant);
        }

        // Remove request after planting
        queue.requests.remove(index);

        System.out.println("Request approved! Added plant \"" 
                + plant.name + "\" (type: " + plant.type 
                + ") to bed: " + bed);
    }

    // REJECT: remove request
    public void rejectRequest(int index) {
        PlantQueue queue = garden.getPlantQueue();

        if (index < 0 || index >= queue.requests.size()) {
            System.out.println("Invalid index.");
            return;
        }

        queue.requests.remove(index);
        System.out.println("Request rejected.");
    }

    // helper
    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Enter a number: ");
            }
        }
    }
}
