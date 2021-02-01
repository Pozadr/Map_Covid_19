package pl.pozadr.map.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.pozadr.map.model.Point;
import pl.pozadr.map.reposiotry.CapitalsEuropeRepo;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MapService {
    private CapitalsEuropeRepo capitalsEuropeRepo;
    private List<Point> points;
    private static final String URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/01-26-2021.csv";

    @Autowired
    public MapService(CapitalsEuropeRepo capitalsEuropeRepo) {
        this.capitalsEuropeRepo = capitalsEuropeRepo;
    }

    public List<Point> getPoints() {
        return points;
    }

    public List<Point> getPointsByCountry(String country) {
        return points.stream()
                .filter(point -> point.getCountry().equalsIgnoreCase(country))
                .collect(Collectors.toList());
    }

    public List<Point> getPointsEurope() {
        List<String> euCountries = capitalsEuropeRepo.getEuropeanCapitals();
        return points.stream()
                .filter(point -> euCountries.stream()
                        .anyMatch(country -> country.equalsIgnoreCase(point.getCountry())))
                .collect(Collectors.toList());
    }


    @EventListener(ApplicationReadyEvent.class)
    public void initApplication() {
        points = fetchData();

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


    private List<Point> fetchData() {
        RestTemplate restTemplate = new RestTemplate();
        List<Point> points = new ArrayList<>();
        try {
            Optional<String> dataOpt = Optional.ofNullable(restTemplate.getForObject(URL, String.class));
            if (dataOpt.isPresent()) {
                StringReader reader = new StringReader(dataOpt.get());
                CsvToBean<Point> csvToBean = new CsvToBeanBuilder<Point>(reader)
                        .withType(Point.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();
                points = csvToBean.parse();
                reader.close();
            }
            List<Point> filteredPoints = filterData(points);
            return filteredPoints;
        } catch (RestClientException ex) {
            ex.getMessage();
        }
        return new ArrayList<>();
    }

    private List<Point> filterData(List<Point> points) {
        List<Point> pointsWithoutNullLenLon = points.stream()
                .filter(point -> point.getLat() != null)
                .filter(point -> point.getLon() != null)
                .collect(Collectors.toList());
        pointsWithoutNullLenLon.forEach(point -> point.setDescription(preparePointDescription(point)));
        return pointsWithoutNullLenLon;
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
