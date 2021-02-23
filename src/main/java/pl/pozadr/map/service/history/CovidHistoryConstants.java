package pl.pozadr.map.service.history;
/**
 * Constants for the CovidHistoryService.
 * Class is final to prevent it from being extended.
 * It can't be instantiated because of a private constructor.
 */
public final class CovidHistoryConstants {
    static final String HISTORIC_DEATHS_DATA_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
    static final String HISTORIC_CONFIRMED_DATA_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    static final String HISTORIC_RECOVERED_DATA_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";


    private CovidHistoryConstants() {
    }
}
