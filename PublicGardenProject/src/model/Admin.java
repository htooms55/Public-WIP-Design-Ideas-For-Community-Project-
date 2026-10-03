package model;
import java.io.Serializable;
import java.util.ArrayList;

public class Admin extends User {
    private static final long serialVersionUID = 1L;

    public Admin() {
        super(); // User() will auto-set accountCreated to now
    }

    public Admin(String name,
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
        return "Admin{" +
                "username='" + username + '\'' +
                ", accountCreated='" + accountCreated + '\'' +
                '}';
    }
}
