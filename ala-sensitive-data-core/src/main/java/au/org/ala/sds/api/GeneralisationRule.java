package au.org.ala.sds.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A generalisation to apply to a location.
 */
public class GeneralisationRule {
    /** Withold a result entirely */
    protected static final String WITHHOLD = "WITHHOLD";
    /** Parsing pattern for generalisation specs. */
    protected static final Pattern GENERALISATION_PATTERN = Pattern.compile("\\s*(\\d+)\\s*(m|km)\\s*");
    /** The distance of one degree of latitude */
    protected static final double DEGREE_TO_METRES = 111132.9;

    @ApiModelProperty(
        value = "The generalisation to apply. A string giving a measurable distance or precision Units recognised are metres (m) or kilometres (km) or WITHHOLD to withhold entirely",
        required = true,
        example = "10km"
    )
    @Getter
    @JsonProperty
    private String generalisation;
    /** Withhold results entirely */
    @Getter
    @JsonIgnore
    private boolean withhold;
    /** The actual generalisation to apply in metres */
    @Getter
    @JsonIgnore
    private int generalisationInMetres;
    /** The precision to apply to coordinates */
    @Getter
    @JsonIgnore
    private double precision;
    /** The precision format to use */
    @Getter
    @JsonIgnore
    private NumberFormat format;

    @JsonCreator
    public GeneralisationRule(@JsonProperty("generalisation") String generalisation) {
        this.generalisation = generalisation;
        this.withhold = this.generalisation.equalsIgnoreCase(WITHHOLD);
        this.generalisationInMetres = this.parseGeneralisation(this.generalisation);
        this.precision = this.buildPrecision(this.generalisationInMetres);
        this.format = this.buildFormat(this.generalisationInMetres);
    }

    /**
     * Parse the string generalisation into a value in metres.
     *
     * @param generalisation The generalisation string.
     *
     * @return The generalisation in metres
     */
    protected int parseGeneralisation(String generalisation) {
        if (generalisation.equalsIgnoreCase(WITHHOLD))
            return 1000000;
        Matcher matcher = GENERALISATION_PATTERN.matcher(generalisation);
        if (!matcher.matches())
            throw new IllegalArgumentException("Invalid generalisation " + generalisation);
        int gen = Integer.parseInt(matcher.group(1));
        if (matcher.group(2).equals("km"))
            gen *= 1000;
        return gen;
    }

    /**
     * Build an appropriate precision for coordinates.
     *
     * @param generalisationInMetres The generalisation to apply, in metres.
     *
     * @return The equivalent lat/long precision.
     */
    protected double buildPrecision(int generalisationInMetres) {
        if (generalisationInMetres <= 1000)
            return 0.01;
        if (generalisationInMetres <= 10000)
            return 0.1;
        return 1;
    }


    /**
     * Build an appropriate precision for coordinates.
     *
     * @param generalisationInMetres The generalisation to apply, in metres.
     *
     * @return The equivalent lat/long precision.
     */
    protected NumberFormat buildFormat(int generalisationInMetres) {
        if (generalisationInMetres <= 1000)
            return new DecimalFormat("0.00");
        if (generalisationInMetres <= 10000)
            return new DecimalFormat("0.0");
        return new DecimalFormat("0");
    }

    /**
     * Generalise a latitude to the required distance and round it to an appropriate precision.
     *
     * @param latitude The latitude value
     * @param longitude The longitude value
     *
     * @return Thw generalised latitude
     */
    public double generaliseLatitude(double latitude, double longitude) {
        latitude = Math.round(latitude * DEGREE_TO_METRES / this.generalisationInMetres) * this.generalisationInMetres / DEGREE_TO_METRES;
        latitude = Math.round(latitude / this.precision) * this.precision;
        return latitude;
    }

    /**
     * Generalise a longitude to the required distance and round it to an appropriate precision.
     * <p>
     * Note that, as one gets closer to the poles, the generalisation in degrees increases for
     * increasing generalisation in kilometers.
     * </p>
     *
     * @param latitude The latitude value
     * @param longitude The longitude value
     *
     * @return Thw generalised latitude
     */
    public double generaliseLongitude(double latitude, double longitude) {
        if (latitude >= -89.9 && latitude <= 89.9) {
            double conversion = DEGREE_TO_METRES * Math.cos(latitude * Math.PI / 180.0);
            longitude = Math.round(longitude * conversion / this.generalisationInMetres) * this.generalisationInMetres / conversion;
        }
        longitude = Math.round(longitude / this.precision) * this.precision;
        return longitude;
    }

    /**
     * Format a lat/long to the precision specified.
     *
     * @param v The value
     *
     * @return The value, appropriately formatted
     */
    public String format(double v) {
        return this.format.format(v);
    }

}
