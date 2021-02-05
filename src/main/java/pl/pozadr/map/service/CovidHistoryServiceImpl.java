package pl.pozadr.map.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pozadr.map.api.HistoricDataFetcher;
import pl.pozadr.map.dto.HistoryChartDto;
import pl.pozadr.map.model.CovidHistory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

@Service
public class CovidHistoryServiceImpl implements CovidHistoryService {
    Logger logger = LoggerFactory.getLogger(CovidHistoryServiceImpl.class);
    private final HistoricDataFetcher dataFetcher;
    private static final String HISTORIC_DEATHS_DATA_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
    private static final String HISTORIC_CONFIRMED_DATA_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static final String HISTORIC_RECOVERED_DATA_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";


    @Autowired
    public CovidHistoryServiceImpl(HistoricDataFetcher dataFetcher) {
        this.dataFetcher = dataFetcher;
    }

    @Override
    public Optional<HistoryChartDto> getHistoryChartDto(String country) {
        HistoryChartDto historyChartDto = new HistoryChartDto();

        Optional<CovidHistory> covidHistoryOpt = getCovidHistory(country);
        if (covidHistoryOpt.isEmpty()) {
            return Optional.empty();
        }
        CovidHistory covidHistory = covidHistoryOpt.get();
        Map<String, Long> confirmedHistory = covidHistory.getConfirmedHistory();
        Map<String, Long> recoveredHistory = covidHistory.getRecoveredHistory();
        Map<String, Long> deathsHistory = covidHistory.getDeathsHistory();
        Set<String> dates = covidHistory.getConfirmedHistory().keySet();

        List<List<Object>> googleChartsData = new LinkedList<>();
        dates.forEach(date -> {
            googleChartsData.add(List.of(date, confirmedHistory.get(date), recoveredHistory.get(date),
                    deathsHistory.get(date)));
        });

        historyChartDto.setGoogleChartsData(googleChartsData);
        historyChartDto.setCountry(covidHistory.getCountry());

        return Optional.of(historyChartDto);
    }

    private Optional<CovidHistory> getCovidHistory(String country) {
        CovidHistory covidHistory = new CovidHistory();

        Optional<String> deathsDataOpt = dataFetcher.getHistoricData(HISTORIC_DEATHS_DATA_URL);
        if (deathsDataOpt.isPresent()) {
            Map<String, Long> deathsMap = parseCsvToMap(deathsDataOpt.get(), country);
            if (deathsMap.isEmpty()) {
                return Optional.empty();
            } else {
                covidHistory.setDeathsHistory(deathsMap);
            }
        }
        Optional<String> confirmedDataOpt = dataFetcher.getHistoricData(HISTORIC_CONFIRMED_DATA_URL);
        if (confirmedDataOpt.isPresent()) {
            Map<String, Long> confirmedMap = parseCsvToMap(confirmedDataOpt.get(), country);
            if (confirmedMap.isEmpty()) {
                return Optional.empty();
            } else {
                covidHistory.setConfirmedHistory(confirmedMap);
            }
        }
        Optional<String> recoveredDataOpt = dataFetcher.getHistoricData(HISTORIC_RECOVERED_DATA_URL);
        if (recoveredDataOpt.isPresent()) {
            Map<String, Long> recoveredMap = parseCsvToMap(recoveredDataOpt.get(), country);
            if (recoveredMap.isEmpty()) {
                return Optional.empty();
            } else {
                covidHistory.setRecoveredHistory(recoveredMap);
            }
        }
        String countryUpperFirstLetter = country.substring(0, 1).toUpperCase() + country.substring(1);
        covidHistory.setCountry(countryUpperFirstLetter);
        return Optional.of(covidHistory);
    }

    private Map<String, Long> parseCsvToMap(String dataCsv, String country) {
        Map<String, Long> data = new LinkedHashMap<>();
        try {
            Reader reader = new StringReader(dataCsv);
            CSVReader csvReader = new CSVReader(reader);

            String[] record;
            record = csvReader.readNext();
            List<String> dates = new LinkedList<>(Arrays.asList(record).subList(4, record.length));

            while ((record = csvReader.readNext()) != null) {
                if (record[1].equalsIgnoreCase(country)) {
                    for (int i = 4; i < record.length; i++) {
                        data.put(dates.get(i - 4), Long.parseLong(record[i]));
                    }
                    break;
                }
            }
            csvReader.close();
            reader.close();
        } catch (IOException ex) {
            logger.error("Error: IOException. {}", ex.getMessage());
            ex.printStackTrace();
        } catch (CsvValidationException e) {
            logger.error("Error: CsvValidationException. {}", e.getMessage());
            e.printStackTrace();
        }
        return data;
    }
}
