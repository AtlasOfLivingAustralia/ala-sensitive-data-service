package au.org.ala.sds.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(builder = SpeciesCheck.SpeciesCheckBuilder.class)
@Value
@Builder
@EqualsAndHashCode
@ApiModel(
    description = "Request to see if a species is potentially sensitive. " +
        "Information on a potential species match, both name and, if available, the taxon identifier"
)
public class SpeciesCheck {
    // The scientificName parameter has been removed. Please use the taxonId parameter instead.
    // Retained temporarily to return a 400 Bad Request error if a client sends it.
    @ApiModelProperty(
        value = "The scientific name - DEPRECATED. Use `taxonId`, which is a required field.",
        extensions = {
            @Extension(
                name = "x-reference",
                properties = {
                    @ExtensionProperty(
                        name = "uri",
                        value = "http://rs.tdwg.org/dwc/terms/scientificName"
                    )
                }
            )
        }
    )
    @Deprecated
    @JsonProperty
    private String scientificName;

    @ApiModelProperty(
        value = "The taxon identifier (required)",
        required = true,
        example = "https://id.biodiversity.org.au/node/apni/2899640",
        extensions = {
            @Extension(
                name = "x-reference" ,
                properties = {
                    @ExtensionProperty(
                        name = "uri" ,
                        value = "http://rs.tdwg.org/dwc/terms/taxonID"
                    )
                }
            )
        }
    )
    @JsonProperty
    private String taxonId;
}
