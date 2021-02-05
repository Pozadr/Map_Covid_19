package pl.pozadr.map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pozadr.map.dto.MapDto;
import pl.pozadr.map.model.Point;
import pl.pozadr.map.reposiotry.euCapitals.CapitalsEuropeRepo;
import pl.pozadr.map.reposiotry.map.MapRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CovidMapService {
    private final CapitalsEuropeRepo capitalsEuropeRepo;
    private final MapDto mapDto;
    private final MapRepo mapRepository;


    @Autowired
    public CovidMapService(CapitalsEuropeRepo capitalsEuropeRepo, MapRepo mapRepository) {
        this.capitalsEuropeRepo = capitalsEuropeRepo;
        this.mapRepository = mapRepository;
        this.mapDto = new MapDto();
    }


    public MapDto getMapDto() {
        return mapDto;
    }

    public void setNotFoundMapDto() {
        mapDto.setStartLat(52.2297700);
        mapDto.setStartLon(21.0117800);
        mapDto.setZoom(4);
        mapDto.setNotFoundMsg(true);
        mapDto.setPoints(new ArrayList<>());
    }

    public boolean filterPointsByCountry(String country) {
        String validatedCountry = validateCountry(country);
        List<Point> filteredPoints = mapRepository.getMapPoints().stream()
                .filter(point -> point.getCountry().equalsIgnoreCase(validatedCountry))
                .collect(Collectors.toList());

        setMapDto(filteredPoints);
        return  !filteredPoints.isEmpty();
    }

    public void filterPointsEurope() {
        List<String> euCountries = capitalsEuropeRepo.getEuropeanCapitals();
        List<Point> filteredPoints = mapRepository.getMapPoints().stream()
                .filter(point -> euCountries.stream()
                        .anyMatch(country -> country.equalsIgnoreCase(point.getCountry())))
                .collect(Collectors.toList());

        setMapDto(filteredPoints);
    }

    private void setMapDto(List<Point> filteredPoints) {
        Double startLat = getAverageLat(filteredPoints);
        Double startLon = getAverageLon(filteredPoints);
        Integer zoom = 4;

        mapDto.setPoints(filteredPoints);
        mapDto.setStartLat(startLat);
        mapDto.setStartLon(startLon);
        mapDto.setZoom(zoom);
        mapDto.setNotFoundMsg(false);
    }

    private Double getAverageLat(List<Point> points) {
        Double sumLat = points.stream().mapToDouble(Point::getLat).sum();
        Integer sizeLat = points.size();
        return sumLat / sizeLat;
    }

    private Double getAverageLon(List<Point> points) {
        Double sumLon = points.stream().mapToDouble(Point::getLon).sum();
        Integer sizeLon = points.size();
        return sumLon / sizeLon;
    }

    private String validateCountry(String country) {
        if (country.equalsIgnoreCase("United States")) {
            return "us";
        } else if (country.equalsIgnoreCase("uk")) {
            return "United Kingdom";
        }

        return country;
    }


}
