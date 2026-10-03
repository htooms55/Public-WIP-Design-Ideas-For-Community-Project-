package controller;
import model.*;
import service.*;
import java.util.List;
import java.util.Scanner;

public class ThreadController {

    private final ThreadManager threadManager;
    private final Scanner scanner;
    private final Garden garden;
    private final String saveFile;

    public ThreadController(ThreadManager threadManager, Scanner scanner, Garden garden, String saveFile) {
        this.threadManager = threadManager;
        this.scanner = scanner;
        this.garden = garden;
        this.saveFile = saveFile;
    }

    public void forumMenu(User user, boolean canModerate) {
        boolean inForum = true;

        while (inForum) {
            System.out.println("\n--- FORUM ---");
            List<ForumThread> threads = threadManager.getThreads();
            if (threads.isEmpty()) {
                System.out.println("(no threads)");
            } else {
                for (int i = 0; i < threads.size(); i++) {
                    System.out.println("[" + i + "] " + threads.get(i));
                }
            }

            System.out.println("1. Create thread");
            System.out.println("2. Open thread");
            if (canModerate) {
                System.out.println("3. Delete thread");
            }
            System.out.println("0. Back");
            System.out.print("Choice: ");

            int c = readInt();

            switch (c) {
                case 1 -> {
                    System.out.print("Thread title: ");
                    String title = scanner.nextLine().trim();
                    threadManager.createThread(user, title);
                    saveGarden();
                }
                case 2 -> {
                    System.out.print("Thread index: ");
                    int idx = readInt();
                    openThread(user, idx, canModerate);
                }
                case 3 -> {
                    if (canModerate) {
                        System.out.print("Thread index to delete: ");
                        int idxDel = readInt();
                        threadManager.deleteThread(idxDel);
                        saveGarden();
                    } else {
                        System.out.println("No permission.");
                    }
                }
                case 0 -> inForum = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void openThread(User user, int threadIndex, boolean canModerate) {
        ForumThread t = threadManager.getThread(threadIndex);
        if (t == null) {
            System.out.println("Invalid thread.");
            return;
        }

        boolean inThread = true;
        while (inThread) {
            System.out.println("\n--- THREAD: " + t.getTitle() + " ---");
            List<String> comments = t.getComments();
            if (comments.isEmpty()) {
                System.out.println("(no comments)");
            } else {
                for (int i = 0; i < comments.size(); i++) {
                    System.out.println("[" + i + "] " + comments.get(i));
                }
            }

            System.out.println("1. Add comment");
            if (canModerate) {
                System.out.println("2. Remove comment");
            }
            System.out.println("0. Back");
            System.out.print("Choice: ");

            int c = readInt();
            switch (c) {
                case 1 -> {
                    System.out.print("Comment: ");
                    String text = scanner.nextLine().trim();
                    threadManager.addComment(threadIndex, user, text);
                    saveGarden();
                }
                case 2 -> {
                    if (canModerate) {
                        System.out.print("Comment index to remove: ");
                        int ci = readInt();
                        threadManager.removeComment(threadIndex, ci);
                        saveGarden();
                    } else {
                        System.out.println("No permission.");
                    }
                }
                case 0 -> inThread = false;
                default -> System.out.println("Invalid choice.");
            }
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
