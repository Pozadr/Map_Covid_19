package pl.pozadr.map.model;

import com.opencsv.bean.CsvBindByName;

public class Point {
    @CsvBindByName(column = "Latitude")
    private Double lat;
    @CsvBindByName(column = "Longitude")
    private Double lon;
    @CsvBindByName(column = "Description")
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

    @Override
    public String toString() {
        return "Point{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", description='" + description + '\'' +
                '}';
    }
}
