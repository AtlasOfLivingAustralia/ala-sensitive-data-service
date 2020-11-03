package au.org.ala.sds.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(builder = TransientEvent.TransientEventBuilder.class)
@Value
@Builder
@EqualsAndHashCode
@ApiModel(
    description = "A transient event in a particular zone."
)
public class TransientEvent {
    @ApiModelProperty(
        value = "The date the event occurred",
        required = true
    )
    @JsonProperty
    private Date eventDate;
    @ApiModelProperty(
        value = "The zone that this event occurred in."
    )
    @JsonProperty
    private SensitivityZone zone;
}
