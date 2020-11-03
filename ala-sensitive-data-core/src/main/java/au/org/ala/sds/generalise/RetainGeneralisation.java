package au.org.ala.sds.generalise;

import au.org.ala.sds.api.SensitivityInstance;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import org.gbif.dwc.terms.Term;

import java.util.Optional;

/**
 * A no-op generalisation for a field.
 */
@ApiModel(
    description = "Retain a field, keeping its original value."
)
public class RetainGeneralisation extends SingleFieldGeneralisation {
    protected RetainGeneralisation() {
    }

    public RetainGeneralisation(Term field) {
        super(field);
    }

    /**
     * Return a no-op for this
     *
     * @param supplied The supplied value
     * @param instance The instance of the rule being applied
     *
     * @return The processed value. An empty value means no change, a present value, even if null means change.
     */
    @Override
    public Optional<Object> process(Optional<String> supplied, SensitivityInstance instance) {
        return null;
    }
}
