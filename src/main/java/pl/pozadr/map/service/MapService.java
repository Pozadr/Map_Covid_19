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

    public List<Point> getPoints() {
        return mapDto.getPoints();
    }

    public void filterPointsByCountry(String country) {
        Double startLat = 52.26077325101084;
        Double startLon = 21.065969374131218;
        Integer zoom = 4;
        String validatedCountry = validateCountry(country);

        List<Point> filteredPoints = remoteApiPoints.stream()
                .filter(point -> point.getCountry().equalsIgnoreCase(validatedCountry))
                .collect(Collectors.toList());

        mapDto.setPoints(filteredPoints);
        mapDto.setStartLat(startLat);
        mapDto.setStartLon(startLon);
        mapDto.setZoom(zoom);
    }

    public void filterPointsEurope() {
        Double startLat = 52.26077325101084;
        Double startLon = 21.065969374131218;
        Integer zoom = 4;
        List<String> euCountries = capitalsEuropeRepo.getEuropeanCapitals();

        List<Point> filteredPoints = remoteApiPoints.stream()
                .filter(point -> euCountries.stream()
                        .anyMatch(country -> country.equalsIgnoreCase(point.getCountry())))
                .collect(Collectors.toList());

        mapDto.setPoints(filteredPoints);
        mapDto.setStartLat(startLat);
        mapDto.setStartLon(startLon);
        mapDto.setZoom(zoom);
    }

    @Scheduled(fixedDelay = 86400000) // 86400000 ms = 24 h
    private void updateData() {
        Optional<String> dataOpt = dataFetcher.getDataFromRemoteApi();
        if (dataOpt.isPresent()) {
            try {
                List<Point> points = csvToBeanParser(dataOpt.get());
                remoteApiPoints = filterData(points);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                logger.info(LocalDateTime.now().format(formatter) + " | Data updated.");
            } catch (IllegalStateException ex) {
                System.out.println(ex.getMessage());
            }
        }

        // TEST
        /*
        List<Point> common = getPointsEurope();

        System.out.println("TEST");
        System.out.println("points size: " + points.size());
        System.out.println("common size: " + common.size());
        common.forEach(point -> {
            System.out.println(point.getCountry() + "\n");
        });

        points.forEach(point -> {
            System.out.println(point.getCountry() + "\n");
            System.out.println("Lat:" + point.getLat() + "\n");
            System.out.println("Lon:" + point.getLon() + "\n");
        });


        points.forEach(point -> {
            System.out.println(point.getDescription() + "\n");
        });
         */
    }

    private List<Point> csvToBeanParser(String dataCsv) throws IllegalStateException {
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
        pointsWithoutNullLenLon.forEach(point -> point.setDescription(preparePointDescription(point)));
        return pointsWithoutNullLenLon;
    }

    private String validateCountry(String country) {
        if (country.equalsIgnoreCase("United States")) {
            return "us";
        }
        return country;
    }

    private String preparePointDescription(Point point) {
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
}
