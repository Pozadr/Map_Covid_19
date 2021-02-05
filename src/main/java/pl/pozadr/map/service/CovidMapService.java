package pl.pozadr.map.service;

import pl.pozadr.map.dto.MapDto;

public interface CovidMapService {
    MapDto getMapDto();
    boolean filterPointsByCountry(String country);
    void filterPointsEurope();
    void setNotFoundMapDto();
}
