package pl.pozadr.map.service.history;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pozadr.map.remotedata.HistoricDataFetcher;
import pl.pozadr.map.dto.HistoryChartDto;
import pl.pozadr.map.model.CovidHistory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

/**
 * Communicates with the dataFetcher and prepares the data used by the Controller.
 */
@Service
public class CovidHistoryServiceImpl implements CovidHistoryService {
    Logger logger = LoggerFactory.getLogger(CovidHistoryServiceImpl.class);
    private final HistoricDataFetcher dataFetcher;


    @Autowired
    public CovidHistoryServiceImpl(HistoricDataFetcher dataFetcher) {
        this.dataFetcher = dataFetcher;
    }


    /**
     * Prepares a HistoryChartDto(DTO) with the data format required by Google Charts used on frontend of the application.
     *
     * @param country - parameter used to filter data by country.
     * @return - an Optional with the data as a HistoryChartDto(DTO) if the specified value is non-null,
     * otherwise an empty Optional.
     */
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

    /**
     * Prepares a CovidHistory model. Uses dataFetcher to retrieve data from a remote API.
     * Uses parseCsvToMap to filter data.
     *
     * @param country - parameter used to filter data by country.
     * @return - an Optional with the data as a CovidHistory(model) if the specified value is non-null,
     * otherwise an empty Optional.
     */
    private Optional<CovidHistory> getCovidHistory(String country) {
        CovidHistory covidHistory = new CovidHistory();

        Optional<String> deathsDataOpt = dataFetcher.getHistoricData(CovidHistoryConstants.HISTORIC_DEATHS_DATA_URL);
        if (deathsDataOpt.isPresent()) {
            Map<String, Long> deathsMap = parseCsvToMap(deathsDataOpt.get(), country);
            if (deathsMap.isEmpty()) {
                return Optional.empty();
            } else {
                covidHistory.setDeathsHistory(deathsMap);
            }
        }
        Optional<String> confirmedDataOpt = dataFetcher.getHistoricData(CovidHistoryConstants.HISTORIC_CONFIRMED_DATA_URL);
        if (confirmedDataOpt.isPresent()) {
            Map<String, Long> confirmedMap = parseCsvToMap(confirmedDataOpt.get(), country);
            if (confirmedMap.isEmpty()) {
                return Optional.empty();
            } else {
                covidHistory.setConfirmedHistory(confirmedMap);
            }
        }
        Optional<String> recoveredDataOpt = dataFetcher.getHistoricData(CovidHistoryConstants.HISTORIC_RECOVERED_DATA_URL);
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

    /**
     * Parse data from .csv file to Map<String, Long> used in CovidHistory(model).
     *
     * @param dataCsv - input data as String from .csv file.
     * @param country - parameter used to filter data by country.
     * @return - Map<String, Long>, which is the field of CovidHistory(model).
     */
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
