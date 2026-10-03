package model;
import java.io.Serializable;
import java.util.ArrayList;

public class Bed implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Plant> plants;
    private int capacity;
    private String soil;

    public Bed(int capacity, String soil) {
        this.capacity = capacity;
        this.soil = soil;
        this.plants = new ArrayList<>();
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getSoil() {
        return soil;
    }

    public void addPlant(Plant plant) {
        if (plant != null && plants.size() < capacity) {
            plants.add(plant);
        }
    }

    public void removePlant(Plant plant) {
        plants.remove(plant);
    }

    @Override
    public String toString() {
        return "Bed{" +
                "capacity=" + capacity +
                ", soil='" + soil + '\'' +
                ", plants=" + plants.size() +
                '}';
    }

    public void setPlants(ArrayList<Plant> plants) {
        this.plants = plants;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }
}
