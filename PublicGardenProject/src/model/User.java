package model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public String name;
    public String username;
    public String description;
    public ArrayList<Plant> userPlants;
    public String accountCreated;   // replaced accountAge
    public String pfp;

    // Default: auto-populate current date/time
    public User() {
        this("", "", "", new ArrayList<>(), null, "");
    }

    // If accountCreated is null/blank, we auto-fill with now
    public User(String name, String username, String description,
                ArrayList<Plant> userPlants, String accountCreated, String pfp) {
        this.name = name;
        this.username = username;
        this.description = description;
        this.userPlants = (userPlants != null) ? userPlants : new ArrayList<>();
        if (accountCreated == null || accountCreated.isBlank()) {
            this.accountCreated = LocalDateTime.now().toString();
        } else {
            this.accountCreated = accountCreated;
        }
        this.pfp = pfp;
    }

    public void addPlant(Plant plant) {
        if (plant != null) {
            userPlants.add(plant);
        }
    }

    public void removePlant(Plant plant) {
        if (userPlants != null) {
            userPlants.remove(plant);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", accountCreated='" + accountCreated + '\'' +
                '}';
    }
}
