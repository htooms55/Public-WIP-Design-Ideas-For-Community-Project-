package service;
import model.*;
import java.util.List;

public class ThreadManager {

    private final Garden garden;

    public ThreadManager(Garden garden) {
        this.garden = garden;
    }

    // Return thread list (Main can print it)
    public List<ForumThread> getThreads() {
        return garden.getForum().getThreads();
    }

    // Create a new thread
    public void createThread(User user, String title) {
        garden.getForum().addThread(title, user.username);
        System.out.println("Thread created.");
    }

    // Add a comment to a specific thread
    public void addComment(int threadIndex, User user, String commentText) {
        List<ForumThread> threads = garden.getForum().getThreads();

        if (threadIndex < 0 || threadIndex >= threads.size()) {
            System.out.println("Invalid thread index.");
            return;
        }

        ForumThread t = threads.get(threadIndex);
        t.addComment(user.username + ": " + commentText);
        System.out.println("Comment added.");
    }

    // Remove a comment from a thread (only mod/admin should call this)
    public void removeComment(int threadIndex, int commentIndex) {
        List<ForumThread> threads = garden.getForum().getThreads();

        if (threadIndex < 0 || threadIndex >= threads.size()) {
            System.out.println("Invalid thread index.");
            return;
        }

        ForumThread t = threads.get(threadIndex);

        if (commentIndex < 0 || commentIndex >= t.getComments().size()) {
            System.out.println("Invalid comment index.");
            return;
        }

        t.removeComment(commentIndex);
        System.out.println("Comment removed.");
    }

    // Get a specific thread object (Main can loop through its comments)
    public ForumThread getThread(int index) {
        List<ForumThread> threads = garden.getForum().getThreads();

        if (index < 0 || index >= threads.size()) {
            return null;
        }
        return threads.get(index);
    }

    // Delete a thread entirely (mod/admin only)
    public void deleteThread(int index) {
        List<ForumThread> threads = garden.getForum().getThreads();

        if (index < 0 || index >= threads.size()) {
            System.out.println("Invalid index.");
            return;
        }

        garden.getForum().removeThread(index);
        System.out.println("Thread removed.");
    }
}
