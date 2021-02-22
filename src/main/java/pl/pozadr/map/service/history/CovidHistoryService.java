package pl.pozadr.map.service.history;

import pl.pozadr.map.dto.HistoryChartDto;

import java.util.Optional;

public interface CovidHistoryService {
    Optional<HistoryChartDto> getHistoryChartDto(String country);
}
