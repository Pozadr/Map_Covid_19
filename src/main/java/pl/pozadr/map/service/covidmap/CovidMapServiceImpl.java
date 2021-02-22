package pl.pozadr.map.service.covidmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pozadr.map.dto.MapDto;
import pl.pozadr.map.model.Point;
import pl.pozadr.map.reposiotry.euCapitals.CapitalsEuropeRepo;
import pl.pozadr.map.reposiotry.map.MapRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Communicates with the MapRepo and CapitalsEuropeRepo. Prepares the MapDto(DTO) used by the Controller.
 */
@Service
public class CovidMapServiceImpl implements CovidMapService {
    private final CapitalsEuropeRepo capitalsEuropeRepo;
    private final MapDto mapDto;
    private final MapRepo mapRepository;


    @Autowired
    public CovidMapServiceImpl(CapitalsEuropeRepo capitalsEuropeRepo, MapRepo mapRepository) {
        this.capitalsEuropeRepo = capitalsEuropeRepo;
        this.mapRepository = mapRepository;
        this.mapDto = new MapDto();
    }


    /**
     * @return - DTO used by Controller of application
     */
    @Override
    public MapDto getMapDto() {
        return mapDto;
    }

    /**
     * Sets default data for empty map.
     */
    @Override
    public void setNotFoundMapDto() {
        mapDto.setStartLat(52.2297700);
        mapDto.setStartLon(21.0117800);
        mapDto.setZoom(4);
        mapDto.setNotFoundMsg(true);
        mapDto.setPoints(new ArrayList<>());
    }

    /**
     * Filters points using country field of model Point.
     * Result is saved in MapDto.
     *
     * @param country - parameter used to filter data by country.
     * @return - status done/not done
     */
    @Override
    public boolean filterPointsByCountry(String country) {
        String validatedCountry = Validator.validateCountry(country);
        List<Point> filteredPoints = mapRepository.getMapPoints().stream()
                .filter(point -> point.getCountry().equalsIgnoreCase(validatedCountry))
                .collect(Collectors.toList());

        setMapDto(filteredPoints);
        return !filteredPoints.isEmpty();
    }

    /**
     * Filters points on map by european capitals list.
     * Result is saved in MapDto.
     */
    @Override
    public void filterPointsEurope() {
        List<String> euCountries = capitalsEuropeRepo.getEuropeanCapitals();
        List<Point> filteredPoints = mapRepository.getMapPoints().stream()
                .filter(point -> euCountries.stream()
                        .anyMatch(country -> country.equalsIgnoreCase(point.getCountry())))
                .collect(Collectors.toList());

        setMapDto(filteredPoints);
    }

    /**
     * Set parameters in MapDto(DTO).
     *
     * @param filteredPoints - list of Point(model) after previous validation.
     */
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

    /**
     * Calculates average lat coordinate used to display point on map.
     *
     * @param points - list of Point(model).
     * @return - calculated average.
     */
    private Double getAverageLat(List<Point> points) {
        Double sumLat = points.stream().mapToDouble(Point::getLat).sum();
        Integer sizeLat = points.size();
        return sumLat / sizeLat;
    }

    /**
     * Calculates average lon coordinate used to display point on map.
     *
     * @param points - list of Point(model).
     * @return - calculated average.
     */
    private Double getAverageLon(List<Point> points) {
        Double sumLon = points.stream().mapToDouble(Point::getLon).sum();
        Integer sizeLon = points.size();
        return sumLon / sizeLon;
    }

}
