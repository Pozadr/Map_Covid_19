package pl.pozadr.map.reposiotry.map;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import pl.pozadr.map.api.DataFetcher;
import pl.pozadr.map.model.Point;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MapRepoImpl implements MapRepo {
    Logger logger = LoggerFactory.getLogger(MapRepoImpl.class);
    private List<Point> mapPoints;
    private final DataFetcher dataFetcher;

    @Autowired
    public MapRepoImpl(DataFetcher dataFetcher) {
        this.dataFetcher = dataFetcher;
    }

    @Override
    public List<Point> getMapPoints() {
        return mapPoints;
    }


    private List<Point> filterData(List<Point> points) {
        List<Point> pointsWithoutNullLenLon = points.stream()
                .filter(point -> point.getLat() != null)
                .filter(point -> point.getLon() != null)
                .collect(Collectors.toList());
        pointsWithoutNullLenLon.forEach(point -> point.setDescription(createPointDescription(point)));
        return pointsWithoutNullLenLon;
    }

    private String createPointDescription(Point point) {
        double incidentRate = (point.getIncidentRate() == null) ? 0.0
                : Math.round(point.getIncidentRate() * 100.0) / 100.0;
        double caseFatalityRatio = (point.getCaseFatalityRatio() == null) ? 0.0
                : Math.round(point.getCaseFatalityRatio() * 100.0) / 100.0;

        StringBuilder descriptionSb = new StringBuilder();
        descriptionSb.append("<b>Country:</b> ").append(point.getCountry()).append("<br>");
        descriptionSb.append("<b>Region:</b> ").append(point.getRegion()).append("<br>");
        descriptionSb.append("<b>Confirmed:</b> ").append(point.getConfirmed()).append("<br>");
        descriptionSb.append("<b>Active:</b> ").append(point.getActive()).append("<br>");
        descriptionSb.append("<b>Recovered:</b> ").append(point.getRecovered()).append("<br>");
        descriptionSb.append("<b>Deaths:</b> ").append(point.getDeaths()).append("<br>");
        descriptionSb.append("<b>Incident rate:</b> ").append(incidentRate).append("<br>");
        descriptionSb.append("<b>Case fatality ratio:</b> ").append(caseFatalityRatio);
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

    @Scheduled(fixedDelay = 86400000) // 86400000 ms = 24 h
    private void updateData() {
        Optional<String> dataOpt = dataFetcher.getDataFromRemoteApi(1);
        if (dataOpt.isEmpty()) {
            dataOpt = dataFetcher.getDataFromRemoteApi(2);
        }

        if (dataOpt.isPresent()) {
            try {
                List<Point> points = parseCsvToBean(dataOpt.get());
                mapPoints = filterData(points);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                logger.info(LocalDateTime.now().format(formatter) + " | Data updated.");
            } catch (IllegalStateException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
