package service;
import model.*;
import java.io.*;

public class FileReader {

    // Save the entire Garden object to disk
    public static void saveGarden(Garden garden, String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(garden);
            System.out.println("Garden saved to: " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving garden: " + e.getMessage());
        }
    }

    // Load the entire Garden from disk
    public static Garden loadGarden(String fileName) {
        File f = new File(fileName);

        // no save file -> make new garden
        if (!f.exists()) {
            System.out.println("No save file. Making new garden.");
            return new Garden();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            Garden g = (Garden) in.readObject();
            System.out.println("Garden loaded from: " + fileName);
            return g;
        } 
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading garden: " + e.getMessage());
            System.out.println("Making new garden.");
            return new Garden();   // important: NEVER return null
        }
    }
}
