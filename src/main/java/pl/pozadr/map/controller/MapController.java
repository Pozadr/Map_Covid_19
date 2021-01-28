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
        Point centralPoint = new Point(54.35340380739709, 18.64126860060682, "Gdańsk");
        List<Point> points = mapService.fetchData();

        model.addAttribute("apiToken", apiToken);
        model.addAttribute("centralPoint", centralPoint);
        model.addAttribute("points", points);
        return "map";
    }
}
