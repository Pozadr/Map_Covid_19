package pl.pozadr.map.dto;

import pl.pozadr.map.model.Point;

import java.util.List;

public class MapDto {
    private Double startLat;
    private Double startLon;
    private Integer zoom;
    private List<Point> points;

    public MapDto() {
    }

    public MapDto(Double startLat, Double startLon, Integer zoom, List<Point> points) {
        this.startLat = startLat;
        this.startLon = startLon;
        this.zoom = zoom;
        this.points = points;
    }

    public Double getStartLat() {
        return startLat;
    }

    public void setStartLat(Double startLat) {
        this.startLat = startLat;
    }

    public Double getStartLon() {
        return startLon;
    }

    public void setStartLon(Double startLon) {
        this.startLon = startLon;
    }

    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "MapDto{" +
                "startLat=" + startLat +
                ", startLon=" + startLon +
                ", zoom=" + zoom +
                ", points=" + points +
                '}';
    }
}
