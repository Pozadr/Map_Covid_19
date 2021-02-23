package pl.pozadr.map.service;

/**
 * Validators for CovidMapService.
 * It can't be instantiated because of a private constructor.
 */
public class CountryValidator {

    private CountryValidator() {
    }

    /**
     * Validates input country variable from controller.
     *
     * @param country - parameter to validate.
     * @return - validated result.
     */
    public static String validateCountry(String country) {
        if (country.equalsIgnoreCase("United States")) {
            return "us";
        } else if (country.equalsIgnoreCase("uk")) {
            return "United Kingdom";
        }

        return country;
    }
}
