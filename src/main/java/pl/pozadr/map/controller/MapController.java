package pl.pozadr.map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pozadr.map.dto.MapDto;
import pl.pozadr.map.model.CovidHistory;
import pl.pozadr.map.model.Point;
import pl.pozadr.map.service.CovidHistoryService;
import pl.pozadr.map.service.CovidMapService;

import java.util.List;
import java.util.Random;

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

    @GetMapping("/map-home")
    public String getHomePage() {
        return getEuropeMap();
    }

    @GetMapping("/get-history")
    public String getHistory(Model model, @RequestParam String country) {
        CovidHistory covidHistory = covidHistoryService.getHistory(country);
        model.addAttribute("country", covidHistory.getCountry());
        model.addAttribute("confirmed", covidHistory.getConfirmedHistory());
        model.addAttribute("recovered", covidHistory.getRecoveredHistory());
        model.addAttribute("deaths", covidHistory.getDeathsHistory());


        Random RANDOM = new Random(System.currentTimeMillis());
        List<List<Object>> list = List.of(
                List.of("1/29/21", RANDOM.nextInt(2000), RANDOM.nextInt(2000), RANDOM.nextInt(2000)),
                List.of("1/30/21", RANDOM.nextInt(2000), RANDOM.nextInt(2000), RANDOM.nextInt(2000)),
                List.of("1/31/21", RANDOM.nextInt(2000), RANDOM.nextInt(2000), RANDOM.nextInt(2000)),
                List.of("2/1/21", RANDOM.nextInt(2000), RANDOM.nextInt(2000), RANDOM.nextInt(2000)),
                List.of("2/2/21", RANDOM.nextInt(2000), RANDOM.nextInt(2000), RANDOM.nextInt(2000)),
                List.of("2/3/21", RANDOM.nextInt(2000), RANDOM.nextInt(2000), RANDOM.nextInt(2000))

        );

        model.addAttribute("chartData", list);
        return "history";
    }

    @GetMapping("/show-map")
    public String getMap(Model model) {
        MapDto mapDto = covidMapService.getMapDto();
        List<Point> points = mapDto.getPoints();
        Double startLat = mapDto.getStartLat();
        Double startLon = mapDto.getStartLon();
        Integer zoom = mapDto.getZoom();

        model.addAttribute("apiToken", apiToken);
        model.addAttribute("startLat", startLat);
        model.addAttribute("startLon", startLon);
        model.addAttribute("zoom", zoom);
        model.addAttribute("points", points);
        return "map";
    }

    @GetMapping("/map-europe")
    public String getEuropeMap() {
        covidMapService.filterPointsEurope();
        return "redirect:/show-map";
    }

    @GetMapping("/get-map-by-country")
    public String getMapByCountry(@RequestParam String country) {
        covidMapService.filterPointsByCountry(country);
        return "redirect:/show-map";
    }




    }
