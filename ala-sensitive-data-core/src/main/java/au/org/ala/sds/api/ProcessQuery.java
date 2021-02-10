package au.org.ala.sds.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
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
    @ApiModelProperty(
        value = "The occurrence record properties"
    )
    @JsonProperty
    private Map<String, String> properties;

}
