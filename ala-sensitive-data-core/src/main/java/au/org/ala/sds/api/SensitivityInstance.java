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
@JsonDeserialize(builder = SensitivityInstance.SensitivityInstanceBuilder.class)
@Value
@Builder
@EqualsAndHashCode
@ApiModel(
    description = "A specific sensitivity rule applying to a taxon in a particular zone."
)
public class SensitivityInstance {
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
}
