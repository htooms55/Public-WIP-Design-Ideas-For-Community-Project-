package model;
import java.io.Serializable;
import java.util.ArrayList;

public class ForumThread implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String createdBy;
    private ArrayList<String> comments;

    public ForumThread(String title, String createdBy) {
        this.title = title;
        this.createdBy = createdBy;
        this.comments = new ArrayList<>();
    }

    public String getTitle() { return title; }
    public String getCreatedBy() { return createdBy; }
    public ArrayList<String> getComments() { return comments; }

    public void addComment(String comment) {
        if (comment != null && !comment.isEmpty()) {
            comments.add(comment);
        }
    }

    public void removeComment(int index) {
        if (index >= 0 && index < comments.size()) {
            comments.remove(index);
        }
    }

    @Override
    public String toString() {
        return "Thread{\"" + title + "\" by " + createdBy +
                ", comments=" + comments.size() + "}";
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
