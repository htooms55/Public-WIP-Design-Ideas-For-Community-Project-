package model;
import java.io.Serializable;
import java.util.ArrayList;
public class Plant implements Serializable {
    private static final long serialVersionUID = 1L;

    public String name;
    public String type;
    public String age;
    public User owner;
    public String pfp;

    public Plant() {
        this("", "", "", null, "");
    }

    public Plant(String name, String type, String age, User owner, String pfp) {
        this.name = name;
        this.type = type;
        this.age = age;
        this.owner = owner;
        this.pfp = pfp;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
