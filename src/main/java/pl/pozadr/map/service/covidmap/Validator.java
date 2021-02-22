package pl.pozadr.map.service.covidmap;

/**
 * Validators for CovidMapService.
 * It can't be instantiated because of a private constructor.
 */
public class Validator {

    private Validator() {
    }

    /**
     * Validates input country variable from controller.
     *
     * @param country - parameter to validate.
     * @return - validated result.
     */
    static String validateCountry(String country) {
        if (country.equalsIgnoreCase("United States")) {
            return "us";
        } else if (country.equalsIgnoreCase("uk")) {
            return "United Kingdom";
        }

        return country;
    }
}
