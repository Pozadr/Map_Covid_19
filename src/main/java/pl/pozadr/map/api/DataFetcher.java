package pl.pozadr.map.api;

import java.util.Optional;

public interface DataFetcher {
    Optional<String> getDataFromRemoteApi(Integer daysBack);
}
