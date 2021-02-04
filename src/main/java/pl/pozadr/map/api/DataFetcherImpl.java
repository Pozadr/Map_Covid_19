package pl.pozadr.map.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class DataFetcherImpl implements DataFetcher, HistoricDataFetcher {
    Logger logger = LoggerFactory.getLogger(DataFetcherImpl.class);
    private static final String DAILY_DATA_BASE_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/";

    @Override
    public Optional<String> getDataFromRemoteApi(Integer daysBack) {
        try {
            URL url = prepareUrl(daysBack);
            return fetchData(url);
        } catch (MalformedURLException exUrl) {
            System.out.println(exUrl.getMessage());
            logger.error("Error during building URL.", exUrl);
        } catch (RestClientException exRestClient) {
            exRestClient.getMessage();
            logger.error("HttpClientErrorException$NotFound: Error during fetching from remote API for "
                    + daysBack + " days back.");
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getHistoricData(String urlString) {
        try {
            URL url = new URL(urlString);
            return fetchData(url);
        } catch (MalformedURLException exUrl) {
            System.out.println(exUrl.getMessage());
            logger.error("Error during building URL.", exUrl);
        } catch (RestClientException exRestClient) {
            exRestClient.getMessage();
            logger.error("Error during fetching data from remote API.\n Url: {}", urlString);
        }
        return Optional.empty();
    }

    private URL prepareUrl(Integer daysBack) throws MalformedURLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate today = LocalDate.now();
        LocalDate pastDay = today.minusDays(daysBack);
        URL url = new URL(DAILY_DATA_BASE_URL + pastDay.format(formatter) + ".csv");
        return url;
    }

    private Optional<String> fetchData(URL url) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        Optional<String> dataOpt = Optional.ofNullable(restTemplate.getForObject(url.toString(), String.class));
        return dataOpt;
    }
}




