package model;
import java.io.Serializable;
import java.util.ArrayList;
public class Member extends User {

    public Member() {
        super(); // uses default User() which auto-sets accountCreated to now
    }

    public Member(String name,
                  String username,
                  String description,
                  ArrayList<Plant> userPlants,
                  String accountCreated,
                  String pfp) {
        // if accountCreated is null/blank, User will auto-fill with "now"
        super(name, username, description, userPlants, accountCreated, pfp);
    }

    @Override
    public String toString() {
        return "Member{" +
                "username='" + username + '\'' +
                ", accountCreated='" + accountCreated + '\'' +
                '}';
    }
}
