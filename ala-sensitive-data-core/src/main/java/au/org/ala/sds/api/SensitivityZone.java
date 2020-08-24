package au.org.ala.sds.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(builder = SensitivityZone.SensitivityZoneBuilder.class)
@Builder
@Value
@EqualsAndHashCode
@ApiModel(
    description = "A geographical zone, giving boundaries to sensitivity declarations."
)
public class SensitivityZone {
    @ApiModelProperty(
        value = "The zone identifier"
    )
    @JsonProperty
    private String id;
    @ApiModelProperty(
        value = "The name of the zone."
    )
    @JsonProperty
    private String name;
    @ApiModelProperty(
        value = "The geospatial layer associated with this zone"
    )
    @JsonProperty
    private String layerId;
    @ApiModelProperty(
        value = "The type of zone",
        allowableValues = "COUNTRY,STATE,EXTERNAL_TERRITORY,QUARANTINE_ZONE"
    )
    @JsonProperty
    private String type;

    /**
     * Construct for suitable values.
     *
     * @param id The zone identifier
     * @param name The zone name
     * @param layerId The genspatial layer identifier
     * @param type The zone type
     */
    public SensitivityZone(String id, String name, String layerId, String type) {
        this.id = id;
        this.name = name;
        this.layerId = layerId;
        this.type = type;
    }
}
