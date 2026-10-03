package model;
import java.io.Serializable;
import java.time.LocalDate;

public class RequestForm implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int nextId = 1;

    private int requestId;
    private User user;
    private String plantName;
    private String plantType;
    private Bed bed;
    private String date;
    private String comment;

    public RequestForm() {
        this.requestId = nextId++;
        this.date = LocalDate.now().toString();
    }

    public RequestForm(User user, String plantName, Bed bed, String comment, String plantType) {
            this.requestId = nextId++;
            this.user = user;
            this.plantName = plantName;
            this.plantType = plantType;
        this.bed = bed;
        this.date = LocalDate.now().toString();
        this.comment = comment;
    }

    public int getRequestId() {
        return requestId;
    }

    public User getUser() {
        return user;
    }

    public String getPlantName() {
        return plantName;
    }

    public Bed getBed() {
        return bed;
    }

    public String getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "RequestForm{" +
                "requestId=" + requestId +
                ", user=" + (user != null ? user.username : "null") +
                ", plantName='" + plantName + '\'' +
                ", bed=" + bed +
                ", date='" + date + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public static void setNextId(int id) {
        nextId = id;
    }

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }
}
