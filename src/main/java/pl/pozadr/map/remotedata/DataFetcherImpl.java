package pl.pozadr.map.remotedata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * The service communicates with the remote API and retrieves the data using RestTemplate.
 */
@Service
public class DataFetcherImpl implements DataFetcher, HistoricDataFetcher {
    Logger logger = LoggerFactory.getLogger(DataFetcherImpl.class);
    private static final String DAILY_DATA_BASE_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/";

    /**
     * Return Optional which should contain daily data fetched from the remote API.
     *
     * @param daysBack - number of days in past.
     * @return an Optional with the data as a String if the specified value is non-null, otherwise an empty Optional.
     */
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

    /**
     * Return Optional which should contain historical data fetched from the remote API.
     *
     * @param urlString - URL for historical data.
     * @return an Optional with the data as a String if the specified value is non-null, otherwise an empty Optional.
     */
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

    /**
     * Prepares URL for connection to the remote API. Remote data is only available for days in the past.
     * The base URL must be parsed with the date.
     *
     * @param daysBack - days in the past to add to the url.
     * @return - URL - reference to a web resource that specifies its location.
     * @throws MalformedURLException - error occurs while parsing a String to a URL.
     */
    private URL prepareUrl(Integer daysBack) throws MalformedURLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate today = LocalDate.now();
        LocalDate pastDay = today.minusDays(daysBack);
        URL url = new URL(DAILY_DATA_BASE_URL + pastDay.format(formatter) + ".csv");
        return url;
    }

    /**
     * Fetch data from remote API.
     *
     * @param url - reference to a web resource that specifies its location.
     * @return an Optional with a present value if the specified value is non-null, otherwise an empty Optional.
     * @throws RestClientException - in case a request fails because of a server error response.
     */
    private Optional<String> fetchData(URL url) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        Optional<String> dataOpt = Optional.ofNullable(restTemplate.getForObject(url.toString(), String.class));
        return dataOpt;
    }
}