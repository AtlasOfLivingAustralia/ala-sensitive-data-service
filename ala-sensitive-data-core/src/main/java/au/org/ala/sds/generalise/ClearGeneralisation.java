package au.org.ala.sds.generalise;

import au.org.ala.sds.api.SensitivityInstance;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import org.gbif.dwc.terms.Term;

import java.util.Optional;

/**
 * Clear a field of any values
 */
@ApiModel(
    description = "Clear a field, if it has a value"
)
public class ClearGeneralisation extends SingleFieldGeneralisation {
    protected ClearGeneralisation() {
    }

    public ClearGeneralisation(@JsonProperty Term field) {
        super(field);
    }

    /**
     * Force a null value.
     *
     * @param supplied The supplied value
     * @param instance The instance of the rule being applied
     *
     * @return The processed value. An empty value means no change, a present value, even if null means change.
     */
    @Override
    public Optional<Object> process(Optional<String> supplied, SensitivityInstance instance) {
        return Optional.empty();
    }
}
