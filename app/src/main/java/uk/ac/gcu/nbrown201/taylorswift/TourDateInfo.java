package uk.ac.gcu.nbrown201.taylorswift;

import java.io.Serializable;

// this class was created following the structure of the SQLite database.
// it just takes the data and sets it and provides getter methods to be
// able to read the data easily in the application.
public class TourDateInfo implements Serializable {

    private int id;
    private String venueName;

    private float longitude;
    private float latitude;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
