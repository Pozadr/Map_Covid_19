package pl.pozadr.map.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.pozadr.map.model.Point;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MapService {
    private static final String DATA_CSV = "./data/mapData.csv";

    public List<Point> fetchData() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(DATA_CSV));
            CsvToBean<Point> csvToBean = new CsvToBeanBuilder<Point>(reader)
                    .withType(Point.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<Point> points = csvToBean.parse();
            reader.close();
            return points;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

}
