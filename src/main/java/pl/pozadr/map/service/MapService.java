package pl.pozadr.map.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.pozadr.map.api.DataFetcher;
import pl.pozadr.map.dto.MapDto;
import pl.pozadr.map.model.Point;
import pl.pozadr.map.reposiotry.CapitalsEuropeRepo;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MapService {
    Logger logger = LoggerFactory.getLogger(MapService.class);
    private final CapitalsEuropeRepo capitalsEuropeRepo;
    private final DataFetcher dataFetcher;
    private List<Point> remoteApiPoints;
    private final MapDto mapDto;


    @Autowired
    public MapService(CapitalsEuropeRepo capitalsEuropeRepo, DataFetcher dataFetcher) {
        this.capitalsEuropeRepo = capitalsEuropeRepo;
        this.dataFetcher = dataFetcher;
        this.mapDto = new MapDto();
    }

    public MapDto getMapDto() {
        return mapDto;
    }

    public void filterPointsByCountry(String country) {
        String validatedCountry = validateCountry(country);
        List<Point> filteredPoints = remoteApiPoints.stream()
                .filter(point -> point.getCountry().equalsIgnoreCase(validatedCountry))
                .collect(Collectors.toList());

        setDto(filteredPoints);
    }

    public void filterPointsEurope() {
        List<String> euCountries = capitalsEuropeRepo.getEuropeanCapitals();
        List<Point> filteredPoints = remoteApiPoints.stream()
                .filter(point -> euCountries.stream()
                        .anyMatch(country -> country.equalsIgnoreCase(point.getCountry())))
                .collect(Collectors.toList());

        setDto(filteredPoints);
    }

    private void setDto(List<Point> filteredPoints) {
        Double startLat = getAverageLat(filteredPoints);
        Double startLon = getAverageLon(filteredPoints);
        Integer zoom = 4;

        mapDto.setPoints(filteredPoints);
        mapDto.setStartLat(startLat);
        mapDto.setStartLon(startLon);
        mapDto.setZoom(zoom);
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
        }
        return country;
    }

    private String createPointDescription(Point point) {
        double incidentRate = (point.getIncidentRate() == null) ? 0.0
                : Math.round(point.getIncidentRate() * 100.0) / 100.0;
        double caseFatalityRatio = (point.getCaseFatalityRatio() == null) ? 0.0
                : Math.round(point.getCaseFatalityRatio() * 100.0) / 100.0;

        StringBuilder descriptionSb = new StringBuilder();
        descriptionSb.append("Country: ").append(point.getCountry()).append("<br>");
        descriptionSb.append("Region: ").append(point.getRegion()).append("<br>");
        descriptionSb.append("Confirmed: ").append(point.getConfirmed()).append("<br>");
        descriptionSb.append("Active: ").append(point.getActive()).append("<br>");
        descriptionSb.append("Recovered: ").append(point.getRecovered()).append("<br>");
        descriptionSb.append("Deaths: ").append(point.getDeaths()).append("<br>");
        descriptionSb.append("Incident rate: ").append(incidentRate).append("<br>");
        descriptionSb.append("Case fatality ratio: ").append(caseFatalityRatio);
        return descriptionSb.toString();
    }

    private List<Point> parseCsvToBean(String dataCsv) throws IllegalStateException {
        StringReader reader = new StringReader(dataCsv);
        CsvToBean<Point> csvToBean = new CsvToBeanBuilder<Point>(reader)
                .withType(Point.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<Point> points = csvToBean.parse();
        reader.close();
        return points;
    }

    private List<Point> filterData(List<Point> points) {
        List<Point> pointsWithoutNullLenLon = points.stream()
                .filter(point -> point.getLat() != null)
                .filter(point -> point.getLon() != null)
                .collect(Collectors.toList());
        pointsWithoutNullLenLon.forEach(point -> point.setDescription(createPointDescription(point)));
        return pointsWithoutNullLenLon;
    }

    @Scheduled(fixedDelay = 86400000) // 86400000 ms = 24 h
    private void updateData() {
        Optional<String> dataOpt = dataFetcher.getDataFromRemoteApi(1);
        if (dataOpt.isEmpty()) {
            dataOpt = dataFetcher.getDataFromRemoteApi(2);
        }

        if (dataOpt.isPresent()) {
            try {
                List<Point> points = parseCsvToBean(dataOpt.get());
                remoteApiPoints = filterData(points);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                logger.info(LocalDateTime.now().format(formatter) + " | Data updated.");
            } catch (IllegalStateException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
