package pl.pozadr.map.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.pozadr.map.model.Point;

@Controller
public class MapController {

    @Value("${api-token}")
    private String apiToken;

    @GetMapping("/map")
    public String getMap(Model model) {
        Point point = new Point(54.42566072931316, 18.588045314373087, "My comment");
        model.addAttribute("apiToken", apiToken);
        model.addAttribute("point", point);
        return "map";
    }
}
