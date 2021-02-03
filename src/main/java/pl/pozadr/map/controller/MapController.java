package pl.pozadr.map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pozadr.map.dto.MapDto;
import pl.pozadr.map.model.Point;
import pl.pozadr.map.service.MapService;

import java.util.List;

@Controller
public class MapController {

    @Value("${api-token}")
    private String apiToken;
    private final MapService mapService;

    @Autowired
    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/map")
    public String initApplication() {
        return getEuropeMap();
    }

    @GetMapping("/show-map")
    public String getMap(Model model) {
        MapDto mapDto = mapService.getMapDto();
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
        mapService.filterPointsEurope();
        return "redirect:/show-map";
    }

    @GetMapping("/get-map-by-country")
    public String getMapByCountry(@RequestParam String country) {
        mapService.filterPointsByCountry(country);
        return "redirect:/show-map";
    }




    }
