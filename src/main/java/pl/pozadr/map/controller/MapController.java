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

    //@EventListener(ApplicationReadyEvent.class)
    public void getHistory() {
        CovidHistory covidHistory = covidHistoryService.getHistory("Poland");
        System.out.println(covidHistory.toString());
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
