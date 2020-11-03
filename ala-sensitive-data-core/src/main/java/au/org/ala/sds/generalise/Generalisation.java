package au.org.ala.sds.generalise;

import au.org.ala.sds.api.SensitivityInstance;
import au.org.ala.sds.api.SensitivityQuery;
import au.org.ala.sds.api.SensitivityReport;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.annotations.ApiModel;
import org.gbif.dwc.terms.Term;

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
@JsonTypeIdResolver(Generalisation.GeneralisationTypeIdResolver.class)
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
    public void process(SensitivityQuery query, SensitivityReport report) {
        if (!report.isSensitive())
            return;
        for (SensitivityInstance instance: report.getReport().getTaxon().getInstances())
            this.process(query.getProperties(), report.getOriginal(), report.getUpdated(), instance);
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

    /**
     * Type resolver for generalisations.
     * <p>
     * Generates a simple string for the generalisation type so that
     * what's going on is not explcitly tied to Java classes.
     * </p>
     * <p>
     * Class-name mappings are intialised from the <code>/generalisation-mapping.properties</code> resource.
     * </p>
     */
    public static class GeneralisationTypeIdResolver extends TypeIdResolverBase {
        private static final TypeFactory TYPE_FACTORY = TypeFactory.defaultInstance();
        private static final Map<Class<Generalisation>, String> CLASS_TO_ID = new HashMap<>();
        private static final Map<String, JavaType> ID_TO_CLASS = new HashMap<>();

        {
            try {
                CLASS_TO_ID.clear();
                ID_TO_CLASS.clear();
                Properties mapping = new Properties();
                mapping.load(this.getClass().getResourceAsStream("/generalisation-mapping.properties"));
                mapping.forEach((className, label) -> {
                    Class<Generalisation> cls = null;
                    try {
                        cls = (Class<Generalisation>) Class.forName(className.toString());
                    } catch (ClassNotFoundException e) {
                        throw new IllegalStateException("Unable to find " + className);
                    }
                    String name = label.toString();
                    if (CLASS_TO_ID.containsKey(cls))
                        throw new IllegalStateException("Duplcate class " + cls);
                    if (ID_TO_CLASS.containsKey(name))
                        throw new IllegalStateException("Duplicate name " + name);
                    JavaType type = TYPE_FACTORY.constructSimpleType(cls, null);
                    CLASS_TO_ID.put(cls, name);
                    ID_TO_CLASS.put(name, type);
                });
            } catch (IOException ex) {
                throw new IllegalStateException("Unable to construct class/id map for Generalisation", ex);
            }
        }

        public GeneralisationTypeIdResolver() {
            super(TypeFactory.defaultInstance().constructSimpleType(Generalisation.class, null), TypeFactory.defaultInstance());
        }

        /**
         * Get the list of names that are mapped.
         *
         * @return The name list
         */
        public static List<String> getNames() {
            List<String> names = new ArrayList<>(ID_TO_CLASS.keySet());
            Collections.sort(names);
            return names;
        }

        @Override
        public String idFromValue(Object value) {
            return this.idFromValueAndType(value, Generalisation.class);
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> suggestedType) {
            return CLASS_TO_ID.getOrDefault(value == null ? suggestedType : value.getClass(), "generalisation");
        }

        @Override
        public JavaType typeFromId(DatabindContext context, String id) throws IOException {
            return ID_TO_CLASS.computeIfAbsent(id, k -> { throw new IllegalArgumentException("No class for action " + k); });
        }

        /**
         * Accessor for mechanism that this resolver uses for determining
         * type id from type. Mostly informational; not required to be called
         * or used.
         */
        @Override
        public JsonTypeInfo.Id getMechanism() {
            return JsonTypeInfo.Id.CUSTOM;
        }
    }
}
