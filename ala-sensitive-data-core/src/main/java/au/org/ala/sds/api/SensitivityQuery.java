package au.org.ala.sds.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import lombok.*;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Value
@SuperBuilder
@EqualsAndHashCode
@NonFinal
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@ApiModel(
    description = "The basic information needed to process an occurrence record, including taxon, broad location and source. "
)
public class SensitivityQuery {
    @ApiModelProperty(
        value = "The scientific name",
        example = "Eucalyptus rossii",
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
    @JsonProperty
    private String scientificName;

    @ApiModelProperty(
        value = "The taxon identifier",
        example = "https://id.biodiversity.org.au/node/apni/2900822",
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

    @ApiModelProperty(
        value = "The data resource identifier",
        example = "dr1329",
        extensions = {
            @Extension(
                name = "x-reference" ,
                properties = {
                    @ExtensionProperty(
                        name = "uri" ,
                        value = "http://rs.tdwg.org/dwc/terms/datasetID"
                    )
                }
            )
        }
    )
    @JsonProperty
    private String dataResourceUid;

    @ApiModelProperty(
        value = "The state or province name",
        notes = "Corresponds to the name provided in the zones list",
        example = "New South Wales",
        extensions = {
            @Extension(
                name = "x-reference" ,
                properties = {
                    @ExtensionProperty(
                        name = "uri" ,
                        value = "http://rs.tdwg.org/dwc/terms/stateProvince"
                    )
                }
            )
        }
    )
    @JsonProperty
    private String stateProvince;

    @ApiModelProperty(
        value = "The country name",
        notes = "Corresponds to the name provided in the zones list",
        example = "Australia",
        extensions = {
            @Extension(
                name = "x-reference" ,
                properties = {
                    @ExtensionProperty(
                        name = "uri" ,
                        value = "http://rs.tdwg.org/dwc/terms/country"
                    )
                }
            )
        }
    )
    @JsonProperty
    private String country;

    @ApiModelProperty(
        value = "The zone identifiers that cover the occurrence record location",
        notes = "State/province and country are supplied separately. Corresponds to the identifiers provided in the zones list",
        example = "FFEZ"
    )
    @JsonProperty
    private List<String> zones;
}
