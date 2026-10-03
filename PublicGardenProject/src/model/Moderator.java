package model;
import java.io.Serializable;
import java.util.ArrayList;
public class Moderator extends User {
    private static final long serialVersionUID = 1L;

    public Moderator() {
        super(); // User() will auto-set accountCreated to now
    }

    public Moderator(String name,
                     String username,
                     String description,
                     ArrayList<Plant> userPlants,
                     String accountCreated,
                     String pfp) {
        // If accountCreated is null/blank, User will auto-fill with current time
        super(name, username, description, userPlants, accountCreated, pfp);
    }

    @Override
    public String toString() {
        return "Moderator{" +
                "username='" + username + '\'' +
                ", accountCreated='" + accountCreated + '\'' +
                '}';
    }
}
