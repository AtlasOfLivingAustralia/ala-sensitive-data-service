package au.org.ala.sds.generalise;

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
 *     <dt>{3}</dt><dd>Reason string</dd>
 *     <dt>{4}</dt><dd>Remarks string</dd>
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
    public MessageGeneralisation(Term field, String message, boolean append) {
        super(field);
        this.message = message;
        this.format = new MessageFormat(this.message);
        this.append = append;
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
         String msg = this.format.format(new Object[] {
            instance.getAuthority(),
            category == null ? "" : category.getValue(),
            zone == null ? "" : zone.getName(),
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
}
