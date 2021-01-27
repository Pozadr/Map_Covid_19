package pl.pozadr.map.model;

public class Point {
    private Double lat;
    private Double lon;
    private String description;

    public Point() {
    }

    public Point(Double lat, Double lon, String description) {
        this.lat = lat;
        this.lon = lon;
        this.description = description;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
