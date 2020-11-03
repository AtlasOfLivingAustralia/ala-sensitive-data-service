package au.org.ala.sds.generalise;

import au.org.ala.sds.api.SensitivityInstance;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import org.gbif.dwc.terms.Term;

import java.util.*;

@ApiModel(
    description = "Generalise a latitude/longitude accordinng to the accuracy specified in a sensitivity instance."
)
public class LatLongGeneralisation extends Generalisation {
    /** The term to use for latitude */
    @JsonProperty
    @Getter
    private FieldAccessor latitudeField;
    /** The term to use for longitude */
    @Getter
    @JsonProperty
    private FieldAccessor longitudeField;

    protected LatLongGeneralisation() {
    }

    public LatLongGeneralisation(Term latitudeField, Term longitudeField) {
        this.latitudeField = new FieldAccessor(latitudeField);
        this.longitudeField = new FieldAccessor(longitudeField);
    }

    /**
     * Get a latitude/longitude and modify it according to the generalisation rule supplied.
     *
     * @param supplied The supplied values
     * @param original The store of original values (taken from supplied)
     * @param updated  To store of updated values
     * @param instance The instance of the rule being applied
     */
    @Override
    public void process(Map<String, String> supplied, Map<String, Object> original, Map<String, Object> updated, SensitivityInstance instance) {
        boolean hasLat = this.latitudeField.isSet(updated);
        boolean hasLong = this.longitudeField.isSet(updated);

        if (hasLat && hasLong)
            return;
        final FieldAccessor.Value<String> latitude = this.latitudeField.get(supplied);
        final FieldAccessor.Value<String> longitude = this.longitudeField.get(supplied);
        if (instance.isWithhold()) {
            latitude.set(Optional.empty(), original, updated);
            longitude.set(Optional.empty(), original, updated);
        } else {
            final Optional<Double> lat = latitude.getValue().map(v -> this.parseDouble(v));
            final Optional<Double> lon = longitude.getValue().map(v -> this.parseDouble(v));
            final Optional<Double> glat = lat.map(v -> instance.generaliseLatitude(v, lon.orElse(0.0)));
            final Optional<Double> glon = lon.map(v -> instance.generaliseLongitude(lat.orElse(0.0), v));
            if (!hasLat)
                latitude.set(glat.map(v -> instance.format(v)), original, updated);
            if (!hasLong)
                longitude.set(glon.map(v -> instance.format(v)), original, updated);
        }
    }


    /**
     * Get the fields that this generalisation uses to gather and modify information.
     *
     * @return The single field
     */
    @Override
    public Set<FieldAccessor> getFields() {
        HashSet<FieldAccessor> fields = new HashSet<>();
        fields.add(this.latitudeField);
        fields.add(this.longitudeField);
        return fields;
    }


    /**
     * Try to parse a double.
     *
     * @param v The double
     *
     * @return A resulting double or empty value if null or unparsable.
     */
    protected Double parseDouble(String v) {
        if (v == null || v.isEmpty())
            return null;
        try {
            return Double.parseDouble(v);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
