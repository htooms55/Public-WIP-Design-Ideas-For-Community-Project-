package controller;
import model.*;
import service.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BedController {

    private final Garden garden;
    private final Scanner scanner;
    private final String saveFile;

    public BedController(Garden garden, Scanner scanner, String saveFile) {
        this.garden = garden;
        this.scanner = scanner;
        this.saveFile = saveFile;
    }

    public void manageBeds() {
        boolean inBeds = true;

        while (inBeds) {
            System.out.println("\n--- MANAGE BEDS ---");
            System.out.println("1. View beds");
            System.out.println("2. Add bed");
            System.out.println("3. Remove bed");
            System.out.println("0. Back");
            System.out.print("Choice: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> viewBeds();
                case 2 -> addBed();
                case 3 -> removeBed();
                case 0 -> inBeds = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void viewBeds() {
        System.out.println("\n--- GARDEN BEDS ---");
        System.out.println(garden);

        ArrayList<Bed> beds = garden.getBeds();
        if (beds == null || beds.isEmpty()) {
            System.out.println("No beds yet.");
            return;
        }

        for (int i = 0; i < beds.size(); i++) {
            Bed b = beds.get(i);
            System.out.println("[" + i + "] " + b);

            List<Plant> plants = b.getPlants();
            if (plants != null && !plants.isEmpty()) {
                for (Plant p : plants) {
                    System.out.println("   - " + p);
                }
            }
        }
    }

    private void addBed() {
        System.out.print("Bed capacity: ");
        int capacity = readInt();
        System.out.print("Soil type: ");
        String soil = scanner.nextLine().trim();

        Bed bed = new Bed(capacity, soil);
        garden.addBed(bed);

        System.out.println("Bed added: " + bed);
        saveGarden();
    }

    private void removeBed() {
        ArrayList<Bed> beds = garden.getBeds();
        if (beds == null || beds.isEmpty()) {
            System.out.println("No beds to remove.");
            return;
        }

        System.out.println("\nCurrent beds:");
        for (int i = 0; i < beds.size(); i++) {
            System.out.println("[" + i + "] " + beds.get(i));
        }

        System.out.print("Remove which bed index? ");
        int idx = readInt();
        if (idx < 0 || idx >= beds.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Bed b = beds.get(idx);
        garden.removeBed(b);
        System.out.println("Removed bed: " + b);
        saveGarden();
    }

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
