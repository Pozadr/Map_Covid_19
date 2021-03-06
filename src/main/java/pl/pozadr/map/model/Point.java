package pl.pozadr.map.model;

import com.opencsv.bean.CsvBindByName;

public class Point {
    @CsvBindByName(column = "Lat")
    private Double lat;
    @CsvBindByName(column = "Long_")
    private Double lon;
    @CsvBindByName(column = "Country_Region")
    private String country;
    @CsvBindByName(column = "Combined_Key")
    private String region;
    @CsvBindByName(column = "Confirmed")
    private Integer confirmed;
    @CsvBindByName(column = "Deaths")
    private Integer deaths;
    @CsvBindByName(column = "Recovered")
    private Integer recovered;
    @CsvBindByName(column = "Active")
    private Integer active;
    @CsvBindByName(column = "Incident_Rate")
    private Double incidentRate;
    @CsvBindByName(column = "Case_Fatality_Ratio")
    private Double caseFatalityRatio;

    private String description;


    public Point() {
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getRecovered() {
        return recovered;
    }

    public void setRecovered(Integer recovered) {
        this.recovered = recovered;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Double getIncidentRate() {
        return incidentRate;
    }

    public void setIncidentRate(Double incidentRate) {
        this.incidentRate = incidentRate;
    }

    public Double getCaseFatalityRatio() {
        return caseFatalityRatio;
    }

    public void setCaseFatalityRatio(Double caseFatalityRatio) {
        this.caseFatalityRatio = caseFatalityRatio;
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
                "Country='" + country + '\'' +
                "Region='" + region + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", description='" + description + '\'' +
                '}';
    }
}
