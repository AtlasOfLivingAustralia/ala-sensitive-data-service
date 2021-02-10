package au.org.ala.sds.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(builder = SensitivityReport.SensitivityReportBuilder.class)
@Value
@Builder
@EqualsAndHashCode
@ApiModel(
    description = "The report delivered back from a sensitivty query"
)
public class SensitivityReport {
    @ApiModelProperty(
        value = "Whether the result is valid"
    )
    @JsonProperty
    private boolean valid;
    @ApiModelProperty(
        value = "Is this a sensitive occurrence?"
    )
    @JsonProperty
    private boolean sensitive;
    @ApiModelProperty(
        value = "Should this occurrence be loaded?"
    )
    @JsonProperty
    private boolean loadable;
    @ApiModelProperty(
        value = "Should there be access control on this occurrence?"
    )
    @JsonProperty
    private boolean accessControl;
    @ApiModelProperty(
        value = "The validation report for this occurrence"
    )
    @JsonProperty
    private ValidationReport report;
    @ApiModelProperty(
        value = "Original occurrence properties"
    )
    @JsonProperty
    private Map<String, Object> original;
    @ApiModelProperty(
        value = "Updated occurrence properties"
    )
    @JsonProperty
    private Map<String, Object> updated;
}
