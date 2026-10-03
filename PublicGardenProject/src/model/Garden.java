package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Garden implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Bed> beds;
    private ArrayList<User> users;
    private String weather;
    private String location;       // "lat,lon"
    private Forum forum;
    private PlantQueue plantQueue;
    private String welcomeMessage;

    // Built-in mini geocoder
    private static final HashMap<String, String> CITY_COORDS = new HashMap<>();
    static {
        CITY_COORDS.put("kansas city", "39.0997,-94.5786");
        CITY_COORDS.put("new york", "40.7128,-74.0060");
        CITY_COORDS.put("los angeles", "34.0522,-118.2437");
        CITY_COORDS.put("chicago", "41.8781,-87.6298");
        CITY_COORDS.put("houston", "29.7604,-95.3698");
        CITY_COORDS.put("phoenix", "33.4484,-112.0740");
        CITY_COORDS.put("seattle", "47.6062,-122.3321");
        CITY_COORDS.put("miami", "25.7617,-80.1918");
        CITY_COORDS.put("denver", "39.7392,-104.9903");
        CITY_COORDS.put("san francisco", "37.7749,-122.4194");
    }

    public Garden() {
        this.beds = new ArrayList<>();
        this.users = new ArrayList<>();
        this.weather = "Unknown";
        this.location = "Unknown";
        this.forum = new Forum();
        this.plantQueue = new PlantQueue();
        this.welcomeMessage = "Welcome to the Community Garden!";
    }

    // --- weather / location ---

    public String getWeather() { return weather; }
    public void setWeather(String weather) { this.weather = weather; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public boolean setLocationByCity(String city) {
        if (city == null) return false;
        String key = city.trim().toLowerCase();
        if (CITY_COORDS.containsKey(key)) {
            this.location = CITY_COORDS.get(key);
            return true;
        }
        return false;
    }

    public ArrayList<String> getSupportedCities() {
        return new ArrayList<>(CITY_COORDS.keySet());
    }

    // --- collections / helpers ---

    public ArrayList<Bed> getBeds() { return beds; }
    public ArrayList<User> getUsers() { return users; }
    public Forum getForum() { return forum; }

    public PlantQueue getPlantQueue() { return plantQueue; }
    public void setPlantQueue(PlantQueue q) { this.plantQueue = q; }

    public String getWelcomeMessage() { return welcomeMessage; }
    public void setWelcomeMessage(String msg) { this.welcomeMessage = msg; }

    public void addUser(User user) { if (user != null) users.add(user); }
    public void removeUser(User user) { if (user != null) users.remove(user); }

    public void addBed(Bed bed) { if (bed != null) beds.add(bed); }
    public void removeBed(Bed bed) { if (bed != null) beds.remove(bed); }

    @Override
    public String toString() {
        return "Garden{" +
                "beds=" + beds.size() +
                ", users=" + users.size() +
                ", location='" + location + '\'' +
                ", weather='" + weather + '\'' +
                '}';
    }

    public void setBeds(ArrayList<Bed> beds) {
        this.beds = beds;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }
}
