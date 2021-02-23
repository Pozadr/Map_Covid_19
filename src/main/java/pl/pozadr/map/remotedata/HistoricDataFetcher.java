package pl.pozadr.map.remotedata;

import java.util.Optional;

public interface HistoricDataFetcher {
    Optional<String> getHistoricData(String url);

}
