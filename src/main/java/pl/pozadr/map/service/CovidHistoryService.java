package pl.pozadr.map.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pozadr.map.api.HistoricDataFetcher;
import pl.pozadr.map.model.CovidHistory;
import pl.pozadr.map.reposiotry.map.MapRepo;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

@Service
public class CovidHistoryService {
    Logger logger = LoggerFactory.getLogger(MapRepo.class);
    private final HistoricDataFetcher dataFetcher;
    private static final String HISTORIC_DEATHS_DATA_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
    private static final String HISTORIC_CONFIRMED_DATA_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static final String HISTORIC_RECOVERED_DATA_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";


    @Autowired
    public CovidHistoryService(HistoricDataFetcher dataFetcher) {
        this.dataFetcher = dataFetcher;
    }


    public CovidHistory getHistory(String country) {
        CovidHistory covidHistory = new CovidHistory();
        covidHistory.setCountry(country);

        Optional<String> deathsDataOpt = dataFetcher.getHistoricData(HISTORIC_DEATHS_DATA_URL);
        if (deathsDataOpt.isPresent()) {
            Optional<Map<String, Long>> deathsMapOpt = parseCsvToBean(deathsDataOpt.get(), country);
            deathsMapOpt.ifPresent(covidHistory::setDeathsHistory);
        }
        Optional<String> confirmedDataOpt = dataFetcher.getHistoricData(HISTORIC_CONFIRMED_DATA_URL);
        if (confirmedDataOpt.isPresent()) {
            Optional<Map<String, Long>> confirmedMapOpt = parseCsvToBean(confirmedDataOpt.get(), country);
            confirmedMapOpt.ifPresent(covidHistory::setConfirmedHistory);
        }
        Optional<String> recoveredDataOpt = dataFetcher.getHistoricData(HISTORIC_RECOVERED_DATA_URL);
        if (recoveredDataOpt.isPresent()) {
            Optional<Map<String, Long>> recoveredMapOpt = parseCsvToBean(recoveredDataOpt.get(), country);
            recoveredMapOpt.ifPresent(covidHistory::setRecoveredHistory);
        }

        return covidHistory;
    }

    private Optional<Map<String, Long>> parseCsvToBean(String dataCsv, String country) throws IllegalStateException {
        try {
            Reader reader = new StringReader(dataCsv);
            CSVReader csvReader = new CSVReader(reader);

            String[] record;
            record = csvReader.readNext();
            List<String> dates = new LinkedList<>(Arrays.asList(record).subList(4, record.length));

            Map<String, Long> data = new LinkedHashMap<>();
            while ((record = csvReader.readNext()) != null) {
                if (record[1].equals(country)) {
                    for (int i = 4; i < record.length; i++) {
                        data.put(dates.get(i - 4), Long.parseLong(record[i]));
                    }
                    break;
                }
            }
            csvReader.close();
            reader.close();
            return Optional.of(data);
        } catch (IOException ex) {
            logger.error("Error: IOException.");
            ex.printStackTrace();
        } catch (CsvValidationException e) {
            logger.error("Error: CsvValidationException.");
            e.printStackTrace();
        }
        return Optional.empty();
    }


}
