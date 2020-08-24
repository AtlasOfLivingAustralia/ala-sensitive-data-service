package au.org.ala.sds.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(builder = SensitiveTaxon.SensitiveTaxonBuilder.class)
@Value
@Builder
@EqualsAndHashCode
@ApiModel(
    description = "A description of a sensitive taxon."
)
public class SensitiveTaxon {
    @ApiModelProperty(
        value = "The taxon scientific name"
    )
    @JsonProperty
    private String scientificName;
    @ApiModelProperty(
        value = "The taxon identifier"
    )
    @JsonProperty
    private String taxonId;
    @ApiModelProperty(
        value = "The taxon family name"
    )
    @JsonProperty
    private String family;
    @ApiModelProperty(
        value = "The taxon accepted name, if this is a synonym"
    )
    @JsonProperty
    private String acceptedName;
    @ApiModelProperty(
        value = "Any common name associated with the taxon"
    )
    @JsonProperty
    private String commonName;
    @ApiModelProperty(
        value = "The taxon rank"
    )
    @JsonProperty
    private String taxonRank;
    @ApiModelProperty(
        value = "Sensitivity information associated with this taxon"
    )
    @JsonProperty
    private List<SensitivityInstance> instances;
}
