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
@JsonDeserialize(builder = SensitivityCategory.SensitivityCategoryBuilder.class)
@Value
@Builder
@EqualsAndHashCode
@ApiModel(
    description = "A specific sensitivity rule applying to a taxon in a particular zone."
)
public class SensitivityCategory {
    @ApiModelProperty(
        value = "The sensitivity category identifier"
    )
    @JsonProperty
    private String id;
    @ApiModelProperty(
        value = "A description of the sensitivity category."
    )
    @JsonProperty
    private String value;
    @ApiModelProperty(
        value = "The type of category",
        allowableValues = "CONSERVATION,PLANT_PEST"
    )
    @JsonProperty
    private String type;

    /**
     * Construct for suitable values.
     *
     * @param id The category identifier/code
     * @param value A category description
     * @param type The cateogry type
     */
    public SensitivityCategory(String id, String value, String type) {
        this.id = id;
        this.value = value;
        this.type = type;
    }
}
