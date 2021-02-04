package pl.pozadr.map.model;

import java.util.Map;

public class CovidHistory {
    private String country;
    private Map<String, Long> deathsHistory;
    private Map<String, Long> confirmedHistory;
    private Map<String, Long> recoveredHistory;

    public CovidHistory() {
    }

    public CovidHistory(String country, Map<String, Long> deathsHistory, Map<String, Long> confirmedHistory,
                        Map<String, Long> recoveredHistory) {
        this.country = country;
        this.deathsHistory = deathsHistory;
        this.confirmedHistory = confirmedHistory;
        this.recoveredHistory = recoveredHistory;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Map<String, Long> getDeathsHistory() {
        return deathsHistory;
    }

    public void setDeathsHistory(Map<String, Long> deathsHistory) {
        this.deathsHistory = deathsHistory;
    }

    public Map<String, Long> getConfirmedHistory() {
        return confirmedHistory;
    }

    public void setConfirmedHistory(Map<String, Long> confirmedHistory) {
        this.confirmedHistory = confirmedHistory;
    }

    public Map<String, Long> getRecoveredHistory() {
        return recoveredHistory;
    }

    public void setRecoveredHistory(Map<String, Long> recoveredHistory) {
        this.recoveredHistory = recoveredHistory;
    }

    @Override
    public String toString() {
        return "CovidHistory{" +
                "country='" + country + '\'' +
                ", deathsHistory=" + deathsHistory +
                ", confirmedHistory=" + confirmedHistory +
                ", recoveredHistory=" + recoveredHistory +
                '}';
    }
}
