package pl.pozadr.map.api;

import java.util.Optional;

public interface HistoricDataFetcher {
    Optional<String> getHistoricData(String url);

}
