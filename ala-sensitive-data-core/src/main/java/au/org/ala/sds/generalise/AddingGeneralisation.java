package au.org.ala.sds.generalise;

import au.org.ala.sds.api.GeneralisationRule;
import au.org.ala.sds.api.SensitivityInstance;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.gbif.dwc.terms.Term;

import java.util.Optional;

/**
 * Add a specific integer amount to an existing value.
 * <p>
 * If {@link #useSensitivity} is true then add the generalisation in metres in the instance's generalisation rule.
 * If {@link #add} is non-zero, add that amount as well.
 * If {@link #retainUnparsable} is true, unparstable amounnts (eg. "10-1000") are left as-is. Otherwise thy are replaced by the value.
 * </p>
 */
public class AddingGeneralisation extends SingleFieldGeneralisation {
    /** Retain unparsable values */
    @JsonProperty
    @Getter
    private boolean retainUnparsable = true;
    /** Use the sensitivity instance generalisation */
    @JsonProperty
    @Getter
    private boolean useSensitivity = true;
    /** Add a specific amount to the value */
    @JsonProperty
    @Getter
    private int add = 0;

    protected AddingGeneralisation() {
    }

    /**
     * Create for a field.
     *
     * @param field The field to access
     * @param retainUnparsable If true, unparsable values are left as-is, rather than overwritten
     * @param useSensitivity Use the generalisation in the sensitivty as an adder
     * @param add The amount to add, regardless of the instance
     */
    public AddingGeneralisation(Term field, boolean retainUnparsable, boolean useSensitivity, int add) {
        super(field);
        this.retainUnparsable = retainUnparsable;
        this.useSensitivity = useSensitivity;
        this.add = add;
    }

    /**
     * Add an amount to the existing value.
     *
     * @param supplied The supplied value, empty for a missing value
     * @param instance The instance of the rule being applied
     * @return The processed value. A null value means no change. An empty value means change to null, a present value means change to that value.
     */
    @Override
    public Optional<Object> process(Optional<String> supplied, SensitivityInstance instance) {
        int value = 0;
        if (supplied.isPresent() && !supplied.get().isEmpty()) {
            try {
                value = Integer.parseInt(supplied.get());
            } catch (NumberFormatException ex) {
                if (retainUnparsable)
                    return null;
            }
        }
        if (this.useSensitivity) {
            GeneralisationRule rule = instance.getGeneralisation();
            value += rule == null ? 0 : rule.getGeneralisationInMetres();
        }
        value += this.add;
        return Optional.of(Integer.toString(value));
    }


    /**
     * Additions can handle null values.
     *
     * @return True
     */
    @Override
    public boolean isProcessNulls() {
        return true;
    }

}
