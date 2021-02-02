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
public class DataFetcher {
    Logger logger = LoggerFactory.getLogger(DataFetcher.class);
    private static final String BASE_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/";

    public Optional<String> getDataFromRemoteApi() {
        try {
            URL url = prepareUrl(1);
            return fetchData(url);
        } catch (MalformedURLException exUrl) {
            System.out.println(exUrl.getMessage());
            logger.error("Error during building URL.", exUrl);
        } catch (RestClientException exRestClient) {
            exRestClient.getMessage();
            logger.error("Error during fetching from remote API.", exRestClient);
            getDataFromRemoteApi(2);
        }
        return Optional.empty();
    }

    private Optional<String> getDataFromRemoteApi(Integer minusDays) {
        try {
            URL url = prepareUrl(minusDays);
            return fetchData(url);
        } catch (MalformedURLException exUrl) {
            System.out.println(exUrl.getMessage());
            logger.error("Error during building URL.", exUrl);
        } catch (RestClientException exRestClient) {
            exRestClient.getMessage();
            logger.error("Error during fetching from remote API.", exRestClient);
        }
        return Optional.empty();
    }

    private URL prepareUrl(Integer minusDays) throws MalformedURLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate today = LocalDate.now();
        LocalDate pastDay = today.minusDays(minusDays);
        URL url = new URL(BASE_URL + pastDay.format(formatter) + ".csv");
        return url;
    }

    private Optional<String> fetchData(URL url) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        Optional<String> dataOpt = Optional.ofNullable(restTemplate.getForObject(url.toString(), String.class));
        return dataOpt;
    }
}




