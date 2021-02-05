package pl.pozadr.map.reposiotry.euCapitals;

import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CapitalsEuropeRepoImpl implements CapitalsEuropeRepo {
    private final List<CapitalEU> europeanCapitals;

    public CapitalsEuropeRepoImpl() {
        this.europeanCapitals = Arrays.asList(CapitalEU.values());
    }


    public List<String> getEuropeanCapitals() {
        return europeanCapitals.stream().map(Enum::toString).collect(Collectors.toList());
    }
}
