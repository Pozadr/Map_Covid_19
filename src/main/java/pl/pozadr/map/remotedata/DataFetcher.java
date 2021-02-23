package pl.pozadr.map.remotedata;

import java.util.Optional;

public interface DataFetcher {
    Optional<String> getDataFromRemoteApi(Integer daysBack);
}
