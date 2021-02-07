package pl.pozadr.map.dto;

import java.util.List;

/**
 * Data transfer object. Contains data on the history of COVID-19 in the country.
 * Data format used in compatibility with Google Charts - List <List <Object>>.
 */
public class HistoryChartDto {
    private String country;
    private List<List<Object>> googleChartsData;


    public HistoryChartDto() {
    }

    public HistoryChartDto(String country, List<List<Object>> googleChartsData) {
        this.country = country;
        this.googleChartsData = googleChartsData;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<List<Object>> getGoogleChartsData() {
        return googleChartsData;
    }

    public void setGoogleChartsData(List<List<Object>> googleChartsData) {
        this.googleChartsData = googleChartsData;
    }

    @Override
    public String toString() {
        return "HistoryDto{" +
                "country='" + country + '\'' +
                ", googleChartsData=" + googleChartsData +
                '}';
    }
}
