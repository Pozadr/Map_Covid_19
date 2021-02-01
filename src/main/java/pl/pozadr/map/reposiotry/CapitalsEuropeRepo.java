package pl.pozadr.map.reposiotry;

import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CapitalsEuropeRepo {
    private final List<CapitalEU> europeanCapitals;

    public CapitalsEuropeRepo() {
        this.europeanCapitals = Arrays.asList(CapitalEU.values());
    }

    public List<String> getEuropeanCapitals() {
        return europeanCapitals.stream().map(Enum::toString).collect(Collectors.toList());
    }
}
