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
@JsonDeserialize(builder = ValidationReport.ValidationReportBuilder.class)
@Value
@Builder
@EqualsAndHashCode
@ApiModel(
    description = "Information about what has been found in terms of sensitivity."
)
public class ValidationReport {
    @ApiModelProperty(
        value = "The taxon that this report has been applied to."
    )
    @JsonProperty
    private SensitiveTaxon taxon;
    @ApiModelProperty(
        value = "A list of messages that apply to this report"
    )
    @JsonProperty
    private List<Message> messages;
    @ApiModelProperty(
        value = "The general category of the report"
    )
    @JsonProperty
    private String category;
    @ApiModelProperty(
        value = "A description of what has been discovered about the occurrence"
    )
    @JsonProperty
    private String assertion;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonDeserialize(builder = Message.MessageBuilder.class)
    @Value
    @Builder
    @EqualsAndHashCode
    @ApiModel(
        description = "A validation message."
    )
    public static class Message {
        @ApiModelProperty(
            value = "The message type.",
            allowableValues = "ERROR,WARNING,ALERT,INFO"
        )
        @JsonProperty
        private String type;
        @ApiModelProperty(
            value = "The message category."
        )
        @JsonProperty
        private String category;
        @ApiModelProperty(
            value = "The message text."
        )
        @JsonProperty
        private String message;

        /**
         * Construct a message.
         *
         * @param type The message type
         * @param category The message category
         * @param message The message
         */
        public Message(String type, String category, String message) {
            this.type = type;
            this.category = category;
            this.message = message;
        }
    }
}
