package au.org.ala.sds.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Value
@SuperBuilder
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@ApiModel(
    description = "The basic information needed to process an occurrence record, including taxon, broad location and source. " +
        "Additional information from the occurrence record is supplied as a set of properties, keyed by URI, prefixed name or field name " +
        "-- the non-namespace part of a Darwin Core URI. " +
        "The result from the query may choose to process the values to meet the sensitivity rules."
)
public class ProcessQuery extends SensitivityQuery {
    // Note the examples don't work due to the swagger 2.0 implementation, so we hide them.
    @ApiModelProperty(
        value = "The occurrence record properties",
        notes = "Key/value properties using Darwin Core terms or other relevant terms. " +
                "Example: {\"decimalLatitude\": \"-25.345\", \"decimalLongitude\": \"135.456\"}",
        hidden = true
    )
    @JsonProperty
    private Map<String, String> properties;

    @ApiModelProperty(hidden = true)
    @JsonProperty
    private List<String> zones;
}
