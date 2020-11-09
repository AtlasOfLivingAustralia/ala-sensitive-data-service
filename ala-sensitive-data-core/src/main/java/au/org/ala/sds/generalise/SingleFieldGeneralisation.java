package au.org.ala.sds.generalise;

import au.org.ala.sds.api.SensitivityInstance;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.gbif.dwc.terms.Term;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A generalisation that is applied to a single field.
 */
abstract public class SingleFieldGeneralisation extends Generalisation {
    /** The field to apply the generalisation to */
    @JsonProperty
    @Getter
    private FieldAccessor field;

    /**
     * Default constructor for jackson.
     */
    protected SingleFieldGeneralisation() {
    }

    /**
     * Create for a field.
     *
     * @param field The field to access
     */
    public SingleFieldGeneralisation(Term field) {
        this.field = new FieldAccessor(field);
    }

    /**
     * Make whatever modifications are necessary to accomodate the generalisation.
     *
     * @param supplied The supplied values
     * @param original The store of original values (taken from supplied)
     * @param updated  To store of updated values
     * @param instance The instance of the rule being applied
     */
    @Override
    public void process(Map<String, String> supplied, Map<String, Object> original, Map<String, Object> updated, SensitivityInstance instance) {
        if (this.field.isSet(updated))
            return;
        FieldAccessor.Value value = this.field.get(supplied);
        if (!value.hasValue() && !this.isProcessNulls())
            return;
        Optional<Object> processed = this.process(value.getValue(), instance);
        if (processed != null) {
            original.put(value.getKey(), value.getValue().orElse(null));
            updated.put(value.getKey(), processed.orElse(null));
        }
    }

    /**
     * Make whatever modifications are necessary to accomodate the generalisation
     * to a field.
     *
     * @param supplied The supplied value, empty for a missing value
     * @param instance The instance of the rule being applied
     *
     * @return The processed value. A null value means no change. An empty value means change to null, a present value means change to that value.
     */
    abstract public Optional<Object> process(Optional<String> supplied, SensitivityInstance instance);

    /**
     * Get the fields that this generalisation uses to gather and modify information.
     *
     * @return The single field
     */
    @Override
    public Set<FieldAccessor> getFields() {
        return Collections.singleton(this.field);
    }
}
