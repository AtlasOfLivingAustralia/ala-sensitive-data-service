package au.org.ala.sds.generalise;

import au.org.ala.sds.api.GeneralisationRule;
import au.org.ala.sds.api.SensitivityCategory;
import au.org.ala.sds.api.SensitivityInstance;
import au.org.ala.sds.api.SensitivityZone;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import org.gbif.dwc.terms.Term;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * Set or append a message to a field.
 * <p>
 * The messge is built using a {@link MessageFormat} and can have the following parameters from {@link SensitivityInstance}:
 * </p>
 * <dl>
 *     <dt>{0}</dt><dd>Authority name</dd>
 *     <dt>{1}</dt><dd>Category name (value)</dd>
 *     <dt>{2}</dt><dd>Zone name</dd>
 *     <dt>{3}</dt><dd>Generalisation</dd>
 *     <dt>{4}</dt><dd>Reason string</dd>
 *     <dt>{5}</dt><dd>Remarks string</dd>
 * </dl>
 */
@ApiModel(
    description = "Set or append a message to a text field."
)
public class MessageGeneralisation extends SingleFieldGeneralisation {
    /** The message to use */
    @JsonProperty
    @Getter
    private String message;
    /** The message, ready to be formatted */
    @JsonIgnore
    private MessageFormat format;
    /** Append to an existing message */
    @JsonProperty(defaultValue = "true")
    @Getter
    private boolean append = true;
    /** Message for withhold only */
    @JsonProperty
    @Getter
    private Trigger trigger = Trigger.ANY;

    /**
     * Default constructor for Jackson
     */
    protected MessageGeneralisation() {
    }

    /**
     * Construct for a message format.
     *
     * @param field The field to append to
     * @param message
     */
    public MessageGeneralisation(Term field, String message, boolean append, Trigger trigger) {
        super(field);
        this.message = message;
        this.append = append;
        this.trigger = trigger;
    }

    /**
     * Lazily intialised format from the message.
     *
     * @return The message format from the message
     */
    protected MessageFormat getFormat() {
        if (this.format == null)
            this.format = new MessageFormat(this.message);
        return this.format;
    }

    /**
     * Set or append the text contained in the message pattern to the field.
     *
     * @param supplied The supplied value, empty for a missing value
     * @param instance The instance of the rule being applied
     *
     * @return The processed value. A null value means no change. An empty value means change to null, a present value means change to that value.
     */
    @Override
    public Optional<Object> process(Optional<String> supplied, SensitivityInstance instance) {
        SensitivityCategory category = instance.getCategory();
        SensitivityZone zone = instance.getZone();
        GeneralisationRule rule = instance.getGeneralisation();
        if (this.trigger == Trigger.GENERALISE && (rule == null || rule.isWithhold()))
            return null;
        if (this.trigger == Trigger.WITHHOLD && (rule == null || !rule.isWithhold()))
            return null;
        String msg = this.getFormat().format(new Object[] {
            instance.getAuthority(),
            category == null ? "" : category.getValue(),
            zone == null ? "" : zone.getName(),
            rule == null ? "" : rule.getGeneralisation(),
            instance.getReason(),
            instance.getRemarks()
        });
        if (this.append && supplied.isPresent()) {
            msg = supplied.get() + " " + msg;
        }
        msg = msg.trim();
        return msg.isEmpty() ? Optional.empty() : Optional.of(msg);
    }

    /**
     * Messages need to set null values.
     *
     * @return True
     */
    @Override
    public boolean isProcessNulls() {
        return true;
    }

    /** The conditions under which the message is triggered */
    @ApiModel(
        description = "The conditions under which a message is created"
    )
    public static enum Trigger {
        /** Trigger on any event */
        ANY,
        /** Trigger on a generalisation */
        GENERALISE,
        /** Trigger on a withhold requirement */
        WITHHOLD
    }
}
