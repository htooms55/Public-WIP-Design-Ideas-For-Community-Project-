package model;
import java.io.Serializable;
import java.util.ArrayList;

public class PlantQueue implements Serializable {
    private static final long serialVersionUID = 1L;

    public ArrayList<RequestForm> requests;

    public PlantQueue() {
        this.requests = new ArrayList<>();
    }

    public void enqueue(RequestForm request) {
        if (request != null) {
            requests.add(request);
        }
    }

    public RequestForm dequeue() {
        if (requests.isEmpty()) return null;
        return requests.remove(0);
    }

    public RequestForm peek() {
        if (requests.isEmpty()) return null;
        return requests.get(0);
    }

    public boolean isEmpty() {
        return requests.isEmpty();
    }

    public int size() {
        return requests.size();
    }

    public void removeRequest(RequestForm request) {
        requests.remove(request);
    }

    @Override
    public String toString() {
        return "PlantQueue{" +
                "requests=" + requests.size() +
                '}';
    }
}
