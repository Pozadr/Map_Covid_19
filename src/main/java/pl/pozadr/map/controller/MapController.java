package pl.pozadr.map.controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.pozadr.map.model.Point;
import pl.pozadr.map.service.MapService;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MapController {

    @Value("${api-token}")
    private String apiToken;
    private final MapService mapService;

    @Autowired
    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/map-home")
    public String getEuropeMap() {
        mapService.filterPointsEurope();
        return "redirect:/map";
    }

    @GetMapping("/map")
    public String getMap(Model model) {
        Double startLat = 52.26077325101084;
        Double startLon = 21.065969374131218;
        Integer zoom = 4;
        List<Point> points = mapService.getPoints();

        model.addAttribute("apiToken", apiToken);
        model.addAttribute("startLat", startLat);
        model.addAttribute("startLon", startLon);
        model.addAttribute("zoom", zoom);
        model.addAttribute("points", points);
        return "map";
    }

    @GetMapping("/get-map-by-country")
    public String getMapByCountry(@RequestParam String country) {
        mapService.filterPointsByCountry(country);
        return "redirect:/map";
    }




    }
