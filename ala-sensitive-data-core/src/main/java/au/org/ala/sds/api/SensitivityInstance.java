package au.org.ala.sds.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(builder = SensitivityInstance.SensitivityInstanceBuilder.class)
@Value
@Builder
@EqualsAndHashCode
@ApiModel(
    description = "A specific sensitivity rule applying to a taxon in a particular zone."
)
public class SensitivityInstance {
    @ApiModelProperty(
        value = "The type of sensitivity this instance manages",
        required = true
    )
    @JsonProperty
    private SensitivityType type;
    @ApiModelProperty(
        value = "The sensitivity category"
    )
    @JsonProperty
    private SensitivityCategory category;
    @ApiModelProperty(
        value = "The name/acronym of the authority responsible for designating this instance."
    )
    @JsonProperty
    private String authority;
    @ApiModelProperty(
        value = "The data resource that holds the sensitivity information"
    )
    @JsonProperty
    private String dataResourceId;
    @ApiModelProperty(
        value = "The zone that this sensitivity instance applies to."
    )
    @JsonProperty
    private SensitivityZone zone;
    @ApiModelProperty(
        value = "The reason for making this taxon/zone sensitive"
    )
    @JsonProperty
    private String reason;
    @ApiModelProperty(
        value = "Any additional remarks"
    )
    @JsonProperty
    private String remarks;
    @ApiModelProperty(
        value = "The generalisation to apply to coordinates"
    )
    @JsonProperty
    private GeneralisationRule generalisation;
    @ApiModelProperty(
        value = "The date this instance applies from"
    )
    @JsonProperty
    private Date fromDate;
    @ApiModelProperty(
        value = "The date this instance applies to"
    )
    @JsonProperty
    private Date toDate;
    @ApiModelProperty(
        value = "Any transient events attached to this instance."
    )
    @JsonProperty
    private List<TransientEvent> transientEvents;


    /**
     * Generalise a latitude value according to this instance's genealisation rules.
     *
     * @param latitude The latitude
     * @param longitude The longitude
     *
     * @return The generalised latitude
     */
    public double generaliseLatitude(double latitude, double longitude) {
        return this.generalisation == null ? latitude : this.generalisation.generaliseLatitude(latitude, longitude);
    }

    /**
     * Generalise a longitude value according to this instance's generalisation rules.
     *
     * @param latitude The latitude
     * @param longitude The longitude
     *
     * @return The generalised longitude
     */
    public double generaliseLongitude(double latitude, double longitude) {
        return this.generalisation == null ? latitude : this.generalisation.generaliseLongitude(latitude, longitude);
    }

    /**
     * Format a lat/long to the precision specified, via the generalisation rule if available.
     *
     * @param v The value
     *
     * @return The value, appropriately formatted
     */
    public String format(double v) {
        return this.generalisation == null ? Double.toString(v) : this.generalisation.format(v);
    }

    /**
     * Withhold results entirely
     *
     * @retyrn True if the result should be withheld.
     */
    @JsonIgnore
    public boolean isWithhold() {
        return this.generalisation != null && this.generalisation.isWithhold();
    }

    /**
     * Provides the type of
     */
    public enum SensitivityType {
        /** An occurrence sensitive for conservation reasons */
        CONSERVATION,
        /** An occurrence sensitive for notifiable pest reasons */
        PEST
    }
}
