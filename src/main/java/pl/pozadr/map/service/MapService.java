package pl.pozadr.map.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.pozadr.map.model.Point;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MapService {
    private List<Point> points;
    private static final String URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/01-26-2021.csv";


    public List<Point> getPoints() {
        return points;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initApplication() {
        points = fetchData();
        //points.forEach(point -> System.out.println(point.getDescription() + "\n"));
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
            filterData(points);
            return points;
        } catch (RestClientException ex) {
            ex.getMessage();
        }
        return new ArrayList<>();
    }

    private List<Point> filterData(List<Point> points) {
        points.forEach(point -> point.setDescription(preparePointDescription(point)));
        return points;
    }

    private String preparePointDescription(Point point) {
        double incidentRate = (point.getIncidentRate() == null) ? 0.0
                : Math.round(point.getIncidentRate() * 100.0) / 100.0;
        double caseFatalityRatio = (point.getCaseFatalityRatio() == null) ? 0.0
                : Math.round(point.getCaseFatalityRatio() * 100.0) / 100.0;

        return "Country: " + point.getCountryRegion() + "<br>" +
                "Region: " + point.getCombinedKey() + "<br>" +
                "Confirmed: " + point.getConfirmed() + "<br>" +
                "Deaths: " + point.getDeaths() + "<br>" +
                "Recovered: " + point.getRecovered() + "<br>" +
                "Deaths: " + point.getDeaths() + "<br>" +
                "Active: " + point.getActive() + "<br>" +
                "Incident rate: " + incidentRate + "<br>" +
                "Case fatality ratio: " + caseFatalityRatio;
    }



}
