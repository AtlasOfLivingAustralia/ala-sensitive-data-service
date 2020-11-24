package au.org.ala.sds.generalise;

import au.org.ala.sds.api.ProcessQuery;
import au.org.ala.sds.api.SensitivityInstance;
import au.org.ala.sds.api.SensitivityReport;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.annotations.ApiModel;

import java.io.IOException;
import java.util.*;

/**
 * A generalisation rule.
 * <p>
 * This is a json-serialisable abstract class, intended for use in a web service.
 * Polymorphism is achieved through the <code>action</code> property, which is mapped
 * via {@link GeneralisationTypeIdResolver} to an implermenting class.
 * The action property is used to avoid very java-specific names in the web service payload.
 * </p>
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CUSTOM, include=JsonTypeInfo.As.PROPERTY, property="action")
@JsonTypeIdResolver(GeneralisationTypeIdResolver.class)
@ApiModel(
    description = "A description of a generalisation that can be applied to parts of the occurrence record. " +
        "Each generalisation type is given by the 'action' property, which corresponds " +
        "to a class of actions that can be used for generalisation.",
    subTypes = {
        ClearGeneralisation.class,
        LatLongGeneralisation.class,
        MessageGeneralisation.class,
        RetainGeneralisation.class
    }
)
abstract public class Generalisation {
    /**
     * Make whatever modifications are necessary to accomodate the generalisation.
     *
     * @param supplied The supplied values
     * @param original The store of original values (taken from supplied)
     * @param updated To store of updated values
     * @param instance The instance of the rule being applied
     */
    abstract public void process(Map<String, String> supplied, Map<String, Object> original, Map<String, Object> updated, SensitivityInstance instance);

    /**
     * Make whatever modifications are necessary to accomodate the generalisation.
     *
     * @param supplied The supplied values
     * @param original The store of original values (taken from supplied)
     * @param updated To store of updated values
     * @param report The instance of the rule being applied
     */
    public void process(Map<String, String> supplied, Map<String, Object> original, Map<String, Object> updated, SensitivityReport report) {
        if (!report.isSensitive())
            return;
        for (SensitivityInstance instance: report.getReport().getTaxon().getInstances())
            this.process(supplied, original, updated, instance);
    }

    /**
     * Process a query/report pair.
     * <p>
     * If the report is not sensitive, the generalisation is not applied.
     * All instances are applied using this rule.
     * </p>
     *
     * @param query The original query
     * @param report The report (in progress)
     *
     * @see #process(Map, Map, Map, SensitivityInstance)
     */
    public void process(ProcessQuery query, SensitivityReport report) {
        this.process(query.getProperties(), report.getOriginal(), report.getUpdated(), report);
    }

    /**
     * Provide processing for null values?
     * <p>
     * Generally, a null or empty value in the source corresponds to a
     * null value when modified.
     * Return true if null values should be passed on for further processing.
     * </p>
     *
     * @return True if null values are processed
     */
    @JsonIgnore
    public boolean isProcessNulls() {
        return false;
    }

    /**
     * Get the fields that this generalisation uses to gather and modify information.
     *
     * @return A set of fields that correspond to the terms used by this
     */
    @JsonIgnore
    abstract public Set<FieldAccessor> getFields();

}
