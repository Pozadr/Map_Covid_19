package pl.pozadr.map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pozadr.map.dto.HistoryChartDto;
import pl.pozadr.map.dto.MapDto;
import pl.pozadr.map.model.Point;
import pl.pozadr.map.service.CovidHistoryService;
import pl.pozadr.map.service.CovidMapService;

import java.util.List;
import java.util.Optional;

/**
 * Takes user input and displays a response from the application API.
 */
@Controller
public class MapController {

    @Value("${api-token}")
    private String apiToken;
    private final CovidMapService covidMapService;
    private final CovidHistoryService covidHistoryService;

    @Autowired
    public MapController(CovidMapService covidMapService, CovidHistoryService covidHistoryService) {
        this.covidMapService = covidMapService;
        this.covidHistoryService = covidHistoryService;
    }


    @GetMapping("/map")
    public String initApplication() {
        return getEuropeMap();
    }

    @GetMapping("/show-map")
    public String getMap(Model model) {
        MapDto mapDto = covidMapService.getMapDto();
        List<Point> points = mapDto.getPoints();
        Double startLat = mapDto.getStartLat();
        Double startLon = mapDto.getStartLon();
        Integer zoom = mapDto.getZoom();
        boolean notFoundMsg = mapDto.isNotFoundMsg();

        model.addAttribute("apiToken", apiToken);
        model.addAttribute("startLat", startLat);
        model.addAttribute("startLon", startLon);
        model.addAttribute("zoom", zoom);
        model.addAttribute("points", points);
        model.addAttribute("notFoundMsg", notFoundMsg);
        return "map";
    }
    @GetMapping("/get-history")
    public String getHistory(Model model, @RequestParam String country) {
        Optional<HistoryChartDto> historyChartDtoOpt = covidHistoryService.getHistoryChartDto(country);
        if (historyChartDtoOpt.isPresent()) {
            HistoryChartDto historyChartDto = historyChartDtoOpt.get();
            List<List<Object>> chartData = historyChartDto.getGoogleChartsData();
            model.addAttribute("chartData", chartData);
            model.addAttribute("country", historyChartDto.getCountry());
            return "history";
        }
        return "redirect:/city-not-found";
    }

    @GetMapping("/get-map-europe")
    public String getEuropeMap() {
        covidMapService.filterPointsEurope();
        return "redirect:/show-map";
    }

    @GetMapping("/get-map-by-country")
    public String getMapByCountry(@RequestParam String country) {
        return (covidMapService.filterPointsByCountry(country)) ? "redirect:/show-map" : "redirect:/city-not-found";
    }

    @GetMapping("/map-home")
    public String getHomePage() {
        return getEuropeMap();
    }

    @GetMapping("/city-not-found")
    public String getCityNotFound(Model model) {
        covidMapService.setNotFoundMapDto();
        return "redirect:/show-map";
    }
}
