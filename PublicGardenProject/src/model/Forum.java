package model;
import java.io.Serializable;
import java.util.ArrayList;

public class Forum implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<ForumThread> threads;

    public Forum() {
        this.threads = new ArrayList<>();
    }

    public ArrayList<ForumThread> getThreads() {
        return threads;
    }

    public void addThread(String title, String createdBy) {
        if (title != null && !title.isEmpty()) {
            threads.add(new ForumThread(title, createdBy));
        }
    }

    public void removeThread(int index) {
        if (index >= 0 && index < threads.size()) {
            threads.remove(index);
        }
    }

    public ForumThread getThread(int index) {
        if (index >= 0 && index < threads.size()) {
            return threads.get(index);
        }
        return null;
    }

    public int size() {
        return threads.size();
    }

    @Override
    public String toString() {
        return "Forum{" +
                "threads=" + threads.size() +
                '}';
    }

    public void setThreads(ArrayList<ForumThread> threads) {
        this.threads = threads;
    }
}
