package controller;
import model.*;
import service.*;
import java.util.Scanner;

public class RequestController {

    private final Garden garden;
    private final Scanner scanner;
    private final RequestManager requestManager;
    private final String saveFile;

    public RequestController(Garden garden, Scanner scanner, RequestManager requestManager, String saveFile) {
        this.garden = garden;
        this.scanner = scanner;
        this.requestManager = requestManager;
        this.saveFile = saveFile;
    }

    @SuppressWarnings("ConvertToStringSwitch")
    public void manageRequests() {
        PlantQueue q = garden.getPlantQueue();

        if (q == null || q.requests == null || q.requests.isEmpty()) {
            System.out.println("No requests.");
            return;
        }

        System.out.println("\n--- PLANT REQUESTS ---");
        for (int i = 0; i < q.requests.size(); i++) {
            RequestForm r = q.requests.get(i);

            String plantName;
            try {
                plantName = r.getPlantName();
            } catch (Exception e) {
                plantName = "(no name)";
            }

            String who = "unknown";
            try {
                if (r.getUser() != null && r.getUser().username != null) {
                    who = r.getUser().username;
                }
            } catch (Exception e) {
                // ignore
            }

            System.out.println("[" + i + "] " + plantName + " (by " + who + ")");
        }

        System.out.print("Select request index (or -1 to cancel): ");
        int idx = readInt();
        if (idx == -1) {
            return;
        }
        if (idx < 0 || idx >= q.requests.size()) {
            System.out.println("Invalid index.");
            return;
        }

        RequestForm form = q.requests.get(idx);

        System.out.println("\n--------------------------");
        System.out.println("      REQUEST DETAILS     ");
        System.out.println("--------------------------");
        try {
            System.out.println("Request ID : " + form.getRequestId());
        } catch (Exception e) {
        }

        try {
            if (form.getUser() != null) {
                System.out.println("User      : " + form.getUser().username);
            }
        } catch (Exception e) {
        }

        try {
            System.out.println("Plant name: " + form.getPlantName());
        } catch (Exception e) {
        }

        try {
            System.out.println("Plant type: " + form.getPlantType());
        } catch (Exception e) {
        }

        try {
            System.out.println("Bed       : " + form.getBed());
        } catch (Exception e) {
        }

        try {
            System.out.println("Date      : " + form.getDate());
        } catch (Exception e) {
        }

        try {
            System.out.println("Comment   : " + form.getComment());
        } catch (Exception e) {
        }

        System.out.println("--------------------------");
        System.out.print("Approve or reject? (a/r/n): ");
        String ans = scanner.nextLine().trim().toLowerCase();

        if (ans.equals("a")) {
            requestManager.approveRequest(idx);
            saveGarden();
        } else if (ans.equals("r")) {
            requestManager.rejectRequest(idx);
            saveGarden();
        } else {
            System.out.println("No action taken.");
        }
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
