package pl.pozadr.map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String getMap(Model model) {
        Double startLat = 52.26077325101084;
        Double startLon = 21.065969374131218;
        Integer zoom = 4;
        List<Point> points = mapService.getPoints();
        System.out.println(points.size());

        model.addAttribute("apiToken", apiToken);
        model.addAttribute("startLat", startLat);
        model.addAttribute("startLon", startLon);
        model.addAttribute("zoom", zoom);
        model.addAttribute("points", points);
        return "map";
    }
}
